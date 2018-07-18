package com.spinytech.macore;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by guofeng05 on 2018/7/18.
 */

public class BundleUtil {
    public static void toJson(Bundle bundle){
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                json.put(key, JSONObject.wrap(bundle.get(key)));
            } catch(JSONException e) {
                //Handle exception here
            }
        }
    }

    public static HashMap toMap(Bundle bundle){
        HashMap result = new HashMap();
        Set<String> stringSet = bundle.keySet();
        for (String key : stringSet) {
            result.put(key,bundle.get(key));
        }
        return result;
    }

    public static Bundle fromMap(Map<String,Object> map){
        Bundle result = new Bundle();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            result.putString(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return  result;
    }


    public static Bundle fromJson(String jsonString){
        try {
            JSONObject jsonObject =new JSONObject(jsonString);
            Bundle bundle = new Bundle();
            Iterator iter = jsonObject.keys();
            while(iter.hasNext()){
                String key = (String)iter.next();
                String value = jsonObject.getString(key);
                bundle.putString(key,value);
            }
            return bundle;
        } catch (JSONException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }
}
