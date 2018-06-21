package com.ihypnus.ihypnuscare.controller;

import android.content.Context;
import android.view.View;

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


    public BaseController(Context context) {
        this.mContext = context;
        this.mRootView = initView();
        initData();
    }

    /**
     * 初始化布局
     *
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public abstract void initData();



    public View getRootView() {

        if (mRootView == null) {
            this.mRootView = initView();
        }
        return mRootView;
    }

    public abstract void onDestroy();
}
