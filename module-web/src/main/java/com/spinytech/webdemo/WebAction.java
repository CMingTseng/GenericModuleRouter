package com.spinytech.webdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.spinytech.macore.RouterAction;
import com.spinytech.macore.RouterCallback;

import java.util.HashMap;

/**
 * Created by wanglei on 2017/1/4.
 */

public class WebAction implements RouterAction {

    @Override
    public void invoke(Context context, HashMap requestData, RouterCallback callback) {
        if (context instanceof Activity) {
            Intent i = new Intent(context, WebActivity.class);
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, WebActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        if (callback != null) {
            Bundle result = new Bundle();
            result.putString(RouterCallback.KEY_VALUE,"web success");
            callback.onResult(RouterCallback.CODE_SUCCESS, result);
        }
    }
}
