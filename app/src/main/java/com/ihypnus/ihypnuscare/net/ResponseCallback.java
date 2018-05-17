package com.ihypnus.ihypnuscare.net;

import com.android.volley.VolleyError;

/**
 * @Package com.ihypnus.ihypnuscare.net
 * @author: llw
 * @Description:
 * @date: 2018/5/17 15:22
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public interface ResponseCallback {
    void onSuccess(Object var1, String var2, String var3);

    void onError(VolleyError var1, String var2, String var3);
}