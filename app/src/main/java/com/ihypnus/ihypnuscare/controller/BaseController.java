package com.ihypnus.ihypnuscare.controller;

import android.content.Context;
import android.view.View;

import com.ihypnus.ihypnuscare.utils.DateTimeUtils;

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
    //前7天日期
    private long mLast7Dates;


    public BaseController(Context context) {
        this.mContext = context;
        this.mRootView = initView();
        mContext = context;
        initData();
        loadData();
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

    /**
     * 刷新列表数据
     */
    public abstract void refreshData();

    public View getRootView() {

        if (mRootView == null) {
            this.mRootView = initView();
        }
        return mRootView;
    }

    public abstract void onDestroy();


    public String getStartTime(long currentTime) {
        return DateTimeUtils.getStringDateByMounthDay(currentTime - ((24L * 60L * 60L * 1000L)));
    }

    public String getEndTime(long currentTime) {
        return DateTimeUtils.getStringDateByMounthDay(currentTime);
    }

    public String getWeekRang(long currentTime) {
        return DateTimeUtils.getWeekRangDates(currentTime);
    }
}
