package com.ihypnus.ihypnuscare.net;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.ihypnus.ihypnuscare.bean.CarSanitationListBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package com.ihypnus.ihypnuscare.net
 * @author: llw
 * @Description:
 * @date: 2018/5/16 15:14
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class IhyRequest {
    /**
     * 获取验证码登录验证码
     */
    public static void getUserConfirm2(Response.Listener<CarSanitationListBean> successCallback, Response.ErrorListener faileCallback) {

        String url = "http://172.20.2.56:8080/kyeapi/SearchVerhicleHealth";
//        String url = "http://106.15.184.7/hypnusMgr";
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuid", "7173454E10FD4046B6B464B4074CC095");
        params.put("carNo", "");
        params.put("pageIndex", String.valueOf(1));
        params.put("pageSize", String.valueOf(10));
        HttpRequest<CarSanitationListBean> httpRequest = new HttpRequest<>(Request.Method.POST, url, CarSanitationListBean.class, successCallback, params, faileCallback);
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("access-token", "C5FE647725DA929111AAFC16D96ADEFD");
        headerParams.put("kye", "10002");
        headerParams.put("Content-Type", "application/json");
        httpRequest.putHeaders(headerParams);
        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);

    }
}
