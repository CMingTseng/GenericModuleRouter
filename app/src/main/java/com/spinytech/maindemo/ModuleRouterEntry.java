package com.spinytech.maindemo;

import android.util.Pair;

import java.util.LinkedList;

/**
 * Created by guofeng05 on 2018/7/18.
 */

public class ModuleRouterEntry {
    public static final String MAIN_DOMAIN = "com.spinytech.maindemo";
    public static final String MAIN_DOMAIN_MAIN_APP = "com.spinytech.maindemo.MainApplicationLogic";


    public static final String MUSIC_DOMAIN = "com.spinytech.maindemo:music";
    public static final String MUSIC_DOMAIN_MUSIC_APP = "com.spinytech.musicdemo.MusicApplicationLogic";


    public static final String PIC_DOMAIN = "com.spinytech.maindemo:pic";
    public static final String PIC_DOMAIN_PIC_APP = "com.spinytech.picdemo.PicApplicationLogic";

    public static final String WEB_DOMAIN = "com.spinytech.maindemo:web";
    public static final String WEB_DOMAIN_WEB_APP = "com.spinytech.webdemo.WebApplicationLogic";


    public static LinkedList<Pair<String, String>> applicationLogic;

    static {
        applicationLogic = new LinkedList<>();
        applicationLogic.add(new Pair<>(MAIN_DOMAIN, MAIN_DOMAIN_MAIN_APP));
        applicationLogic.add(new Pair<>(MUSIC_DOMAIN, MUSIC_DOMAIN_MUSIC_APP));
        applicationLogic.add(new Pair<>(PIC_DOMAIN, PIC_DOMAIN_PIC_APP));
        applicationLogic.add(new Pair<>(WEB_DOMAIN, WEB_DOMAIN_WEB_APP));

    }


    /**
     * 1.由于夸进程访问是通过 ContentProvider，其 onCreate,call 方法在
     * application.attachBaseContext 和 onCreate 之间
     * 而夸进程调用之前必须初始化 Router
     * 2.每个进程 ContentProvider 会自动在应用启动的时候 onCreate，所以在这里初始化 Route ，不用集成方去调用
     * <p>
     * <p>
     * 这里只需要提供列表即可
     *
     * @param base
     */

    public static LinkedList<Pair<String, String>> provideAllModule() {
        return applicationLogic;
    }

}
