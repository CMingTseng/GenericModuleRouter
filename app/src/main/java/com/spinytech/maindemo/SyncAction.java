package com.spinytech.maindemo;

import android.content.Context;
import android.os.Bundle;

import com.spinytech.macore.RouterAction;
import com.spinytech.macore.RouterCallback;

import java.util.HashMap;

/**
 * Created by wanglei on 2016/12/28.
 */

public class SyncAction implements RouterAction {

    @Override
    public void invoke(Context context, HashMap requestData, RouterCallback callback) {
        if (callback != null) {
            Bundle result = new Bundle();
            result.putString(RouterCallback.KEY_VALUE,"sync success");
            callback.onResult(RouterCallback.CODE_SUCCESS, result);
        }
    }
}
