package com.spinytech.musicdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.spinytech.macore.RouterAction;
import com.spinytech.macore.RouterCallback;

import java.util.HashMap;

/**
 * Created by wanglei on 2016/12/28.
 */

public class ShutdownAction implements RouterAction {

    @Override
    public Bundle invoke(Context context, HashMap requestData, RouterCallback callback) {
        context.getApplicationContext().stopService(new Intent(context, MusicService.class));
        if (callback != null) {
            Bundle result = new Bundle();
            result.putString(RouterCallback.KEY_VALUE,"shutdown success");
            callback.onResult(RouterCallback.CODE_SUCCESS, result);
        }
        return null;
    }
}
