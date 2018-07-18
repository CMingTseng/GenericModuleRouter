package com.spinytech.macore;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.BundleCompat;
import android.text.TextUtils;
import android.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author guofeng05
 */
public class RouterIPCProvider extends ContentProvider {
    public static final String ROUTER_MODULE_ENTRY_CLZ = "ModuleRouterEntry";
    public static final String PROVIDE_ALL_MODULE = "provideAllModule";


    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if ("ipcRouter".equals(method) && !TextUtils.isEmpty(arg)) {
            RouterRequest routerRequest = new RouterRequest(arg);
            Messenger callbackMessenger = null;
            int callbackId = -1;
            if (extras != null) {
                callbackMessenger = new Messenger(BundleCompat.getBinder(extras, "callback"));
                callbackId = extras.getInt("callbackId");
            }

            final Messenger finalCallback = callbackMessenger;
            final int finalCallbackId = callbackId;
            LocalRouter.getInstance().route(getContext(), routerRequest, new RouterCallback() {
                @Override
                public void onResult(int resultCode, Bundle resultData) {
                    Message message = Message.obtain();
                    message.what = LocalRouter.MESSAGE_CALLBACK;
                    message.arg1 = finalCallbackId;
                    message.arg2 = resultCode;
                    message.obj = resultData;
                    if (finalCallback != null) {
                        // 有回调才回调
                        try {
                            finalCallback.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    // 自动初始化注入
    @Override
    public boolean onCreate() {
        // android.os.Debug.waitForDebugger();
        Context context = getContext();
        String packageName = context.getPackageName();
        try {
            Class<?> moduleEntry = Class.forName(packageName + "." + ROUTER_MODULE_ENTRY_CLZ);
            Method provideAllModule = moduleEntry.getMethod(PROVIDE_ALL_MODULE);
            Object moduleList = provideAllModule.invoke(null);
            RouterManager.getInstance().init(context, (List<Pair<String, String>>) moduleList);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}