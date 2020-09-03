package com.xiaojun.horizontalwheelview.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.xiaojun.horizontalwheelview.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 离散型刻度管理器
 * created by xiaojun at 2020/9/1
 */
public class ScalesDiscreteManager {

    private int mCanvasWidth, mCanvasHeight;
    private float mTotalScaleWidth;
    private float mScalesFixDistance;//每两个刻度间的距离
    private int mNumOfSmallScale;//两个大刻度之间的小刻度数量
    private int mNumOfBigScale;//大刻度的总数量
    private Context mContext;
    private List<Scale> mScales = new ArrayList<>();
    private float mStartX;//固定的Scroll距离，现在是viewWidth/2
    private int mIndex;//纠正过后的index

    public ScalesDiscreteManager(Context context, int canvasWidth, int canvasHeight, float offsetFix) {
        this.mContext = context;
        this.mCanvasWidth = canvasWidth;
        this.mCanvasHeight = canvasHeight;
        this.mStartX = offsetFix;
    }

    //--------------------------------------------对外接口--------------------------------------------------

    /**
     * 设置离散型数据集
     *
     * @param datas
     */
    public void setDiscreteDatas(List<String> datas) {
        mScales.clear();
        init(datas);
    }

    /**
     * 根据当前数据类型，纠正距离
     *
     * @param currentScrollX 当前的偏移距离
     * @param startX         需要滑动到的起始点(现在目前是ViewWidth/2)
     */
    public float correctOffsetX(float currentScrollX, float startX) {
        mStartX = startX;
        return correctDiscrete(currentScrollX);
    }


    /**
     * 获取所有刻度列表
     *
     * @return
     */
    public List<Scale> getScales() {
        return mScales;
    }

    /**
     * 获取所有刻度的总长度
     *
     * @return
     */
    public float getTotalScaleWidth() {
        return mTotalScaleWidth;
    }

    /**
     * 去到对应离散刻度index的位移距离
     *
     * @param scrollX 当前X轴位移距离
     * @param index   索引
     * @return 返回对应滑动的距离，带有方向(正负号)
     */
    public float GotoIndex(float scrollX, int index) {
        return 0;
    }

    /**
     * 返回最终停下的index索引
     *
     * @return
     */
    public int getFinalStopIndex() {
        return mIndex;
    }

    /**
     * 获取中间的刻度
     *
     * @return
     */
    public Scale getCenterScale() {
        if (mNumOfBigScale <= 0)
            return null;
        if (mNumOfBigScale <= 2) {
            return mScales.get(0);
        } else {
            int realIndex = (mNumOfBigScale / 2) + mNumOfSmallScale * (mNumOfBigScale / 2);
            Log.e("xiaojun", "realIndex=" + realIndex + ",numOfBigScale=" + mNumOfBigScale + ",numOfSmallScale=" + mNumOfSmallScale);
            return mScales.get(realIndex);
        }
    }

    /**
     * 根据需要滑动到的Position返回偏移量(带方向(正负号))
     *
     * @param scrollX
     * @param position
     */
    public float getDxFromPosition(float scrollX, int position) {
        if (position < 0 || position > (mNumOfBigScale - 1)) {
            Log.e("xiaojun", "-------------------------------position超出范围-----------------------------");
            return 0;
        }
        this.mIndex = position;
        float currentScrollX = scrollX + mStartX;//当前阵阵的scrollx
        int realPosition = position + position * mNumOfSmallScale;//真正的position
        float correctScrollX = mScales.get(realPosition).mStartX;
        return correctScrollX - currentScrollX;
    }

    //----------------------------------------------------------------------------------------

    /**
     * 初始化所有的刻度坐标
     */
    private void init(List<String> datas) {
        float offsetXfix = ScreenUtils.dp2px(mContext, 12);//刻度与刻度之间固定的距离
        float totalOffsetX = 0;
        float strokeWidth = ScreenUtils.dp2px(mContext, 1);
        float strokeHeightBigScale = ScreenUtils.dp2px(mContext, 12);//大刻度的高度
        float strokeHeightSmallScale = ScreenUtils.dp2px(mContext, 12);//小刻度的高度
        int sizeofDatas = datas.size();
        int sizeofDatasSpecial = sizeofDatas - 1;
        int smallScaleNumber = 4;
        for (int i = 0; i < sizeofDatas; i++) {
            //大刻度
            Scale bigScale = new Scale(strokeWidth, strokeHeightBigScale, totalOffsetX, Color.WHITE, 1, Scale.TYPE.BIG);
            mScales.add(bigScale);
            //这里注意，因为最后一个刻度还会增加一份offsetFix，所以这里需要去除掉最后一个
            //最后一个大刻度不需要小刻度
            if (i < sizeofDatasSpecial) {
                totalOffsetX += offsetXfix;
                //小刻度
                for (int j = 0; j < smallScaleNumber; j++) {
                    Scale smallScale = new Scale(strokeWidth, strokeHeightSmallScale, totalOffsetX, Color.WHITE, 0.5f, Scale.TYPE.SMALL);
                    mScales.add(smallScale);
                    totalOffsetX += offsetXfix;
                }
            }
        }
        mTotalScaleWidth = totalOffsetX;
        mScalesFixDistance = offsetXfix;
        mNumOfSmallScale = smallScaleNumber;
        mNumOfBigScale = datas.size();
    }

    /**
     * 纠正离散型数据对应View的偏移量
     * 离散型数据,只考虑偏移到某个大刻度上
     *
     * @return
     */
    private float correctDiscrete(float scrollX) {
        float bigScalesDistance = (mNumOfSmallScale + 1) * mScalesFixDistance;//两个大刻度之间的距离
        float startOffset = scrollX + mStartX;
        float index = startOffset / bigScalesDistance;
        //如果已经滑动到最右边
        if (index > (mNumOfBigScale - 1)) {
            mIndex = mNumOfBigScale - 1;
            return mTotalScaleWidth - startOffset;//滑动剩下距离就可以了
        }
        //如果滑动到了最左边
        else if (startOffset <= 0) {
            mIndex = 0;
            return Math.abs(startOffset);
        }
        //剩余的正常部分
        else {
            if (scrollX > 0 || scrollX < 0) {
                if (index >= 1) {
                    if (index % ((int) index) >= 0.5f) {
                        index += 1;
                    }
                } else {
                    if (index >= 0.5f)
                        index += 1;
                }
                mIndex = (int) index;
                float bigScaleWidth = (int) index * bigScalesDistance;
                return bigScaleWidth - startOffset;
            }
            return 0;
        }
    }

    /**
     * TODO 纠正连续型数据对应的偏移量
     */
    private float correctContinued() {
        return 0;
    }

}
