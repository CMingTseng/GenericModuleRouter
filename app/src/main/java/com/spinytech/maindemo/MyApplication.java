package com.spinytech.maindemo;

import android.app.Application;
import android.util.Pair;

import com.spinytech.macore.BaseApplicationLogic;
import com.spinytech.macore.RouterManager;

import java.util.LinkedList;

/**
 * Created by wanglei on 2016/11/29.
 */

public class MyApplication extends Application{

    public static LinkedList<Pair<String,String>> applicationLogic;

    static {
        applicationLogic = new LinkedList<>();
        applicationLogic.add(new Pair<String, String>("com.spinytech.maindemo", "com.spinytech.maindemo.MainApplicationLogic"));
        applicationLogic.add(new Pair<String, String>("com.spinytech.maindemo", "com.spinytech.webdemo.WebApplicationLogic"));
        applicationLogic.add(new Pair<String, String>("com.spinytech.maindemo:music", "com.spinytech.musicdemo.MusicApplicationLogic"));
        applicationLogic.add(new Pair<String, String>("com.spinytech.maindemo:pic", "com.spinytech.picdemo.PicApplicationLogic"));

    }

    @Override
    public void onCreate() {
        super.onCreate();

        for (Pair<String, String> processLogic : applicationLogic) {
            try {
                Class<? extends BaseApplicationLogic> applicationLogicClass = (Class<? extends BaseApplicationLogic>) Class.forName(processLogic.second);
                RouterManager.getInstance().registerApplicationLogic(applicationLogicClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        RouterManager.getInstance().init(this);
    }

}
