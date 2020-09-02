package com.xiaojun.horizontalwheelview.view;

import android.content.Context;
import android.graphics.Color;

import com.xiaojun.horizontalwheelview.util.ScreenUtils;

/**
 * 垂直的刻度类
 * 由下(y=0)往上(y=height)画
 * created by xiaojun at 2020/8/31
 */
public class Scale {

    //刻度线宽
    public float mStrokeWidth;
    //刻度高度
    public float mStrokeHeight;
    //刻度当前坐标,mYstart等于0表示最下面,所以mYend=mXstart+mStrokeHeight;
    public int mStartX,mStartY,mEndX,mEndY;
    //刻度当前的X轴偏移距离
    public float mOffsetX;
    //刻度颜色
    public int mStrokeColor;
    //刻度不透明度 0 ~ 1,越大越不透明
    public float mAlpha;
    //刻度类型:大刻度和小刻度
    public TYPE mType;

    public Scale(Context context){
        mStrokeWidth = ScreenUtils.dp2px(context,1);
        mStrokeHeight = ScreenUtils.dp2px(context,16);
        mStrokeColor = Color.WHITE;
        mAlpha = 1;
        mType = TYPE.SMALL;
    }

    public Scale(float strokeWidth,float strokeHeight,float offsetX,int strokeColor,float alpha,TYPE type){
        mStrokeWidth = strokeWidth;
        mStrokeHeight = strokeHeight;
        mOffsetX = offsetX;
        mStrokeColor = strokeColor;
        mAlpha = alpha;
        mType = type;
        calculateLocation();
    }

    /**
     * 滑动
     * @param offsetX 滑动刻度从左到右为正,从右到左为负
     */
    public void scroll(float offsetX){
        mOffsetX = offsetX;
        calculateLocation();
    }

    /**
     * 计算当前刻度的坐标
     */
    private void calculateLocation(){
        mStartX += mOffsetX;
        mEndX = mStartX;
        mStartY = 0;
        mEndY = (int) mStrokeHeight;
    }

    public enum TYPE{
        BIG,
        SMALL
    }

}
