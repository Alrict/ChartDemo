package com.ihypnus.ihypnuscare;

import android.app.Application;

import com.ihypnus.ihypnuscare.net.NetRequestHelper;


/**
 * @Package com.ihypnus.ihypnuscare
 * @author: llw
 * @Description:
 * @date: 2018/5/16 17:17
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class IhyApplication extends Application {

    public static IhyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // 初始化新网络框架请求
        NetRequestHelper.getInstance().init(this);
    }
}
