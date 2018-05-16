package com.ihypnus.ihypnuscare;

import android.app.Application;


/**
 * @Package com.ihypnus.ihypnuscare
 * @author: llw
 * @Description:
 * @date: 2018/5/16 17:17
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class IhyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化新网络框架请求
//        NetRequestHelper.getInstance().init(this);
    }
}
