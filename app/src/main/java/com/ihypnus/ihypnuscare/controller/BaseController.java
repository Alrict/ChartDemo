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
    //选择的日期
    private long mDate;


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

    public void setDate(long date) {
        mDate = date;
    }


    public String getStartTime() {
        long startDate = mDate - (24L * 60L * 60L * 1000L);
        return DateTimeUtils.getStringDateTime(startDate);
    }

    public String getEndTime() {
        return DateTimeUtils.getStringDateTime(mDate);
    }
}
