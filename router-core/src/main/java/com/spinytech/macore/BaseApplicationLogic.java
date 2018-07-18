package com.spinytech.macore;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 *
 * @author wanglei
 * @date 2016/11/25
 */

public class BaseApplicationLogic {
    protected Context mApplication;


    public void setApplication(@NonNull Context application) {
        mApplication = application;
    }

    public Context getApplication(){
        return mApplication;
    }



    public BaseApplicationLogic() {
    }

    public void onCreate(Context context){
    }
}
