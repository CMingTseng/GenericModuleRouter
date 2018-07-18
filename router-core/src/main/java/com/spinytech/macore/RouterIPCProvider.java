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

/**
 * @author guofeng05
 */
public class RouterIPCProvider extends ContentProvider {


    @Override
    public boolean onCreate() {
        // android.os.Debug.waitForDebugger();
        Context context = getContext();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if ("ipcRouter".equals(method) && !TextUtils.isEmpty(arg) && extras != null) {
            RouterRequest routerRequest = new RouterRequest(arg);
            final Messenger callbackMessenger = new Messenger(BundleCompat.getBinder(extras,"callback"));
            final int callbackId = extras.getInt("callbackId");
            LocalRouter.getInstance().route(getContext(), routerRequest, new RouterCallback() {
                @Override
                public void onResult(int resultCode, Bundle resultData) {
                    Message message = Message.obtain();
                    message.what = LocalRouter.MESSAGE_CALLBACK;
                    message.arg1 = callbackId;
                    message.arg2 = resultCode;
                    message.obj = resultData;
                    try {
                        callbackMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return null;
    }


}