package com.ihypnus.ihypnuscare.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.ihypnus.ihypnuscare.IhyApplication;
import com.ihypnus.ihypnuscare.widget.CusttomToast;

/**
 * @Package com.ihypnus.ihypnuscare.widget
 * @author: llw
 * @Description:
 * @date: 2018/5/17 17:44
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ToastUtils {


    private static final String TAG = "ToastUtils";
    private static long lastClickTime;
    private static long mDuration = 0;
    private static Context mContext;
    private static String mContent = "";
    private static Toast mToast;
    /**
     * 上限5s
     */
    private static final long SHORT_DURATION_TIMEOUT = 5000;
    /**
     * 下限时间500ms
     */
    private static final long LONG_DURATION_TIMEOUT = 500;
    //默认时间1s
    private static final long DEFAULT_TIME = 1000;
    private static Handler mHandler = new Handler();


    /**
     * 普通吐司
     *
     * @param context
     * @param content
     */
    public static void showToastInCenter(Context context, String content) {
        if (isFastClick()) return;
        if (context == null || StringUtils.isNullOrEmpty(content)) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        CusttomToast.makeText(IhyApplication.mInstance, content, Toast.LENGTH_SHORT).show();

    }


    public static void showToastDefault(Context context, String content) {

        if (isFastClick()) return;
        if (context == null || StringUtils.isNullOrEmpty(content)) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
//        CustomToast.makeText(GlobalApplication.instance.getApplicationContext(), content, Toast.LENGTH_SHORT).show();
        CusttomToast.makeText(IhyApplication.mInstance, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @param content  显示内容
     * @param duration 自定义显示时长
     */
    public static void showToastCusttomTime(Context context, String content, long duration) {
//        if(isFastClick()) return;
//        if (context == null) {
//            return;
//        }
//        if (context instanceof Activity) {
//            if (((Activity) context).isFinishing())
//                return;
//        }
//        int offsetY = ViewUtils.getDimenPx(R.dimen.h500);
//        Toast toast = Toast.makeText(context,content,Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM,0,offsetY);
//        toast.show();
        if (isFastClick()) return;
        if (context == null || StringUtils.isNullOrEmpty(content)) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
//        CustomToast.makeText(context, content,Integer.parseInt(String.valueOf(duration))).show();
        show(context, content, duration);
    }


    /**
     * 防止快速点击
     *
     * @return
     */
    private static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (600 < timeD) {
            lastClickTime = time;
            return false;
        }
        lastClickTime = time;
        return true;
    }

    private static void show(Context context, String content, long duration) {
        mContext = context;
        mContent = content;
        mDuration = duration;
        LogOut.d(TAG, "Show custom toast");
        if (null == mHandler) {
            mHandler = new Handler();
        }
        mHandler.post(showRunnable);
    }

    private static void hide() {
        LogOut.d(TAG, "Hide custom toast");
        mDuration = 0;
        if (mToast != null) {
            mToast.cancel();
            if (null != showRunnable) {
                mHandler.removeCallbacks(showRunnable);
                mHandler = null;
                LogOut.i("xxffff", "Remove runnable");
            }
        }
    }

    private static Runnable showRunnable = new Runnable() {
        @Override
        public void run() {
            if (mDuration != 0) {
                mToast = CusttomToast.makeText(mContext, mContent, Toast.LENGTH_LONG);
                mToast.show();
            } else {
                LogOut.d(TAG, "Hide custom toast in runnable");
                hide();
                return;
            }
            if (mDuration > SHORT_DURATION_TIMEOUT) {
                mDuration = SHORT_DURATION_TIMEOUT;
            } else if (mDuration < LONG_DURATION_TIMEOUT) {
                mDuration = DEFAULT_TIME;
            }
            mHandler.postDelayed(showRunnable, mDuration);
            mDuration = 0;
        }
    };
}
