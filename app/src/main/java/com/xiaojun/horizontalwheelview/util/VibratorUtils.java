package com.xiaojun.horizontalwheelview.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * 震动工具
 * created by xiaojun at 2020/9/4
 */
public class VibratorUtils {

    private Vibrator mVibrator;
    private boolean mHasVibrator;
    private VibrationEffect mVibrationEffect;

    public VibratorUtils(Context context) {
        this.mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mHasVibrator = mVibrator.hasVibrator();
        if (mHasVibrator)
            mVibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
    }

    /**
     * 震动
     */
    public void vibrate() {
        if (mVibrator != null && mHasVibrator) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mVibrator.vibrate(mVibrationEffect);
            } else {
                mVibrator.vibrate(10);
            }
        }
    }

}
