package com.ihypnus.ihypnuscare.actionbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import com.ihypnus.ihypnuscare.IhyApplication;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.BaseActivity;
import com.ihypnus.ihypnuscare.utils.AndroidSystemHelper;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

import java.lang.reflect.Field;

/**
 * @version V1.0
 * @createAuthor yzw
 * @createDate 2017/1/9 17:44
 * @updateAuthor
 * @updateDate
 * @company 跨越速运
 * @description 右滑关闭activity辅助类
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ActFinishHandler implements View.OnTouchListener, RootViewGroup.RootViewGroupAnimationListener {
    private static final int MIN_X_DISTANCE;

    private static final int MAX_Y_DISTANCE;

    private int MAX_LEFT_DISTANCE;

    private RootViewGroup mRootView;

    private Activity mActivity;

    // 记录手指按下时的横坐标。
    private float xDown;

    // 记录手指移动时的横坐标。
    private float xMove;

    private float yDown;

    private float xDistance;

    private float yDistance;

    // 用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    private int screenWidth;

    // private int screenHeight;

    // private ViewGroup mContentView;

    // private int MAX_TRANSLATE = 33;

    private RootViewGroup mLowerRootView;

    private View mShadowView;

    private static int LOWER_ROOTVIEW_MARGINRIGHT;

    // private List<View> childViewList = new ArrayList<View>();

    private float mPercent = 1.0f;

    private boolean IS_FIRST_LOAD = false;

    private Color mColor = new Color();


    /**
     * 设置上层rootview是否可以跟随手指移动
     */
    // private boolean CAN_MOVE = true;

    // /**
    // * 判断当前触点是否在受冲突的VIEW区域之内
    // */
    // private boolean MOVE_FORBID = false;

    /**
     * 设置底层控件是否可以长按
     */
    private boolean IS_LONG_CLICK_ABLE = true;

    // private boolean IS_TRANSFER = false;

    private boolean IS_HIDE_INPUT_KEYBOARD = false;//是否隐藏输入法键盘;

    /**
     * 原始关闭状态
     */
    private final int STATU_ORG = 0;

    /**
     * view移动中，手未放开
     */
    private final int STATU_TRANSFER = 1;

    /**
     * view移动中，手已经放开
     */
    private final int STATU_MOVING = 2;

    /**
     * 禁止移动
     */
    private final int STATU_FORBID = 3;

    /**
     * 当前状态
     */
    private int STATU_CURRENT = STATU_ORG;

    public static final int SHADOW_WIDTH;

    private static final int MOVE_TIME_WHOLE = 600;

    private static final int MOVE_TIME = 300;

    private boolean isHandle;


    static {
        MIN_X_DISTANCE = AndroidSystemHelper.dp2px(20, IhyApplication.mInstance);
        LOWER_ROOTVIEW_MARGINRIGHT = AndroidSystemHelper.dp2px(200, IhyApplication.mInstance);
        MAX_Y_DISTANCE = AndroidSystemHelper.dp2px(10, IhyApplication.mInstance);
        SHADOW_WIDTH = AndroidSystemHelper.dp2px(10, IhyApplication.mInstance);
    }

    public ActFinishHandler(final Activity act) {
        mActivity = act;
        screenWidth = AndroidSystemHelper.getScreenWidth(act);
        MAX_LEFT_DISTANCE = screenWidth;
        // screenHeight = DeviceUtils.getScreenHeight();
        mShadowView = act.findViewById(R.id.shadow_view);
        View rootVG = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
        if (rootVG instanceof RootViewGroup) {
            mRootView = (RootViewGroup) rootVG;
        }
        if (mRootView != null) {
            mRootView.setOnTouchListener(this);
            mRootView.setRootViewGroupAnimationListener(this);
        }

        // mContentView = (ViewGroup) act.findViewById(android.R.id.content);
        // mContentView.setBackgroundColor(Color.parseColor("#" + MAX_TRANSLATE
        // + "000000"));

//        initLowerRootView(act);

        STATU_CURRENT = STATU_MOVING;
        // rootView.scrollToStart(600);
        if (mRootView != null) {
            mRootView.scrollToStart(MOVE_TIME_WHOLE);
        }
        IS_FIRST_LOAD = true;
    }

    private void changeActBgAlpha() {
//        WindowManager.LayoutParams params = mWindow.getAttributes();
//        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        params.dimAmount = mPercent;
//        mWindow.setAttributes(params);
    }

    public boolean dispatcheOntouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isHandle = false;
                if (ev.getX() > MAX_LEFT_DISTANCE / 9) {
                    return false;
                } else {
                    isHandle = true;
                }
        }
        if (isHandle) {
            return onTouch(mRootView, ev);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mRootView == null) {
            return false;
        }
        if (mActivityCanMoveHandler != null
                && !mActivityCanMoveHandler.canMove()) {
            return false;
        }
        if (STATU_CURRENT == STATU_MOVING) {
            return true;
        }

        View view = mActivity.getCurrentFocus();
        if (view != null) {
            if (!ViewUtils.isTouchedViewOutSideEditText(view, event) && MotionEvent.ACTION_UP != event.getAction()) {
                return false;
            }
        }
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                cancelLongClick();
                xMove = event.getRawX() - xDown;
                xDistance = event.getX() - xDown;
                mPercent = 1 - xMove / screenWidth;
                yDistance = event.getRawY() - yDown;
                // 活动的距离
                // 获取顺时速度
                // System.out.println("xDown=" + xDown + "xDistance="
                // + (event.getX() - xDown) + "yDistance="
                // + (event.getRawY() - yDown));

                if (xMove < 0) {
                    xMove = 0;
                }
                if (STATU_CURRENT != STATU_TRANSFER
                        && Math.abs(yDistance) > LOWER_ROOTVIEW_MARGINRIGHT / 8
                        || STATU_CURRENT == STATU_FORBID) {
                    STATU_CURRENT = STATU_FORBID;
                    return false;
                }
                if (((Math.abs(yDistance) <= MAX_Y_DISTANCE) && (xDistance > MIN_X_DISTANCE))
                        || STATU_CURRENT == STATU_TRANSFER) {
                    STATU_CURRENT = STATU_TRANSFER;
                    if (!IS_HIDE_INPUT_KEYBOARD) {
                        IS_HIDE_INPUT_KEYBOARD = true;
                        AndroidSystemHelper.HideKeyboard(mActivity);
                    }
                    transferView();
                    setBackGroundColor();
                }
                break;
            case MotionEvent.ACTION_UP:
                IS_HIDE_INPUT_KEYBOARD = false;
                IS_LONG_CLICK_ABLE = true;
                if (STATU_CURRENT == STATU_TRANSFER) {
                    STATU_CURRENT = STATU_MOVING;
                    mRootView.scrollToEnd(mVelocityTracker, false, MOVE_TIME);
                }
                recycleVelocityTracker();
                if (STATU_CURRENT == STATU_FORBID) {
                    STATU_CURRENT = STATU_ORG;
                }
                if (mRootView.getScrollX() < 0) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                break;
            default:
                break;
        }
        if (mRootView.getScrollX() < 0) {
            return true;
        }
        return false;
    }

    private void transferView() {
        if (mRootView == null) {
            return;
        }
        mRootView.scrollTo((int) -xMove, 0);
        if (mLowerRootView == null) {
            return;
        }
        mLowerRootView.scrollTo((int) (LOWER_ROOTVIEW_MARGINRIGHT * mPercent),
                0);
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    private void setBackGroundColor() {
        if (Build.VERSION.SDK_INT > 11 && mShadowView != null) {
            int alpha = Math.min((int) Math.max((0xaa * (1 - mPercent)), 0), 0x55);
            int oxColor = mColor.argb(alpha, 0, 0, 0);
            LogOut.d("manymore13", "color = " + oxColor);
            mShadowView.setBackgroundColor(oxColor);
//            changeActBgAlpha();
        }
    }

    // @Override
    // public void finish()
    // {
    // finishEverything();
    // }

    @Override
    public void reBack() {
        STATU_CURRENT = STATU_ORG;
        if (mLowerRootView != null) {
            mLowerRootView.scrollTo(LOWER_ROOTVIEW_MARGINRIGHT, 0);
        }
        if (mListener == null) {
            return;
        }

        if (IS_FIRST_LOAD) {
            IS_FIRST_LOAD = false;
            mListener.activityAnimationComplete();
        } else {
            mListener.activityAnimationReBack();
        }
    }

    @Override
    public void moving(boolean isFinish, int X, int Y) {
        if (X < -screenWidth) {
            finishEverything();
        }
        mPercent = 1.0f + 1.1f * X / screenWidth;
        setBackGroundColor();
        if (mLowerRootView != null) {
            int destictionX = (int) (LOWER_ROOTVIEW_MARGINRIGHT * mPercent);
            if (destictionX < 0) {
                mLowerRootView.scrollTo(0, 0);
            } else {
                mLowerRootView.scrollTo(destictionX, 0);
            }
        }
    }

//    public void initLowerRootView(Activity act) {
//        Activity activity = KyeActivityManager.getInstance()
//                .getLowerActivity(act);
//        if (activity != null) {
//            View lowerVG = (((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
//            if (lowerVG instanceof RootViewGroup) {
//                mLowerRootView = (RootViewGroup) lowerVG;
//            }
//        }
//    }

    private void finishEverything() {
        if (mLowerRootView != null) {
            mLowerRootView.scrollTo(0, 0);
        }
        if (mListener != null) {
            mListener.activityAnimationFinish();
        }
        if (mActivity instanceof BaseActivity) {
            mActivity.finish();
        }
//        else if (mActivity instanceof BaseActivityGroup)
//        {
//            ((BaseActivityGroup) mActivity).finishActivity();
//        }
    }

    public void finishActivity() {
        if (mRootView == null) {
            return;
        }
        STATU_CURRENT = STATU_MOVING;
        mRootView.scrollToEnd(mVelocityTracker, true, 300);
    }

    public void setLeftTouchAbleWidth(int width) {
        this.MAX_LEFT_DISTANCE = width;
    }

    /**
     * 向右滑动activity再回到当前页面的回调
     *
     * @author res-jianpingliu
     */
    public interface ActivityAnimationEndListener {
        /**
         * activity初次加载动画结束回调方法
         */
        void activityAnimationComplete();

        /**
         * 回来的动画结束后的回调方法
         */
        void activityAnimationReBack();

        /**
         * activity被关闭，动画结束回调方法
         */
        void activityAnimationFinish();

        /**
         * activity首次调用Move
         */
        void activityAnimationFirstOpen();

    }

    private ActivityAnimationEndListener mListener;

    public void setActivityAnimationEndListener(
            ActivityAnimationEndListener listener) {
        mListener = listener;
    }

    public RootViewGroup getmLowerRootView() {
        return mLowerRootView;
    }

    public void setmLowerRootView(RootViewGroup mLowerRootView) {
        this.mLowerRootView = mLowerRootView;
    }

    /**
     * 还原目标Activity的rootView为初始状态
     */
    // public void reBackDescRootViewByActivityName(String activityName, String
    // tag)
    // {
    // Activity activity = XiXinActivityManager.getInstance()
    // .getActivityByName(activityName, tag);
    // if (activity != null)
    // {
    // RootViewGroup lowestRootView = (RootViewGroup) (((ViewGroup) activity
    // .findViewById(android.R.id.content)).getChildAt(0));
    // lowestRootView.scrollTo(0, 0);
    // }
    // else
    // {
    // throw new NullPointerException("不存在此Activity");
    // }
    // }
    private void cancelLongClick() {
        if (STATU_CURRENT == STATU_ORG || !IS_LONG_CLICK_ABLE) {
            return;
        }
        MessageQueue msgQueue = Looper.myQueue();
        if (msgQueue == null) {
            return;
        }
        Message msg = (Message) getPrivateObject(msgQueue, "mMessages");
        if (msg == null) {
            return;
        }
        Runnable runnable = (Runnable) getPrivateObject(msg, "callback");
        if (runnable == null) {
            return;
        }
        String clzName = runnable.getClass().getName();
        if (clzName != null
                && "android.view.View$CheckForLongPress".equals(clzName)) {
            Handler handler = (Handler) getPrivateObject(msg, "target");
            if (handler == null) {
                return;
            }
            handler.removeCallbacks(runnable);
            IS_LONG_CLICK_ABLE = false;
        }
    }

    private Object getPrivateObject(Object obj, String name) {
        try {
            Class<?> clz = obj.getClass();
            Field field;
            field = clz.getDeclaredField(name);
            field.setAccessible(true); // 抑制Java对修饰符的检查
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public interface ActivityCanMoveHandler {
        boolean canMove();
    }

    ActivityCanMoveHandler mActivityCanMoveHandler;

    public void setActivityCanMoveHandler(
            ActivityCanMoveHandler activityCanMoveHandler) {
        this.mActivityCanMoveHandler = activityCanMoveHandler;
    }
}