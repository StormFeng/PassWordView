package com.nof.pwv;

import android.content.res.Resources;

/**
 * Created by Administrator on 2017/11/17.
 */

public class Util {
    public static int dp2px(int dp){
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }

    public static int px2dp(int px){
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }
}
