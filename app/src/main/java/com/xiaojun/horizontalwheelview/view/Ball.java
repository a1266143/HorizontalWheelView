package com.xiaojun.horizontalwheelview.view;

import androidx.annotation.Nullable;

/**
 * 球
 * created by xiaojun at 2020/9/3
 */
public class Ball {

    public float x;//圆心x
    public float y;//圆心y
    public float radius;//半径
    public float alpha;//透明度
    public int color;//颜色

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Ball) {
            if (x == ((Ball) obj).x &&
                    y == ((Ball) obj).y &&
                    radius == ((Ball) obj).radius &&
                    alpha == ((Ball) obj).radius &&
                    color == ((Ball) obj).color)
                return true;
        }
        return false;
    }
}
