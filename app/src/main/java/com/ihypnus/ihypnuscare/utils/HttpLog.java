package com.ihypnus.ihypnuscare.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @Package com.ihypnus.ihypnuscare.utils
 * @author: llw
 * @Description:
 * @date: 2018/5/17 15:42
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HttpLog {

    public static boolean LOG_FLAG = true;

    public HttpLog() {
    }

    public static void exceptionOut(String tag, String msg, Exception e) {
        if(LOG_FLAG) {
            Log.e(tag, msg, e);
        }

    }

    public static void v(String tag, String msg) {
        if(LOG_FLAG) {
            Log.v(tag, msg);
        }

    }

    public static void d(String tag, String msg) {
        if(LOG_FLAG) {
            Log.d(tag, msg);
        }

    }

    public static void i(String tag, String msg) {
        if(LOG_FLAG) {
            Log.i(tag, msg);
        }

    }

    public static void w(String tag, String msg) {
        if(LOG_FLAG) {
            Log.w(tag, msg);
        }

    }

    public static void e(String tag, String msg) {
        if(LOG_FLAG) {
            Log.e(tag, msg);
        }

    }

    public static void printStackTrace(Throwable e) {
        if(LOG_FLAG) {
            e.printStackTrace();
        }

    }

    public static String getExceptionMsg(Throwable ex) {
        if(ex == null) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);

            for(Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
                cause.printStackTrace(printWriter);
            }

            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            return sb.toString();
        }
    }
}
