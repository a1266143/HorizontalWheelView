package com.xiaojun.horizontalwheelview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;

import com.xiaojun.bulter.yyUtils.YyScreenUtils;
import com.xiaojun.horizontalwheelview.SCROLLTYPE;
import com.xiaojun.horizontalwheelview.util.ScreenUtils;
import com.xiaojun.horizontalwheelview.util.VibratorUtils;

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
    private ScalesDiscreteManager mScalesManager = new ScalesDiscreteManager(getContext());
    private int mWidth, mHeight;
    private int mOffsetXFix;//固定偏移距离
    private SCROLLTYPE mType;
    //-----------------------------------------------------------------------------------

    //---------------------------------------球--------------------------------------
    private Ball mBallCenter = new Ball();
    private BallManager mBallManager;
    private Paint mPaintBall = new Paint(Paint.ANTI_ALIAS_FLAG);
    private CenterLine mCenterLine = new CenterLine();


    private GestureDetectorCompat mGestureDetector;
    private Scroller mScroller;
    private OnProgressChangeListener mListener;
    private VibratorUtils mVibratorUtils;


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

        mVibratorUtils = new VibratorUtils(getContext());
    }

    class MySimpleOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return HorizontalWheelView.this.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (mScalesManager == null)
                return;
            setCenterLine(Color.WHITE);
            if (mScalesManager.getCenterScale().mStartX == (getScrollX() + mOffsetXFix))
                mBallManager.dismissBall();
            mVibratorUtils.vibrate();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int defaultSizeWidth = getMeasuredWidth();
        mWidth = getProperSize(defaultSizeWidth, widthMeasureSpec, true);
        defaultSizeWidth = getMeasuredHeight();
        mHeight = getProperSize(defaultSizeWidth, heightMeasureSpec, false);
        setMeasuredDimension(mWidth, mHeight);
        //固定偏移距离
        this.mOffsetXFix = mWidth / 2;
        mScalesManager.setFixOffsetX(mOffsetXFix);
        Log.e("xiaojun","HorizontalWheelView:onMeasure");
        if (mScalesManager.getInitPosition() != mScalesManager.getFinalStopIndex() && mScalesManager.getInitPosition() != -1) {
            scrollTo(-mOffsetXFix,0);
            int dx = (int) mScalesManager.getDxFromPosition(getScrollX(), mScalesManager.getInitPosition());
            scrollTo(dx - mOffsetXFix, 0);
            correctPosition();
            setCenterLine(Color.WHITE);
            mScalesManager.setInitPosition(-1);
        }
    }

    private int getProperSize(int defaultSize, int measureSpec, boolean isWidth) {
        int properSize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                properSize = size;
                break;
            case MeasureSpec.AT_MOST:
                if (!isWidth)
                    properSize = YyScreenUtils.dp2px(getContext(), 64);
//                else
//                    properSize = Math.min(size,YyScreenUtils.dp2px(getContext(),72));
                break;
        }
        return properSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            setCenterLine(Color.WHITE);
            boolean isFinish = mScroller.isFinished();
            if (isFinish) //只scroll但是没有Fling，这时候需要纠正距离
                correctPosition();
        }
        return result;
    }

    private void correctPosition() {
        float dx = mScalesManager.correctOffsetX(getScrollX(), mOffsetXFix);
        scrollToCorrespondingPosition(dx);
    }

    private void scrollToCorrespondingPosition(float dx) {
        if (dx != 0) {
            mType = SCROLLTYPE.PROGRAM;
            mScroller.startScroll(getScrollX(), 0, (int) dx, 0, 150);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("xiaojun","HorizontalWheelView:onFinishInflate");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("xiaojun","HorizontalWheelView:onAttachedToWindow");
    }

    //是否已经初始化过
    private boolean mSetDataAlready;

    /**
     * 外部设置数据
     *
     * @param size
     * @param initIndex 初始滑动到的index
     * @param dataType  数据类型
     */
    public void setDatas(int size, int initIndex, TYPE dataType) {
        Log.e("xiaojun","HorizontalWheelView:setDatas");
        if (size <= 0)
            return;
        if (initIndex < 0 || initIndex >= size)
            return;
        if (mScroller != null && !mScroller.isFinished())
            mScroller.forceFinished(true);
        mScalesManager.setDatas(size, initIndex, dataType);
        initBall();
        if (mSetDataAlready && mOffsetXFix != 0) {
            scrollTo(-mOffsetXFix, 0);
            int dx = (int) mScalesManager.getDxFromPosition(getScrollX(), mScalesManager.getInitPosition());
            scrollTo(dx - mOffsetXFix, 0);
            correctPosition();
            setCenterLine(Color.WHITE);
            ViewCompat.postInvalidateOnAnimation(this);
        }
        mSetDataAlready = true;
    }

    private void setCenterLine(int color) {
        mCenterLine.startX = (int) (getScrollX() + mWidth / 2.f);
        mCenterLine.startY = 0;
        mCenterLine.endX = mCenterLine.startX;
        mCenterLine.endY = (int) (mHeight - mUpMove);
        mCenterLine.color = color;
        mCenterLine.alpha = 1;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void initBall() {
        Scale centerScale = mScalesManager.getCenterScale();
        if (centerScale == null)
            return;
        mBallCenter.x = centerScale.mStartX;
        mBallCenter.y = ScreenUtils.dp2px(getContext(), 3) * 4;
        mBallCenter.radius = ScreenUtils.dp2px(getContext(), 3);
        mBallCenter.alpha = 1;
        mBallCenter.color = Color.WHITE;
        if (mBallManager == null)
            mBallManager = new BallManager(this, mBallCenter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("xiaojun","HorizontalWheelView:onDraw");
        if (mScalesManager == null)
            return;
        drawScales(canvas);
        drawCenterLine(canvas);
        drawCenterBall(canvas);
    }

    private void drawCenterLine(Canvas canvas) {
        mPaintScale.setColor(mCenterLine.color);
        mPaintScale.setAlpha((int) (mCenterLine.alpha * 255));
        canvas.drawLine(mCenterLine.startX, mCenterLine.startY, mCenterLine.endX, mCenterLine.endY, mPaintScale);
    }

    private void drawCenterBall(Canvas canvas) {
        mPaintBall.setColor(mBallCenter.color);
        mPaintBall.setAlpha((int) (mBallCenter.alpha * 255));
        canvas.drawCircle(mBallCenter.x, mBallCenter.y, mBallCenter.radius, mPaintBall);
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            vibrate(false);
            setCenterLine(Color.WHITE);
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            if (mType == SCROLLTYPE.FLING) {
                mType = SCROLLTYPE.PROGRAM;
                correctPosition();
            } else {
                if (mType == SCROLLTYPE.PROGRAM) {//startScroll结束
                    mType = SCROLLTYPE.NONE;
                    setCenterLine(Color.WHITE);
                    //滑动到中间需要使球消失
                    if (mScalesManager.getCenterScale().mStartX == (getScrollX() + mOffsetXFix))
                        mBallManager.dismissBall();
                    if (mListener != null) {
                        mListener.onProgressSelected(mScalesManager.getFinalStopIndex());
                    }

                }
            }
        }
    }

    /**
     * 震动并progressChange
     *
     * @param fromUser
     */
    private void vibrate(boolean fromUser) {
        if (mScalesManager.isThroughPosition(getScrollX())) {
            mVibratorUtils.vibrate();
            if (mListener != null)
                mListener.onProgressChange(mScalesManager.getIndex(), fromUser);
        }
    }

    /**
     * 滑动到某个固定的Position
     *
     * @param position
     */
    public void scrollToPosition(int position) {
        mType = SCROLLTYPE.PROGRAM;
        mScroller.forceFinished(true);
        mBallManager.showBall();
        mScroller.startScroll(getScrollX(), 0, (int) mScalesManager.getDxFromPosition(getScrollX(), position), 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 设置进度改变监听器
     *
     * @param listener
     */
    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        this.mListener = listener;
    }

    private float mUpMove = ScreenUtils.dp2px(getContext(), 16);

    /**
     * 绘制所有刻度
     *
     * @param canvas
     */
    private void drawScales(Canvas canvas) {
        List<Scale> list = mScalesManager.getScales();
        for (Scale scale : list) {
            mPaintScale.setColor(scale.mStrokeColor);
            mPaintScale.setAlpha((int) (scale.mAlpha * 255));
            mPaintScale.setStrokeWidth(scale.mStrokeWidth);
            canvas.drawLine(scale.mStartX, mHeight - scale.mStartY - mUpMove, scale.mEndX, mHeight - scale.mEndY - mUpMove, mPaintScale);
        }
    }

    private boolean onDown(MotionEvent e) {
        if (mScalesManager == null)
            return false;
        mScroller.forceFinished(true);
        ViewCompat.postInvalidateOnAnimation(this);
        setCenterLine(Color.parseColor("#FFAB00"));
        return true;
    }

    private boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mScalesManager == null)
            return false;
        mType = SCROLLTYPE.SCROLL;
        if (getScrollX() > mScalesManager.getTotalScaleWidth() - mOffsetXFix)
            distanceX *= 0.4f;
        else if (getScrollX() < -mOffsetXFix)
            distanceX *= 0.4f;
        scrollBy((int) distanceX, 0);
        //判断当前的scrollX是否在BigScale线段内
        vibrate(true);
        setCenterLine(Color.parseColor("#FFAB00"));
        if (mBallManager != null)
            mBallManager.showBall();
        return true;
    }

    private boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mScalesManager == null)
            return false;
        //过滤掉这些fling
        if (velocityX > -500 && velocityX < 500)
            return false;
        mScroller.forceFinished(true);
        if (flingCondition(velocityX)) {
            mType = SCROLLTYPE.FLING;
            velocityX *= 0.9f;//将速度减小到一半
            mScroller.fling(getScrollX(), 0, -(int) velocityX, 0, -mWidth / 2 - mWidth / 9, ((int) (mScalesManager.getTotalScaleWidth() - mWidth / 2)) + mWidth / 9, 0, 0);
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
