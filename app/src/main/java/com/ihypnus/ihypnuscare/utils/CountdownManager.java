package com.ihypnus.ihypnuscare.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ihypnus.ihypnuscare.iface.OnCountdownEndListener;
import com.ihypnus.ihypnuscare.iface.OnCountdownIntervalListener;
import com.ihypnus.ihypnuscare.iface.OnCountdownListener;

import java.util.Timer;
import java.util.TimerTask;

public class CountdownManager {

    private final static String TAG = "CountdownManager";

    private static CountdownManager mInstance;

    public static synchronized CountdownManager getInstance() {
        synchronized (CountdownManager.class) {
            if (mInstance == null) {
                mInstance = new CountdownManager();
            }
        }
        return mInstance;
    }

    /*默认倒计时长*/
    private long countDownLength = 0;
    /**
     * 开始执行计时的类，可以在每秒实行间隔任务
     */
    private static Timer timer;

    /**
     * 每秒时间到了之后所执行的任务
     */
    private static TimerTask timerTask;

    /*间隔时间*/
    private long intervalTime = 5000;
    private long countdownTime = 5000;

    private OnCountdownEndListener onCountdownEndListener;

    private OnCountdownIntervalListener onCountdownIntervalListener;

    private OnCountdownListener onCountdownListener;

    /**
     * 开始倒计时
     */
    public void start(long length) {
        initTimer();
        this.countDownLength = length;
        timer.schedule(timerTask, 0, 1000);

    }

    public void seOnCountdownListener(OnCountdownListener onCountdownListener) {
        this.onCountdownListener = onCountdownListener;
    }

    public void setOnCountdownEndListener(OnCountdownEndListener onCountdownEndListener) {
        this.onCountdownEndListener = onCountdownEndListener;
    }

    public void setOnCountdownIntervalListener(long intervalTime, OnCountdownIntervalListener onCountdownIntervalListener) {
        this.onCountdownIntervalListener = onCountdownIntervalListener;
        this.intervalTime = intervalTime;
        this.countdownTime = intervalTime;
    }

    /**
     * 初始化时间
     */
    private void initTimer() {
        clearTimer();
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            countDownLength -= 1000;
            onCountdownListener.onCountdownTime(countDownLength);
            countdownTime -= 1000;
            if (countdownTime == 0) {
                onCountdownIntervalListener.onInterval();
                countdownTime = intervalTime;
            }
            if (countDownLength < 0) {
                CountdownManager.getInstance().countDownLength = -1;
                clearTimer();
                onCountdownEndListener.onEnd();
            }
        }
    };

    /**
     * 清除倒计时
     */
    public void clearTimer() {

        Log.i(TAG, "clearTimer");
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }


}
