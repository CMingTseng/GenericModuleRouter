package com.spinytech.maindemo;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.spinytech.macore.ParceableAttachObject;
import com.spinytech.macore.RouterAction;
import com.spinytech.macore.RouterCallback;

import java.util.HashMap;

/**
 * Created by wanglei on 2017/2/15.
 */

public class AttachObjectAction implements RouterAction {

    @Override
    public Bundle invoke(Context context, HashMap requestData, RouterCallback callback) {
        if(requestData!= null && requestData.containsKey("textview")){
            Object textview = requestData.get("textview");
            if(textview instanceof TextView){
                ((TextView) textview).setText("The text is changed by async.");
            }
        }
        if (callback != null) {
            Bundle result = new Bundle();
            result.putString(RouterCallback.KEY_VALUE,"attach object success");
            Toast toast = Toast.makeText(context, "toast from attach", Toast.LENGTH_SHORT);

            ParceableAttachObject attachObject = new ParceableAttachObject();
            attachObject.obj = toast;
            result.putParcelable("toast",attachObject);
            callback.onResult(RouterCallback.CODE_SUCCESS, result);
        }
        return null;
    }
}
