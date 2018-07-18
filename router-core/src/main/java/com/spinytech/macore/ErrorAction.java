package com.spinytech.macore;

import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;

/**
 * Created by wanglei on 2016/12/28.
 */

public class ErrorAction implements RouterAction {

    private static final String DEFAULT_MESSAGE = "Something was really wrong. Ha ha!";

    public ErrorAction() {
    }

    @Override
    public Bundle invoke(Context context, HashMap requestData, RouterCallback callback) {
        if (callback != null) {
            Bundle result = new Bundle();
            result.putString(RouterCallback.KEY_ERROR_MSG,DEFAULT_MESSAGE);
            callback.onResult(RouterCallback.CODE_NOT_IMPLEMENT, result);
        }
        return null;
    }
}
