package com.spinytech.macore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.BundleCompat;
import android.util.Log;
import android.util.SparseArray;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * The Local Router
 */

public class LocalRouter {
    private static final String TAG = "LocalRouter";
    private static LocalRouter sInstance = null;
    public static final int MESSAGE_CALLBACK = 100;
    private HashMap<String, RouterProvider> mProviders = null;
    private Context mApplication;
    private final String mProcessName;
    private Messenger mCallbackMessenger;
    // 记录夸进程 callback
    private int uniqueId = 0;
    private SparseArray<WeakReference<RouterCallback>> mIPCCallbacks;
    private Handler mCallbackHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CALLBACK:
                    int callbackId = msg.arg1;
                    WeakReference<RouterCallback> routerCallbackWeakReference = mIPCCallbacks.get(callbackId);
                    if(routerCallbackWeakReference!=null && routerCallbackWeakReference.get()!=null){
                        RouterCallback routerCallback = routerCallbackWeakReference.get();
                        routerCallback.onResult(msg.arg2, (Bundle) msg.obj);
                    }
                    break;
                default:
                    break;
            }

        }
    };

    private LocalRouter(Context context) {
        mApplication = context;
        mProcessName = ProcessUtil.getProcessName(context);
        mProviders = new HashMap<>();
        mCallbackMessenger = new Messenger(mCallbackHandler);
        mIPCCallbacks = new SparseArray<>();
    }

    public Context getApplication(){
        return mApplication;
    }
    public static synchronized LocalRouter init(Context context) {
        if (sInstance == null) {
            sInstance = new LocalRouter(context);
        }
        return sInstance;
    }
    public static synchronized LocalRouter getInstance() {
        if (sInstance == null) {
            throw new RuntimeException("Local Router must be init first");
        }
        return sInstance;
    }


    void registerProvider(String providerName, RouterProvider provider) {
        mProviders.put(providerName, provider);
    }

    /**
     * 路由只涉及功能分发，不做线程切换，在调起线程执行，并在该线程回调，如果有耗时或者UI线程需求，自行切换
     * @param context context
     * @param routerRequest 请求
     * @param callback 回调
     */
    void route(Context context, RouterRequest routerRequest, RouterCallback callback) {

        Log.d(TAG, "Process:Local route start: " + System.currentTimeMillis());
        // Local request
        if (mProcessName.equals(routerRequest.getDomain())) {
            Log.d(TAG, "Process:Local find action start: " + System.currentTimeMillis());
            RouterAction targetAction = findRequestAction(routerRequest);
            HashMap<String, String> params = new HashMap<>();
            params.putAll(routerRequest.getData());
            routerRequest.isIdle.set(true);
            Log.d(TAG, "Process:Local find action end: " + System.currentTimeMillis());
            // Sync result, return the result immediately.
            try {
                targetAction.invoke(context, params, callback);
            } catch (Exception e) {
                e.printStackTrace();
                Bundle result = new Bundle();
                result.putString(RouterCallback.KEY_ERROR_MSG, e.getMessage());
                callback.onResult(RouterCallback.CODE_ERROR, result);
            }
            Log.d(TAG, "Process:Local route end: " + System.currentTimeMillis());
        } else {
            // IPC
            // ContentProvider
            // ContentService ContentResolver 不需要观察者，这里直接跨进程过去了
            Uri targetUri = Uri.parse("content://"+routerRequest.getDomain() + ProcessUtil.IPC_AUTHORITY_SUFFIX);
            Bundle callBackBundle = new Bundle();
            BundleCompat.putBinder(callBackBundle,"callback",mCallbackMessenger.getBinder());
            uniqueId++;
            callBackBundle.putInt("callbackId",uniqueId);
            mIPCCallbacks.put(uniqueId,new WeakReference<RouterCallback>(callback));
            // api 11
            try {
                context.getContentResolver().call(targetUri, "ipcRouter", routerRequest.toString(), callBackBundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private RouterAction findRequestAction(RouterRequest routerRequest) {
        RouterProvider targetProvider = mProviders.get(routerRequest.getProvider());
        ErrorAction defaultNotFoundAction = new ErrorAction();
        if (null == targetProvider) {
            return defaultNotFoundAction;
        } else {
            RouterAction targetAction = targetProvider.findAction(routerRequest.getAction());
            if (null == targetAction) {
                return defaultNotFoundAction;
            } else {
                return targetAction;
            }
        }
    }

}
