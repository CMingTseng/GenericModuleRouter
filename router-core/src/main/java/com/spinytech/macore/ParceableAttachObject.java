package com.spinytech.macore;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guofeng05 on 2018/7/17.
 * 用来附着进程内对象
 */

public class ParceableAttachObject implements Parcelable {
    public Object obj;

    public ParceableAttachObject(){

    }
    public ParceableAttachObject(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    // 反序列过程：必须实现Parcelable.Creator接口，并且对象名必须为CREATOR
    // 读取Parcel里面数据时必须按照成员变量声明的顺序，Parcel数据来源上面writeToParcel方法，读出来的数据供逻辑层使用
    public static final Parcelable.Creator<ParceableAttachObject> CREATOR = new Creator<ParceableAttachObject>() {

        @Override
        public ParceableAttachObject createFromParcel(Parcel source) {
            return new ParceableAttachObject(source);
        }

        @Override
        public ParceableAttachObject[] newArray(int size) {
            return new ParceableAttachObject[size];
        }
    };

}
