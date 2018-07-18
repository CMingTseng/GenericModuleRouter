package com.spinytech.maindemo;

import android.app.Application;
import android.content.Context;
import android.util.Pair;

import com.spinytech.macore.BaseApplicationLogic;
import com.spinytech.macore.RouterManager;

import java.util.LinkedList;

/**
 * Created by wanglei on 2016/11/29.
 */

public class MyApplication extends Application{

    public static final String MAIN_DOMAIN = "com.spinytech.maindemo";
    public static final String MAIN_DOMAIN_MAIN_APP = "com.spinytech.maindemo.MainApplicationLogic";



    public static final String MUSIC_DOMAIN = "com.spinytech.maindemo:music";
    public static final String MUSIC_DOMAIN_MUSIC_APP = "com.spinytech.musicdemo.MusicApplicationLogic";



    public static final String PIC_DOMAIN = "com.spinytech.maindemo:pic";
    public static final String PIC_DOMAIN_PIC_APP = "com.spinytech.picdemo.PicApplicationLogic";

    public static final String WEB_DOMAIN = "com.spinytech.maindemo:web";
    public static final String WEB_DOMAIN_WEB_APP = "com.spinytech.webdemo.WebApplicationLogic";


    public static LinkedList<Pair<String,String>> applicationLogic;

    static {
        applicationLogic = new LinkedList<>();
        applicationLogic.add(new Pair<>(MAIN_DOMAIN, MAIN_DOMAIN_MAIN_APP));
        applicationLogic.add(new Pair<>(MUSIC_DOMAIN, MUSIC_DOMAIN_MUSIC_APP));
        applicationLogic.add(new Pair<>(PIC_DOMAIN, PIC_DOMAIN_PIC_APP));
        applicationLogic.add(new Pair<>(WEB_DOMAIN, WEB_DOMAIN_WEB_APP));

    }

    /**
     *  由于夸进程访问是通过 ContentProvider，其 onCreate,call 方法在
     *  application.attachBaseContext 和 onCreate 之间
     *  而夸进程调用之前必须初始化 Router，所以 Router 必须放在 attachBaseContext 中
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        for (Pair<String, String> processLogic : applicationLogic) {
            try {
                Class<? extends BaseApplicationLogic> applicationLogicClass = (Class<? extends BaseApplicationLogic>) Class.forName(processLogic.second);
                RouterManager.getInstance().registerApplicationLogic(processLogic.first, applicationLogicClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        RouterManager.getInstance().init(this);
    }

}
