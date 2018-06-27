package com.ihypnus.ihypnuscare.net;

import com.android.volley.Request;
import com.android.volley.ResponseCallback;
import com.ihypnus.ihypnuscare.bean.PhoneVO;
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

    public static void login(String username, String password, ResponseCallback callback) {
        String url = IhyAction.Login;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 校验手机号码是否被注册
     *
     * @param phoneNumber 手机号码
     * @param callback
     */
    public static void VerifyPhoneNumber(String phoneNumber, ResponseCallback callback) {
        String url = IhyAction.Validationet;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNumber);
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }


    /**
     * 校验手机号码对应的验证码
     *
     * @param phoneNumber 手机号码
     * @param authCode    验证码
     * @param callback
     */
    public static void VerifyPhoneCodeNumber(String phoneNumber, String authCode, ResponseCallback callback) {
        String url = IhyAction.Appvalidation;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNumber);
        params.put("authCode", authCode);
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }


    /**
     * 注册账号
     *  CP701000516S CP70100517S CP70100518S CP70100519S CP70100508S
     * @param userInfo 用户登入信息
     * @param deviceId 设备sn
     * @param callback
     */
    public static void registerApp(UserInfo userInfo, String deviceId, ResponseCallback callback) {
        String url = IhyAction.Register;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userInfo", userInfo);
        params.put("deviceId", deviceId);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 注册获取手机验证码
     *
     * @param phone    手机号
     * @param callback
     */
    public static void getVerifyCode(String phone, ResponseCallback callback) {
//        String url = "http://care.ihypnus.com/hypnusMgr/dmz/authCode/get";
        String url = IhyAction.GetVerfityCode;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        HttpRequest httpRequest = new HttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    public static void getVerifyCode(PhoneVO userIphonenfo, ResponseCallback callback) {
        String url = "http://care.ihypnus.com/hypnusMgr/dmz/authCode/get";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("phone", userIphonenfo);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 获取设备列表信息
     *
     * @param JSESSIONID
     * @param isCookie
     * @param callback
     */
    public static void getDeviceListInfos(String JSESSIONID, boolean isCookie, ResponseCallback callback) {
        String url = IhyAction.getPageList;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }


    /**
     * 重置密码
     *
     * @param JSESSIONID  表明是否为cookie
     * @param password    当前密码
     * @param newPassword 新密码
     * @param callback
     */
    public static void resetPassword(String JSESSIONID, boolean isCookie, String password, String newPassword, ResponseCallback callback) {
        String url = IhyAction.ResetPwd;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        params.put("password", password);
        params.put("newPassword", newPassword);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 删除（解绑）用户名下设备
     *
     * @param JSESSIONID
     * @param isCookie
     * @param deviceId
     * @param callback
     */
    public static void unbindDevice(String JSESSIONID, boolean isCookie, String deviceId, ResponseCallback callback) {
        String url = IhyAction.unbind;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        params.put("deviceId", deviceId);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 添加（绑定）用户名下设备
     *
     * @param JSESSIONID
     * @param isCookie
     * @param deviceId
     * @param callback
     */
    public static void bindDevice(String JSESSIONID, boolean isCookie, String deviceId, ResponseCallback callback) {
        String url = IhyAction.bind;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        params.put("deviceId", deviceId);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 获取某一时段使用信息（包括使用时长，参数信息）
     *
     * @param JSESSIONID
     * @param endTime
     * @param deviceId
     * @param callback
     */
    public static void getEvents(String JSESSIONID, String deviceId, String startTime, String endTime, ResponseCallback callback) {
        String url = IhyAction.events;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("deviceId", deviceId);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }


}
