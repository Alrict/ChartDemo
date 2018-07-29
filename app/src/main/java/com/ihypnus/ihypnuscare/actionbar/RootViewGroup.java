package com.ihypnus.ihypnuscare.actionbar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.VelocityTracker;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * @version V1.0
 * @createAuthor yzw
 * @createDate 2017/1/9 17:49
 * @updateAuthor
 * @updateDate
 * @company 跨越速运
 * @description 顶层可滑动的View
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class RootViewGroup extends com.zhy.autolayout.AutoFrameLayout {

    private Context mContext;

    private static String TAG = "RootViewGroup";

    private Scroller mScroller = null;

    public static int screenWidth; // 屏幕宽度

    public static int scrrenHeight; // 屏幕高度

    /**
     * 判断滑动结束后activity是否finish
     */
    private boolean isFinish = false;

    /**
     * 手指是否up
     */
    private boolean isActionUp = false;

    public RootViewGroup(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RootViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RootViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    public void computeScroll() {
        // 如果返回true，表示动画还没有结束
        // 因为前面startScroll，所以只有在startScroll完成时 才会为false
        if (mScroller.computeScrollOffset() && isActionUp) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (mListener != null) {
                mListener.moving(isFinish, x, y);
            }
            // 产生了动画效果 每次滚动一点
            scrollTo(x, y);
            // 刷新View 否则效果可能有误差
            postInvalidate();
        } else {
            if (mListener != null && isActionUp) {
                isActionUp = false;
                if (isFinish) {
//                     mListener.finish();
                } else {
                    mListener.reBack();
                }
            }
        }
    }

    // ///以上可以演示Scroller类的使用
    // // --------------------------------
    // ///--------------------------------

    // //我们是缓慢移动的
    public void scrollToEnd(VelocityTracker velocityTracker, boolean finish,
                            int duration) {
        isActionUp = true;
        // 当前的偏移位置
        int scrollX = getScrollX();
        // 判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
        // 直接使用这个公式判断是哪一个屏幕 前后或者自己
        // 判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
        // 这样的一个简单公式意思是：假设当前滑屏偏移值即 scrollCurX 加上每个屏幕一半的宽度，除以每个屏幕的宽度就是
        // 我们目标屏所在位置了。 假如每个屏幕宽度为320dip, 我们滑到了500dip处，很显然我们应该到达第二屏

        int velocityX = 0;
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(1000);
            velocityX = (int) velocityTracker.getXVelocity();
        }

        int distance = 0;

        if (velocityX > 600 || Math.abs(scrollX) > screenWidth / 2 || finish) {
            // Fling enough to move left
            distance = -(ActFinishHandler.SHADOW_WIDTH + screenWidth + scrollX);
            isFinish = true;
        } else {
            distance = -scrollX;
            isFinish = false;
        }
        mScroller.startScroll(scrollX, 0, distance, 0, duration);
        // 此时需要手动刷新View 否则没效果
        invalidate();
    }

    public void scrollToStart(int duration) {
        isActionUp = true;
        isFinish = false;
//        mScroller.startScroll(-screenWidth, 0, screenWidth, 0, duration);
        // 此时需要手动刷新View 否则没效果
//        invalidate();
    }

    private void init() {
        mScroller = new Scroller(mContext, new DecelerateInterpolator(2.0f));
        // 获得屏幕分辨率大小
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        screenWidth = metric.widthPixels;
        scrrenHeight = metric.heightPixels;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    // private int getScrollVelocity(VelocityTracker velocityTracker)
    // {
    // final int change = 1000;
    // velocityTracker.computeCurrentVelocity(change);
    // int velocity = (int) velocityTracker.getXVelocity();
    // return velocity;
    // }

    public interface RootViewGroupAnimationListener {
        // void finish();

        void moving(boolean isFinish, int X, int Y);

        void reBack();
    }

    private RootViewGroupAnimationListener mListener;

    public void setRootViewGroupAnimationListener(
            RootViewGroupAnimationListener listener) {
        this.mListener = listener;
    }
}

