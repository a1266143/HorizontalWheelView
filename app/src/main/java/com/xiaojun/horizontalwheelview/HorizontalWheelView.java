package com.xiaojun.horizontalwheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * 横向滚动View
 * //目前已知的数据方式有两种:
 * //1.连续型: 0~100
 * //2.离散型:4.7 4.9 8.0 (统一封装成List<String>)
 * created by xiaojun at 2020/8/28
 */
public class HorizontalWheelView extends View {

    private Paint mPaint;
    private int mWidth, mHeight;
    //滑动偏移距离
    private float mOffsetScroll;
    //固定的偏移距离,因为需要显示到中间
    private float mOffsetFix;
    //当前Position
    private int mPosition;
    //相邻两个index间的距离
    private float mDistanceTwoIndex;
    //离散型数据
    private List<String> mListDiscrete;
    //连续型数据:采用min和max
    private int mMin, mMax;

    public HorizontalWheelView(Context context) {
        this(context, null);
    }

    public HorizontalWheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);

        //两个index之间的距离
        mDistanceTwoIndex = ScreenUtils.dp2px(getContext(),40);
        mMin = 0;
        mMax = 100;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.mOffsetFix = w/2.f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mListDiscrete == null)
            drawContinuous(canvas);
        else
            drawDiscrete(canvas);
    }



    /**
     * 绘制连续型数据,根据min和max
     *
     * @param canvas
     */
    private void drawContinuous(Canvas canvas) {

    }

    /**
     * 绘制离散型数据,根据mListDiscrete来绘制
     * @param canvas
     */
    private void drawDiscrete(Canvas canvas) {

    }
}
