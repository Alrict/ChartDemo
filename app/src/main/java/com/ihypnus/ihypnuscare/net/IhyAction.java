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
    public static final String Validationet = HOST + "hypnusMgr/dmz/authCode/validationet";
    /**
     * 验证手机号码对应的验证码
     */
    public static final String Appvalidation = HOST + "hypnusMgr/dmz/authCode/appvalidation";
    /**
     * 重置密码
     */
    public static final String ResetPwd = HOST + "hypnusMgr/app/user/pwd";

}
