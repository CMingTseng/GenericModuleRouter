package com.spinytech.macore;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author wanglei
 * @date 2016/12/27
 */

public class RouterRequest {
    private static final String TAG = "RouterRequest";
    private String from;
    private String domain;
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

    public RouterRequest(String requestJsonString) {
        try {
            JSONObject jsonObject = new JSONObject(requestJsonString);
            from = jsonObject.optString("from");
            domain = jsonObject.optString("domain");
            provider = jsonObject.optString("provider");
            action = jsonObject.optString("action");
            this.data = new HashMap<>();
            try {
                JSONObject jsonData = new JSONObject(jsonObject.optString("data"));
                Iterator it = jsonData.keys();
                while (it.hasNext()) {
                    String key = String.valueOf(it.next());
                    String value = (String) jsonData.get(key);
                    this.data.put(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 只是给缓冲池初始化使用，后续都是用带 Context
     */
    private RouterRequest() {
        this.from = ProcessUtil.DEFAULT_PROCESS;
        this.domain = ProcessUtil.DEFAULT_PROCESS;
        this.provider = "";
        this.action = "";
        this.data = new HashMap<>();
    }


    /**
     * 后续缓存 new 从这里
     * @param context
     */
    private RouterRequest(Context context) {
        this.from = ProcessUtil.getProcessName(context);
        this.domain = ProcessUtil.getProcessName(context);
        this.provider = "";
        this.action = "";
        this.data = new HashMap<>();
    }


    public String getDomain() {
        return domain;
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

    private static RouterRequest obtain(Context context, int retryTime) {
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
            target.from = ProcessUtil.getProcessName(context);
            target.domain = ProcessUtil.getProcessName(context);
            target.provider = "";
            target.action = "";
            target.data.clear();
            return target;
        } else {
            if (retryTime < 5) {
                return obtain(context, retryTime++);
            } else {
                return new RouterRequest(context);
            }

        }
    }

    public RouterRequest url(String url) {
        int questIndex = url.indexOf('?');
        String[] urls = url.split("\\?");
        if (urls.length != 1 && urls.length != 2) {
            Log.e(TAG, "The url is illegal.");
            return this;
        }
        String[] targets = urls[0].split("/");
        if (targets.length == 3) {
            this.domain = targets[0];
            this.provider = targets[1];
            this.action = targets[2];
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
            jsonObject.put("from", from);
            jsonObject.put("domain", domain);
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

    public RouterRequest domain(String domain) {
        this.domain = domain;
        return this;
    }

    public static RouterRequest obtain(Context context) {
        return obtain(context, 0);
    }
}
