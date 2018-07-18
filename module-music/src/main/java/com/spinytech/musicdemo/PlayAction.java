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

public class PlayAction implements RouterAction {

    @Override
    public Bundle invoke(Context context, HashMap requestData, RouterCallback callback) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra("command", "play");
        context.startService(intent);
        if (callback != null) {
            Bundle result = new Bundle();
            result.putString(RouterCallback.KEY_VALUE,"play success");
            callback.onResult(RouterCallback.CODE_SUCCESS,result);
        }
        return null;
    }

}
