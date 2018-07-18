package com.spinytech.macore;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static RouterManager getInstance() {
        return sInstance;
    }

    private RouterManager(){
    }

    public void init(Context context) {
        if( context == null){
            throw new RuntimeException("Router manager init with context null");
        }
        Log.d(TAG, "Application onCreate start: " + System.currentTimeMillis());
        LocalRouter.init(context);
        appLogicInit(context);
        Log.d(TAG, "Application onCreate end: " + System.currentTimeMillis());
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

    public void registerApplicationLogic(String processName, Class<? extends BaseApplicationLogic> logicClass) {
        if (null != mLogicClassMap) {
            ArrayList<ApplicationLogicWrapper> tempList = mLogicClassMap.get(processName);
            if (null == tempList) {
                tempList = new ArrayList<>();
                mLogicClassMap.put(processName, tempList);
            }
            if (tempList.size() > 0) {
                for (ApplicationLogicWrapper baseApplicationLogic : tempList) {
                    if (logicClass.getName().equals(baseApplicationLogic.logicClass.getName())) {
                        throw new RuntimeException(logicClass.getName() + " has registered.");
                    }
                }
            }
            tempList.add(new ApplicationLogicWrapper(logicClass));
        }
    }

    public void route(Context context, RouterRequest routerRequest, RouterCallback callback) {
        LocalRouter.getInstance().route(context, routerRequest, callback);
    }

    public void registerProvider(String providerName, RouterProvider provider) {
        LocalRouter.getInstance().registerProvider(providerName, provider);
    }
}
