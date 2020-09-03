package com.xiaojun.horizontalwheelview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

import androidx.core.view.ViewCompat;

/**
 * 球管理器
 * created by xiaojun at 2020/9/3
 */
public class BallManager {

    private ValueAnimator mDismissAnimator;
    private ValueAnimator mShowAnimator;

    private View mView;
    private Ball mBall;

    public BallManager(View view,Ball ball){
        this.mView = view;
        this.mBall = ball;
    }

    /**
     * 动态使球消失
     */
    public void dismissBall(){
        if (mShowAnimator!=null&&mShowAnimator.isRunning()){
            mShowAnimator.cancel();
        }
        if (mDismissAnimator == null){
            mDismissAnimator = ValueAnimator.ofFloat(mBall.alpha,0);
            mDismissAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mBall.alpha = (float) animation.getAnimatedValue();
                    ViewCompat.postInvalidateOnAnimation(mView);
                }
            });
            mDismissAnimator.start();
        }else{
            if (!mDismissAnimator.isRunning()&&mBall.alpha!=0){
                mDismissAnimator.start();
            }
        }
    }

    /**
     * 动态显示球
     */
    public void showBall(){
        if (mDismissAnimator!=null&&mDismissAnimator.isRunning()){
            mDismissAnimator.cancel();
        }
        if (mShowAnimator == null){
            mShowAnimator = ValueAnimator.ofFloat(mBall.alpha,1);
            mShowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mBall.alpha = (float) animation.getAnimatedValue();
                    ViewCompat.postInvalidateOnAnimation(mView);
                }
            });
            mShowAnimator.start();
        }else{
            if (!mShowAnimator.isRunning()&&mBall.alpha!=1){
                mShowAnimator.setFloatValues(mBall.alpha,1);
                mShowAnimator.start();
            }
        }
    }

}
