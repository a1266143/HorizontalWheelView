package com.xiaojun.horizontalwheelview.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.xiaojun.horizontalwheelview.SCROLLTYPE;
import com.xiaojun.horizontalwheelview.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 刻度管理器
 * created by xiaojun at 2020/9/1
 */
public class ScalesManager {

    private int mCanvasWidth, mCanvasHeight;
    private float mTotalScaleWidth;
    private float mScalesFixDistance;
    private int mNumOfSmallScale;//两个大刻度之间的小刻度数量
    private int mNumOfBigScale;//大刻度的总数量
    private Context mContext;
    private List<Scale> mScales = new ArrayList<>();

    public ScalesManager(Context context, int canvasWidth, int canvasHeight) {
        this.mContext = context;
        this.mCanvasWidth = canvasWidth;
        this.mCanvasHeight = canvasHeight;
    }

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
            //新建大刻度
            Scale bigScale = new Scale(strokeWidth, strokeHeightBigScale, totalOffsetX, Color.WHITE, 1, Scale.TYPE.BIG);
            mScales.add(bigScale);
            //这里注意，因为最后一个刻度还会增加一份offsetFix，所以这里需要去除掉最后一个
            if (i < sizeofDatasSpecial)
                totalOffsetX += offsetXfix;
            //最后一个大刻度不需要小刻度
            if (i < sizeofDatasSpecial) {
                //新建小刻度
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

    private float mStartX;

    /**
     * 根据当前数据类型，纠正距离
     *
     * @param type           数据类型
     * @param currentScrollX 当前的偏移距离
     * @param startX         需要滑动到的起始点(现在目前是ViewWidth/2)
     */
    public float correctOffsetXByType(DataType type, float currentScrollX, float startX) {
        mStartX = startX;
        if (type == DataType.DISCRETE) {//离散型数据
            return correctDiscrete(currentScrollX);
        } else if (type == DataType.CONTINUED) {//连续型数据
            return correctContinued();
        }
        return 0;
    }

    /**
     * TODO 纠正连续型数据对应的偏移量
     */
    private float correctContinued() {
        return 0;
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
            return mTotalScaleWidth - startOffset;//滑动剩下距离就可以了
        }
        //如果滑动到了最左边
        else if (startOffset <= 0) {
            return Math.abs(startOffset);
        }
        //剩余的正常部分
        else {
            if (scrollX > 0||scrollX<0) {
                if (index % ((int) index) >= 0.5f) {
                    index += 1;
                }
                float bigScaleWidth = (int) index * bigScalesDistance;
                return bigScaleWidth - startOffset;
            }
            return 0;
        }
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
     * 获取刻度间的固定距离
     *
     * @return
     */
    public float getScalesFixDistance() {
        return mScalesFixDistance;
    }

    /**
     * 获取所有刻度的总长度
     *
     * @return
     */
    public float getTotalScaleWidth() {
        return mTotalScaleWidth;
    }

}
