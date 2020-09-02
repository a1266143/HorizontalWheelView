package com.xiaojun.horizontalwheelview;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.xiaojun.horizontalwheelview.util.ScreenUtils;

/**
 * 风险View
 * created by xiaojun at 2020/8/31
 */
public class RiskView extends View {

    private int mWidth, mHeight;
    private Ball mBall;
    private Line mLine1 = new Line();
    private Line mLine2 = new Line();

    private int[] mColors = new int[]{0xFFFF0000, 0xFFFFFF00, 0xFF008000};
    private float[] mPositions = new float[]{0, 0.5f, 1};

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintBall = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);

    public RiskView(Context context) {
        this(context, null);
    }

    public RiskView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RiskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBall = new Ball();
        mPaintBall.setColor(Color.WHITE);

        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(ScreenUtils.dp2px(getContext(), 1));
    }

    private float mBallOffsetX;
    private ValueAnimator mAnimator;

    /**
     * 设置值
     *
     * @param value 0~1
     */
    public void setValue(float value) {
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }
        } else {
            mAnimator = new ValueAnimator();
            mAnimator.setDuration(250);
            mAnimator.setInterpolator(new DecelerateInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mBallOffsetX = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
        }
        int widthBar = mWidth - mHeight;
        mAnimator.setFloatValues(mBallOffsetX, widthBar * value);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mAnimator.start();
            }
        });
    }

    private Handler mHandler = new Handler();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mPaint.setShader(new LinearGradient(0, mHeight / 2.f, mWidth, mHeight / 2.f, mColors, mPositions, Shader.TileMode.REPEAT));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mHeight);
        //初始化轨迹球
        mBall.x = 0;
        mBall.y = mHeight / 2.f;
        mBall.radius = mHeight / 5.f * 2;
        //初始化线段
        mLine1.startX = (mWidth - mHeight) / 3.f + mHeight / 2.f;
        mLine1.startY = 0;
        mLine1.endX = (mWidth - mHeight) / 3.f + mHeight / 2.f;
        mLine1.endY = mHeight;
        mLine1.color = Color.WHITE;

        mLine2.startX = (mWidth - mHeight) / 3.f * 2 + mHeight / 2.f;
        mLine2.startY = 0;
        mLine2.endX = (mWidth - mHeight) / 3.f * 2 + mHeight / 2.f;
        mLine2.endY = mHeight;
        mLine2.color = Color.WHITE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(mHeight / 2.f, mHeight / 2.f, mWidth - mHeight / 2.f, mHeight / 2.f, mPaint);
        drawLine(canvas);
        drawBall(canvas);
    }

    private void drawLine(Canvas canvas) {
        mPaintLine.setColor(mLine1.color);
        canvas.drawLine(mLine1.startX, mLine1.startY, mLine1.endX, mLine1.endY, mPaintLine);
        mPaintLine.setColor(mLine2.color);
        canvas.drawLine(mLine2.startX, mLine2.startY, mLine2.endX, mLine2.endY, mPaintLine);
    }

    private void drawBall(Canvas canvas) {
        canvas.drawCircle(mHeight / 2.f + mBallOffsetX + mBall.x, mBall.y, mBall.radius, mPaintBall);
    }

    /**
     * 线段
     */
    public static class Line {
        public float startX, startY, endX, endY;
        public int color;
    }

    /**
     * 轨迹球
     */
    public static class Ball {
        public float x;
        public float y;
        public float radius;
    }

}
