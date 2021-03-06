package com.spinytech.macore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author wanglei
 * @date 2016/11/25
 */

public class RouterManager {
    private static final String TAG = "RouterManager";
    private static RouterManager sInstance = new RouterManager();
    private ArrayList<ApplicationLogicWrapper> mLogicList;
    private HashMap<String, ArrayList<ApplicationLogicWrapper>> mLogicClassMap = new HashMap<>();

    private RouterManager() {
    }

    public static RouterManager getInstance() {
        return sInstance;
    }

    public void init(Context context, List<Pair<String, String>> applicationLogic) {
        if( context == null){
            throw new RuntimeException("Router manager init with context null");
        }
        Log.d(TAG, "Application onCreate start: " + System.currentTimeMillis());
        // 1. init local router
        LocalRouter.init(context);
        // 2. register applogic
        for (Pair<String, String> processLogic : applicationLogic) {
            try {
                Class<? extends BaseApplicationLogic> applicationLogicClass = (Class<? extends BaseApplicationLogic>) Class.forName(processLogic.second);
                registerApplicationLogic(processLogic.first, applicationLogicClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        // 3. instant applogic to register provider to current process local router
        appLogicInit(context);
        Log.d(TAG, "Application onCreate end: " + System.currentTimeMillis());
    }

    public void registerApplicationLogic(String processName, Class<? extends BaseApplicationLogic> logicClass) {
        if (null != mLogicClassMap) {
            ArrayList<ApplicationLogicWrapper> tempList = mLogicClassMap.get(processName);
            if (null == tempList) {
                tempList = new ArrayList<>();
                mLogicClassMap.put(processName, tempList);
            }
            for (ApplicationLogicWrapper baseApplicationLogic : tempList) {
                if (logicClass.getName().equals(baseApplicationLogic.logicClass.getName())) {
                    // aleady added ,so skip to continue
                    return;
                }

            }
            tempList.add(new ApplicationLogicWrapper(logicClass));
        }
    }

    private void appLogicInit(Context context) {
        if (null != mLogicClassMap) {
            mLogicList = mLogicClassMap.get(ProcessUtil.getProcessName(context));
        }
        if( mLogicList == null || mLogicList.size() < 1){
            return ;
        }
        for (ApplicationLogicWrapper priorityLogicWrapper : mLogicList) {
            // construct
            if (null != priorityLogicWrapper) {
                try {
                    priorityLogicWrapper.instance = priorityLogicWrapper.logicClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (null != priorityLogicWrapper && null != priorityLogicWrapper.instance) {
                    priorityLogicWrapper.instance.onCreate(context);
                    priorityLogicWrapper.instance.setApplication(context.getApplicationContext());
                }
            }
        }
    }

    public Bundle route(Context context, RouterRequest routerRequest, RouterCallback callback) {
        return LocalRouter.getInstance().route(context, routerRequest, callback);
    }

    public void registerProvider(String providerName, RouterProvider provider) {
        LocalRouter.getInstance().registerProvider(providerName, provider);
    }
}
