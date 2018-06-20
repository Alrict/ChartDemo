package com.ihypnus.ihypnuscare.net;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ResponseCallback;
import com.ihypnus.ihypnuscare.bean.UserInfo;

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
        String url = "http://care.ihypnus.com/hypnusMgr/app/login";
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "18820000743");
        params.put("password", "12345678");
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, successCallback, params, faileCallback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.NORMAL_STRING);
        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 校验手机号码是否被注册
     *
     * @param phoneNumber     手机号码
     * @param successCallback
     * @param faileCallback
     */
    public static void VerifyPhoneNumber(String phoneNumber, Response.Listener<Object> successCallback, Response.ErrorListener faileCallback) {
        String url = "http://care.ihypnus.com/hypnusMgr/dmz/authCode/validationet";
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNumber);
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, successCallback, params, faileCallback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.NORMAL_STRING);
        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }


    /**
     * 注册
     *
     * @param userInfo        用户登入信息(帐号密码信息)
     * @param deviceId        设备id
     * @param successCallback
     * @param faileCallback
     */
    public static void registerApp(UserInfo userInfo, String deviceId, Response.Listener<Object> successCallback, Response.ErrorListener faileCallback) {
        String url = "http://care.ihypnus.com/hypnusMgr/dmz/user/register";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userInfo", userInfo);
//        params.put("deviceId", deviceId);
        params.put("deviceId", "CP70100508S");
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, successCallback, params, faileCallback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.NORMAL_STRING);
        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    public static void registerApp(UserInfo userInfo, String deviceId, ResponseCallback callback) {
        String url = "http://care.ihypnus.com/hypnusMgr/dmz/user/register";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userInfo", userInfo);
//        params.put("deviceId", deviceId);
        params.put("deviceId", "CP70100508S");
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.NORMAL_STRING);
        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

}
