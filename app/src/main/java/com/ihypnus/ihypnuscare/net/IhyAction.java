package com.ihypnus.ihypnuscare.net;

/**
 * @Package com.ihypnus.ihypnuscare.net
 * @author: llw
 * @Description:
 * @date: 2018/5/16 15:18
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class IhyAction {
    public final static String HOST = "http://172.20.2.56:8080/";
    /**
     * 获取登录验证码( 内部,外请,代理,合作伙伴)
     */
    public static final String Get_Login_Code = HOST + "GetVCode";
    /**
     * 注册
     */
    public static final String Register = "http://care.ihypnus.com/hypnusMgr/dmz/user/register";

}
