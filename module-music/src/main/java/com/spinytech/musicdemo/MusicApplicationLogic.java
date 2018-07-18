package com.spinytech.musicdemo;

import android.content.Context;

import com.spinytech.macore.BaseApplicationLogic;
import com.spinytech.macore.RouterManager;

/**
 * Created by wanglei on 2016/11/30.
 */

public class MusicApplicationLogic extends BaseApplicationLogic {
    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        RouterManager.getInstance().registerProvider("music",new MusicProvider());
    }
}
