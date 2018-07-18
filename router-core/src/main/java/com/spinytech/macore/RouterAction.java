package com.spinytech.macore;

import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;

/**
 * Created by guofeng on 2016/11/29.
 */

public interface RouterAction {
    Bundle invoke(Context context, HashMap requestData, RouterCallback callback);
}
