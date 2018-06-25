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
     * 获取验证码
     */
    public static final String GetVerfityCode = HOST + "hypnusMgr/dmz/authCode/get?phone=";
    /**
     * 注册
     */
    public static final String Register = HOST + "hypnusMgr/dmz/user/register";
    /**
     * 验证手机号码是否被注册
     */
    public static final String Validationet = HOST + "hypnusMgr/dmz/authCode/validationet";

}
