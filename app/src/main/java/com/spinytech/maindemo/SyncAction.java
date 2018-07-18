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
    public Bundle invoke(Context context, HashMap requestData, RouterCallback callback) {
        Bundle result = new Bundle();
        result.putString(RouterCallback.KEY_VALUE, "sync success");
        if (callback != null) {
            callback.onResult(RouterCallback.CODE_SUCCESS, result);
        }
        // 同步返回结果
        return result;
    }
}
