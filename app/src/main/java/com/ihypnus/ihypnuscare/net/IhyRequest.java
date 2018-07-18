package com.ihypnus.ihypnuscare.net;

import com.android.volley.Request;
import com.android.volley.ResponseCallback;
import com.android.volley.toolbox.Volley;
import com.ihypnus.ihypnuscare.bean.DefaultDeviceIdVO;
import com.ihypnus.ihypnuscare.bean.DeviceListVO;
import com.ihypnus.ihypnuscare.bean.HistogramData;
import com.ihypnus.ihypnuscare.bean.LoginBean;
import com.ihypnus.ihypnuscare.bean.OssTokenVO;
import com.ihypnus.ihypnuscare.bean.PersonMesVO;
import com.ihypnus.ihypnuscare.bean.PhoneVO;
import com.ihypnus.ihypnuscare.bean.ShadowDeviceBean;
import com.ihypnus.ihypnuscare.bean.UsageInfos;
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
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_JAVA_BEAN);
        httpRequest.setResponseJavaBean(LoginBean.class);
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
     * CP701000516S CP70100517S CP70100518S CP70100519S CP70100508S
     *
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
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_JAVA_BEAN);
        httpRequest.setResponseJavaBean(DeviceListVO.class);
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
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_JAVA_BEAN);
        httpRequest.setResponseJavaBean(UsageInfos.class);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    public static void getinfos(String JSESSIONID, boolean isCookie, ResponseCallback callback) {
        String url = IhyAction.getinfo;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_JAVA_BEAN);
        httpRequest.setResponseJavaBean(PersonMesVO.class);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }


    /**
     * 获取柱状图数据
     *
     * @param JSESSIONID
     * @param isCookie
     * @param callback
     */
    public static void getHistogramData(String JSESSIONID, boolean isCookie, String deviceId, String startTime, String endTime, ResponseCallback callback) {
        String url = IhyAction.getHistogramData;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        params.put("deviceId", deviceId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_JAVA_BEAN);
        httpRequest.setResponseJavaBean(HistogramData.class);
//        httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 0.0f));
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * @param JSESSIONID
     * @param isCookie
     * @param nickname
     * @param gender
     * @param birthday
     * @param weight
     * @param height
     * @param BMI
     * @param callback
     */
    public static void updateinfo(String JSESSIONID, boolean isCookie, String nickname, String gender, String birthday, String weight, String height, String BMI, ResponseCallback callback) {
        String url = IhyAction.updateinfo;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        params.put("nickname", nickname);
        params.put("gender", gender);
        params.put("birthday", birthday);
        params.put("weight", weight);
        params.put("height", height);
        params.put("BMI", BMI);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 获取用户名下默认设备
     *
     * @param JSESSIONID
     * @param isCookie
     * @param callback
     */
    public static void getDefaultDeviceId(String JSESSIONID, boolean isCookie, ResponseCallback callback) {
        String url = IhyAction.getdefaultDeviceId;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_JAVA_BEAN);
        httpRequest.setResponseJavaBean(DefaultDeviceIdVO.class);
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 设置用户默认数据读取设备
     *
     * @param JSESSIONID
     * @param isCookie
     * @param deviceId
     * @param callback
     */
    public static void setDefaultDeviceId(String JSESSIONID, boolean isCookie, String deviceId, ResponseCallback callback) {
        String url = IhyAction.setdefaultDeviceId;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        params.put("deviceId", deviceId);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
        NetRequestHelper.getInstance().add(httpRequest, url);
    }


    /**
     * 获取OSS操作所需的权限信息
     *
     * @param JSESSIONID
     * @param isCookie
     * @param callback
     */
    public static void getSTSToken(String JSESSIONID, boolean isCookie, ResponseCallback callback) {
        String url = IhyAction.getSTSinfo;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_JAVA_BEAN);
        httpRequest.setResponseJavaBean(OssTokenVO.class);
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 获取设备参数以及状态信息
     *
     * @param JSESSIONID
     * @param isCookie
     * @param deviceId
     * @param callback
     */
    public static void getShadowDevice(String JSESSIONID, boolean isCookie, String deviceId, ResponseCallback callback) {
        String url = IhyAction.getShadowDevice;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        params.put("deviceId", deviceId);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_JAVA_BEAN);
        httpRequest.setResponseJavaBean(ShadowDeviceBean.class);
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 更新设备参数
     *
     * @param params
     * @param callback
     */
    public static void updateShadowDevice(Map<String, Object> params, ResponseCallback callback) {
        String url = IhyAction.updateShadowDevice;

        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
//        httpRequest.setResponseJavaBean(ShadowDeviceBean.class);
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * APP找回密码
     *
     * @param JSESSIONID
     * @param isCookie
     * @param authCode
     * @param newPassword
     * @param callback
     */
    public static void getBackPassword(String JSESSIONID, boolean isCookie, String phone, String authCode, String newPassword, ResponseCallback callback) {
        String url = IhyAction.getPwd;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        params.put("isCookie", isCookie);
        params.put("phone", phone);
        params.put("authCode", authCode);
        params.put("newPwd", newPassword);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
        NetRequestHelper.getInstance().add(httpRequest, url);
    }

    /**
     * 用户登出
     *
     * @param JSESSIONID
     * @param callback
     */
    public static void userLogout(String JSESSIONID, ResponseCallback callback) {
        String url = IhyAction.logout;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("JSESSIONID", JSESSIONID);
        SpecialHttpRequest httpRequest = new SpecialHttpRequest(Request.Method.POST, url, params, callback);
        Volley.me.addInitRequestHead("Accept", "application/json");
        httpRequest.setResponseDataType(HttpRequest.ResponseDataType.RESULT_STRING);
        NetRequestHelper.getInstance().add(httpRequest, url);
    }
}
