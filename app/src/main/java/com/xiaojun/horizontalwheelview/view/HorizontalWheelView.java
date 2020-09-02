package com.xiaojun.horizontalwheelview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;

import com.xiaojun.horizontalwheelview.SCROLLTYPE;

import java.util.ArrayList;
import java.util.List;

/**
 * 横向滑动类尺子View
 * created by xiaojun at 2020/9/1
 */
public class HorizontalWheelView extends View {

    //--------------------------------------刻度相关--------------------------------
    //刻度画笔
    private Paint mPaintScale = new Paint(Paint.ANTI_ALIAS_FLAG);
    //刻度管理器
    private ScalesManager mScalesManager;
    private int mWidth, mHeight;
    private int mOffsetXFix;//固定偏移距离
    //-----------------------------------------------------------------------------------

    private GestureDetectorCompat mGestureDetector;
    private Scroller mScroller;


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
        mPaintScale.setStyle(Paint.Style.STROKE);
        mGestureDetector = new GestureDetectorCompat(getContext(), new MySimpleOnGestureListener());
        mScroller = new Scroller(getContext());
    }

    class MySimpleOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return HorizontalWheelView.this.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontalWheelView.this.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return HorizontalWheelView.this.onDown(e);
        }
    }

    private int mAction;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mAction = event.getAction();
        boolean result = mGestureDetector.onTouchEvent(event);
        if (mAction == MotionEvent.ACTION_UP) {
            boolean isFinish = mScroller.isFinished();
            if (isFinish) {//只scroll但是没有Fling，这时候需要纠正距离
                float dx = mScalesManager.correctOffsetXByType(DataType.DISCRETE, getScrollX(), mOffsetXFix);
                scrollToCorrespondingPosition(dx);
            }
        }
        return result;
    }

    private void scrollToCorrespondingPosition(float dx) {
        mType = SCROLLTYPE.PROGRAM;
        if (dx != 0) {
            mScroller.startScroll(getScrollX(), 0, (int) dx, 0, 300);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScalesManager = new ScalesManager(getContext(), w, h);
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("" + i);
        }
        mScalesManager.setDiscreteDatas(datas);
        this.mWidth = w;
        this.mHeight = h;
        mOffsetXFix = mWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScales(canvas);
        drawCenterLine(canvas);
    }

    private void drawCenterLine(Canvas canvas) {
        mPaintScale.setColor(Color.RED);
        mPaintScale.setAlpha(255);
        canvas.drawLine(getScrollX() + mWidth / 2.f, 0, getScrollX() + mWidth / 2.f, mHeight, mPaintScale);
    }

    private SCROLLTYPE mType;

    private boolean isCorrectionScroll;//是否在修正滑动

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.e("xiaojun", "computeScroll," + getClass().getSimpleName() + ",computeScroll,getScrollX=" + getScrollX());
            scrollTo(mScroller.getCurrX(), 0);
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            if (mType == SCROLLTYPE.FLING) {
                Log.e("xiaojun", "fling corresponding");
                float dx = mScalesManager.correctOffsetXByType(DataType.DISCRETE, getScrollX(), mOffsetXFix);
                scrollToCorrespondingPosition(dx);
            }
        }
    }

    /**
     * 绘制所有刻度
     *
     * @param canvas
     */
    private void drawScales(Canvas canvas) {
        List<Scale> list = mScalesManager.getScales();
        for (Scale scale : list) {
            mPaintScale.setAlpha((int) (scale.mAlpha * 255));
            mPaintScale.setColor(scale.mStrokeColor);
            mPaintScale.setStrokeWidth(scale.mStrokeWidth);
            canvas.drawLine(scale.mStartX, mHeight - scale.mStartY, scale.mEndX, mHeight - scale.mEndY, mPaintScale);
        }
    }

    private boolean onDown(MotionEvent e) {
        mScroller.forceFinished(true);
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }


    private boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mType = SCROLLTYPE.SCROLL;
        if (getScrollX() > mScalesManager.getTotalScaleWidth() - mOffsetXFix)
            distanceX *= 0.4f;
        else if (getScrollX() < -mOffsetXFix)
            distanceX *= 0.4f;
        scrollBy((int) distanceX, 0);
        Log.e("xiaojun", "e1.getAction()=" + e1.getAction() + ",e2.getAction()=" + e2.getAction() + ",mAction=" + mAction);
        return true;
    }

    private boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mScroller.forceFinished(true);
        if (flingCondition(velocityX)) {
            mType = SCROLLTYPE.FLING;
//            velocityX /= 2;//将速度减小到一半
            mScroller.fling(getScrollX(), 0, -(int) velocityX, 0, -mWidth / 2, ((int) (mScalesManager.getTotalScaleWidth() - mWidth / 2)), 0, 0);
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    /**
     * 是否满足fling条件
     *
     * @return
     */
    private boolean flingCondition(float velocityX) {
        //从右往左滑动
        if (velocityX < 0) {
            if (getScrollX() >= mScalesManager.getTotalScaleWidth() - mOffsetXFix)
                return false;
        }
        //从左往右滑动
        else if (getScrollX() <= -mOffsetXFix) {
            return false;
        }
        return true;
    }
}
