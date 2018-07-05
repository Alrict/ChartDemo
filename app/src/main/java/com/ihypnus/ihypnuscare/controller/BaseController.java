package com.ihypnus.ihypnuscare.controller;

import android.content.Context;
import android.nfc.FormatException;
import android.view.View;

import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;

/**
 * @Package com.ihypnus.ihypnuscare
 * @author: llw
 * @Description:
 * @date: 2018/6/21 14:15
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseController {
    private View mRootView;
    protected Context mContext;
    //当前选择日期
    private long mDate;
    //前7天日期
    private long mLast7Dates;
    private NotifyDateChangedListener mNotifyDateChangedListener;


    public BaseController(Context context) {
        this.mContext = context;
        this.mRootView = initView();
        mContext = context;
        initData();
        loadData();
//        notifyDateChanged();
    }

    /**
     * 初始化布局
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 请求数据
     */
    public abstract void loadData();


    public View getRootView() {

        if (mRootView == null) {
            this.mRootView = initView();
        }
        return mRootView;
    }

    public abstract void onDestroy();

    public long getDate() {
        return mDate;
    }

    //设置当前选择的时间日期
    public void setDate(long date) {
        mDate = date;
        mLast7Dates = getSpecialDate(7);
//        notifyDateChanged(listener);
    }
//    public void setDate(long date,NotifyDateChangedListener listener) {
//        mDate = date;
//        mLast7Dates = getSpecialDate(7);
//        notifyDateChanged(listener);
//    }

    private void notifyDateChanged(NotifyDateChangedListener listener) {
        if (listener != null) {
            mNotifyDateChangedListener = listener;
            listener.onNotifyDateChangedListener();
        }
    }

    //获取查询条件的开始前L天的日期
    public String getStartTime() {
        return getStartTime(1);
    }

    public String getStartTime(long l) {
        long startDate = getSpecialDate(l);
        return DateTimeUtils.getStringDateTime(startDate);
    }

    public String getWeekStart(long l) {
        long startDate = getSpecialDate(l);
        return DateTimeUtils.getMonthDayDateTime(startDate);
    }

    public String getWeekEnd() {
        return DateTimeUtils.getMonthDayDateTime(mDate);
    }

    //获取相对当前时间前/后l天的日期
    private long getSpecialDate(long l) {
        return mDate - (24L * 60L * 60L * 1000L) * l;
    }

    //获取查询条件的结束日期(即用户当前选择的日期)
    public String getEndTime() {
        return DateTimeUtils.getStringDateTime(mDate);
    }

    //获取前7天的日期
    public long getLast7Dates() {
        return mLast7Dates;
    }

    public void setLast7Dates(long last7Dates) {
        mLast7Dates = last7Dates;
    }

    /**
     * 获取当前日期一周的时间范围
     *
     * @return
     * @throws FormatException
     */
    public String getWeekDates() throws FormatException {
        String startTime = getWeekStart(7);
        String endTime = getWeekEnd();
        if (StringUtils.isNullOrEmpty(startTime) || StringUtils.isNullOrEmpty(endTime)) {
            return "";
        } else if (startTime.split("-").length == 2 && endTime.split("-").length == 2) {
            String[] startTimesplit = startTime.split("-");
            String[] endTimesplit = endTime.split("-");
            String startMonth = startTimesplit[0] + "月";
            String startDay = startTimesplit[1] + "日";
            String endMonth = endTimesplit[0] + "月";
            String endDay = endTimesplit[1] + "日";
            LogOut.d("llw", startMonth + startDay + "~" + endMonth + endDay);
            return startMonth + startDay + "~" + endMonth + endDay;
        } else {
            return "";
        }

    }

    public interface NotifyDateChangedListener {
        void onNotifyDateChangedListener();
    }

    public void setNotifyDateChangedListener(NotifyDateChangedListener listener) {
        mNotifyDateChangedListener = listener;

    }
}
