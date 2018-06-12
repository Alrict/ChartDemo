package com.ihypnus.ihypnuscare.net;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;

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


    public static void getUserRegister(String userInfo, String deviceId, Response.Listener<Object> successCallback, Response.ErrorListener faileCallback) {
//测试账号   18820000743  888888

//        String url = IhyAction.Register;
        String url = "http://Care.ihypnus.com/hypnusMgr/admin/statisti/data/getHistogramData";
//        String url = "http://106.15.184.7/hypnusMgr";
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceId", "CP70100506S");
        params.put("createDateDay", "20180515");
        params.put("endDateDay", "20180516");
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, successCallback, params, faileCallback);
        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);

    }

    public static void login(Response.Listener<Object> successCallback, Response.ErrorListener faileCallback) {
        String url = "http://106.15.184.7/hypnusMgr/login";
        Map<String, String> params = new HashMap<String, String>();
        params.put("captcha", "nfoa");
        params.put("enPassword", "BmRP8Dsu9BiQO8LMNOP3nN5EMKWMnjVvLclM8owVsOIBdYTO9iMmNV98wgJ8HLAX1NyikBFlrJ+8cUOjYH3ITFXWUcD8gtv2aGXpJ6k7z0gMItSAqHsI0imJWai5ajqgtr+e9TNMPbl3gw5FhbbMqWwmZOLDgFIO+vcx6t6wWW8=");
        params.put("exponent", "AQAB");
        params.put("modulus", "ALgu/auC+OsH39oe7gOTNAK1vDCi/i0xQks2rZ//XkDR2qpCZ/3ApzDW753HyGHpujv3Ay1+SH9vn87fDIMwElPkl3QbL7DUdet/4kOU32oCKothBmSJ7qyaxxtp4hjT/I0OqmvRw1btHHQGhF1C0zYZ6imMnrU+vC43UhQu3/PJ");
        params.put("password", "");
        params.put("rememberMe", "false");
        params.put("username", "18820000743");
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, successCallback, params, faileCallback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.NORMAL_STRING);
        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    public static void getUserConfirm2(Response.Listener<Object> successCallback, Response.ErrorListener faileCallback) {
//测试账号   18820000743  888888
        String url = "http://172.20.2.56:8080/kyeapi/GetInvoiceTitle";
//        String url = "http://106.15.184.7/hypnusMgr";
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuid", "8D34DBC67AAB4584816C0947936C3388");
//        params.put("carNo", "");
//        params.put("pageIndex", String.valueOf(1));
//        params.put("pageSize", String.valueOf(10));
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, successCallback, params, faileCallback);
        Map<String, String> headerParams = new HashMap<String, String>();
//        headerParams.put("access-token", "C5FE647725DA929111AAFC16D96ADEFD");
        headerParams.put("kye", "10002");
        headerParams.put("Content-Type", "application/json");
        httpRequest.putHeaders(headerParams);

        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.NORMAL_STRING);
//        httpRequest.setResponseJavaBean(CarSanitationListBean.class);
        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);

    }
}
