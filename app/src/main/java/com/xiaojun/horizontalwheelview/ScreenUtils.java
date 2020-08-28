package com.xiaojun.horizontalwheelview;

import android.content.Context;

/**
 * 屏幕工具类
 * created by xiaojun at 2020/8/28
 */
public class ScreenUtils {

    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static float dp2px(Context context,float dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

}
