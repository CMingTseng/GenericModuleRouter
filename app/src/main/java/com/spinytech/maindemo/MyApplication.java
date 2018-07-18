package com.spinytech.maindemo;

import android.app.Application;

/**
 * Created by wanglei on 2016/11/29.
 */

public class MyApplication extends Application{


    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // 采用 ContentProvider 默认初始化，注意在某些插件框架不支持 ContentProvider 的时候，记得手动在 onCreate 初始化
        // RouterManager.getInstance().init(this, ModuleRouterEntry.provideAllModule());
    }

}
