package com.xiaojun.horizontalwheelview.util;

import android.content.Context;

public class ScreenUtils {
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
