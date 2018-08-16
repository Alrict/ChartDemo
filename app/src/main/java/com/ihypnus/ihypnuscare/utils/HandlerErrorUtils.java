package com.ihypnus.ihypnuscare.utils;

import android.content.Context;

import com.ihypnus.ihypnuscare.R;

/**
 * @Package com.ihypnus.ihypnuscare.utils
 * @author: llw
 * @Description:
 * @date: 2018/8/15 18:43
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HandlerErrorUtils {

    public static String handlerErrorMsg(Context context, String errorCode, String errorMsg) {
        if (errorCode.equals("0000")) {
            return context.getString(R.string.tv_toast_success);
        }
        if (errorCode.equals("0001")) {
            return context.getString(R.string.tv_toast_send_success);
        }
        if (errorCode.equals("0013")) {
            return context.getString(R.string.tv_toast_error0013);
        }
        if (errorCode.equals("1000")) {
            return context.getString(R.string.tv_toast_error1000);
        }
        if (errorCode.equals("1119")) {
            return context.getString(R.string.tv_toast_error1119);
        }
        if (errorCode.equals("1120")) {
            return context.getString(R.string.tv_toast_pwd_error);
        }
        if (errorCode.equals("1121")) {
            return context.getString(R.string.tv_toast_account_locked);
        }
        if (errorCode.equals("1300")) {
            return context.getString(R.string.tv_toast_error1300);
        }
        if (errorCode.equals("1301")) {
            return context.getString(R.string.tv_toast_error1301);
        }
        if (errorCode.equals("1302")) {
            return context.getString(R.string.tv_toast_error1302);
        }
        if (errorCode.equals("1304")) {
            return context.getString(R.string.tv_toast_error1304);
        }
        if (errorCode.equals("1305")) {
            return context.getString(R.string.tv_toast_error1305);
        }

        if (errorCode.equals("1306")) {
            return context.getString(R.string.tv_toast_error1036);
        }
        if (errorCode.equals("1307")) {
            return context.getString(R.string.tv_toast_error1037);
        }
        if (errorCode.equals("1308")) {
            return context.getString(R.string.tv_toast_error1038);
        }
        if (errorCode.equals("1310")) {
            return context.getString(R.string.tv_toast_error1310);
        }
        if (errorCode.equals("1311")) {
            return context.getString(R.string.tv_toast_error_1311);
        }
        if (errorCode.equals("1312")) {
            return context.getString(R.string.tv_toast_error_1312);
        }
        if (errorCode.equals("1400")) {
            return context.getString(R.string.tv_toast_value_ilegal);
        }

        return errorMsg;
    }
}
