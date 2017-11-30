package com.spinytech.macore;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Created by wanglei on 2016/12/27.
 */

public class RouterRequest {
    private static final String TAG = "RouterRequest";

    private String provider;
    private String action;
    private HashMap data;


    AtomicBoolean isIdle = new AtomicBoolean(true);

    private static final int length = 64;
    private static AtomicInteger sIndex = new AtomicInteger(0);
    private static final int RESET_NUM = 1000;
    private static volatile RouterRequest[] table = new RouterRequest[length];

    static {
        for (int i = 0; i < length; i++) {
            table[i] = new RouterRequest();
        }
    }


    public RouterRequest() {
        this.provider = "";
        this.action = "";
        this.data = new HashMap<>();
    }

    public String getProvider() {
        return provider;
    }

    public String getAction() {
        return action;
    }

    public HashMap getData() {
        return data;
    }

    public RouterRequest url(String url) {
        int questIndex = url.indexOf('?');
        String[] urls = url.split("\\?");
        if (urls.length != 1 && urls.length != 2) {
            Log.e(TAG, "The url is illegal.");
            return this;
        }
        String[] targets = urls[0].split("/");
        if (targets.length == 2) {
            //this.domain = targets[0];
            this.provider = targets[0];
            this.action = targets[1];
        } else {
            Log.e(TAG, "The url is illegal.");
            return this;
        }
        //Add params
        if (questIndex != -1) {
            String queryString = urls[1];
            if (queryString != null && queryString.length() > 0) {
                int ampersandIndex, lastAmpersandIndex = 0;
                String subStr, key, value;
                String[] paramPair, values, newValues;
                do {
                    ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
                    if (ampersandIndex > 0) {
                        subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
                        lastAmpersandIndex = ampersandIndex;
                    } else {
                        subStr = queryString.substring(lastAmpersandIndex);
                    }
                    paramPair = subStr.split("=");
                    key = paramPair[0];
                    value = paramPair.length == 1 ? "" : paramPair[1];
                    try {
                        value = URLDecoder.decode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    data.put(key, value);
                } while (ampersandIndex > 0);
            }
        }
        return this;
    }
    @Override
    public String toString() {
        //Here remove Gson to save about 10ms.
        //String result = new Gson().toJson(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("provider", provider);
            jsonObject.put("action", action);

            try {
                JSONObject jsonData = new JSONObject();
                Set keySet = data.keySet();
                for (Object key :  keySet) {
                    jsonData.put(key.toString(),data.get(key));
                }
                jsonObject.put("data", jsonData);
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("data", "{}");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public RouterRequest provider(String provider) {
        this.provider = provider;
        return this;
    }

    public RouterRequest action(String action) {
        this.action = action;
        return this;
    }

    public RouterRequest data(String key, Object data) {
        this.data.put(key, data);
        return this;
    }

    public static RouterRequest obtain() {
        return obtain(0);
    }

    private static RouterRequest obtain(int retryTime) {
        int index = sIndex.getAndIncrement();
        if (index > RESET_NUM) {
            sIndex.compareAndSet(index, 0);
            if (index > RESET_NUM * 2) {
                sIndex.set(0);
            }
        }

        int num = index & (length - 1);

        RouterRequest target = table[num];

        if (target.isIdle.compareAndSet(true, false)) {
            target.provider = "";
            target.action = "";
            target.data.clear();
            return target;
        } else {
            if (retryTime < 5) {
                return obtain(retryTime++);
            } else {
                return new RouterRequest();
            }

        }
    }
}
