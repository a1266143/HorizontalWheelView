package com.xiaojun.horizontalwheelview.view;

/**
 * 进度被改变监听器
 * created by xiaojun at 2020/9/3
 */
public interface OnProgressChangeListener {

    /**
     *  进度值被改变
     * @param position
     * @param fromUser 是否来自用户调节
     */
    void onProgressChange(int position,boolean fromUser);

    /**
     * 最终的进度值
     * @param position 当前滑动到的index
     * @param lastPosition  上一次的index
     */
    void onProgressSelected(int position,int lastPosition);

    /**
     * 如果按住HorizontalWheelView不滑动一段时间后回调用户
     * @param position 当前选中的index
     */
    void onProgressDelayChange(int position);

}
