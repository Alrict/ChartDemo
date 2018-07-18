package com.ihypnus.ihypnuscare.net;

import com.ihypnus.ihypnuscare.BuildConfig;

/**
 * @Package com.ihypnus.ihypnuscare.net
 * @author: llw
 * @Description:
 * @date: 2018/5/16 15:18
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class IhyAction {
    /**
     * 服务器ip
     */
    public final static String HOST = BuildConfig.SERVER_URL;
    /**
     * 登入
     */
    public final static String Login = HOST + "hypnusMgr/app/login";

    /**
     * 获取验证码
     */
    public static final String GetVerfityCode = HOST + "hypnusMgr/dmz/authCode/appget";
    /**
     * 注册
     */
    public static final String Register = HOST + "hypnusMgr/dmz/user/register";
    /**
     * 验证手机号码是否被注册
     */
    public static final String Validationet = HOST + "hypnusMgr/dmz/authCode/appcheck";
    /**
     * 验证手机号码对应的验证码
     */
    public static final String Appvalidation = HOST + "hypnusMgr/dmz/authCode/appvalidation";
    /**
     * 重置密码
     */
    public static final String ResetPwd = HOST + "hypnusMgr/app/user/pwd";
    /**
     * 获取设备列表
     */
    public static final String getPageList = HOST + "hypnusMgr/app/device/getPageList";
    /**
     * 删除（解绑）用户名下设备
     */
    public static final String unbind = HOST + "hypnusMgr/app/device/unbind";
    /**
     * 添加（绑定）用户名下设备
     */
    public static final String bind = HOST + "hypnusMgr/app/device/bind";
    /**
     * 获取某一时段使用信息（包括使用时长，参数信息）
     */
    public static final String events = HOST + "hypnusMgr/app/statisti/data/events";
    /**
     * 获取用户个人信息:如身高体重等
     */
    public static final String getinfo = HOST + "hypnusMgr/app/user/getinfo";
    /**
     * 统计柱状图数据
     */
    public static final String getHistogramData = HOST + "hypnusMgr/app/statisti/data/getAppHistogramData";
    /**
     * 更新用户信息
     */
    public static final String updateinfo = HOST + "hypnusMgr/app/user/updateinfo";
    /**
     * 获取用户名下默认设备
     */
    public static final String getdefaultDeviceId = HOST + "hypnusMgr/app/device/getdefault";
    /**
     * 设置用户默认数据读取设备
     */
    public static final String setdefaultDeviceId = HOST + "hypnusMgr/app/device/setdefault";
    /**
     * 获取OSS操作所需的权限信息
     */
    public static final String getSTSinfo = HOST + "hypnusMgr/app/user/getSTSinfo";
    /**
     * 获取设备参数以及状态信息
     */
    public static final String getShadowDevice = HOST + "hypnusMgr/app/device/getShadowDevice";
    /**
     * 更新设备参数
     */
    public static final String updateShadowDevice = HOST + "hypnusMgr/app/device/updateShadowDevice";
    /**
     * 找回密码
     */
    public static final String getPwd = HOST + "hypnusMgr/dmz/user/resetpwd";
    /**
     * 用户登出
     */
    public static final String logout = HOST + "hypnusMgr/logout";


}
