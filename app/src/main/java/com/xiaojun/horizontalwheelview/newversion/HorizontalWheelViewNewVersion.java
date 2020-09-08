package com.xiaojun.horizontalwheelview.newversion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;

/**
 * 新版本HorizontalWheelView
 * created by xiaojun at 2020/9/8
 */
public class HorizontalWheelViewNewVersion extends View {

    private Paint mPaintScales = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Scroller mScroller = new Scroller(getContext());
    private GestureDetectorCompat mGestureDetector = new GestureDetectorCompat(getContext(),new MyGestureListener());
    private int mWidth,mHeight;

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            HorizontalWheelViewNewVersion.this.onLongPress();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return HorizontalWheelViewNewVersion.this.onScroll(distanceX);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontalWheelViewNewVersion.this.onFling(velocityX);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return HorizontalWheelViewNewVersion.this.onDown();
        }
    }

    public HorizontalWheelViewNewVersion(Context context) {
        this(context, null);
    }

    public HorizontalWheelViewNewVersion(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalWheelViewNewVersion(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        mPaintScales.setStyle(Paint.Style.STROKE);
        mPaintScales.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private boolean onDown(){
        mScroller.forceFinished(true);
        return true;
    }

    private void onLongPress(){
        Log.e("xiaojun","onLongPress");
    }

    private boolean onScroll(float distanceX){
        scrollBy((int) distanceX,0);
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    private boolean onFling(float velocityX){
//        mScroller.startScroll();
        return true;
    }

}
