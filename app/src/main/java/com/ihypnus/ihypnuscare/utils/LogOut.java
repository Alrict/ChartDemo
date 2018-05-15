package com.ihypnus.ihypnuscare.utils;

import android.util.Log;

/**
 * 项目名称：LeCheng
 * 类名称：LogOut
 * 类描述：   日志工具类
 * 创建人：张勇军
 * 创建时间：2015年06月11日 下午4:30:46
 * 修改人：张勇军
 * 修改时间：2015年06月11日 下午4:30:46
 * 修改备注：
 */
public class LogOut {
    
    public static boolean DEBUG = true;
    
    public static void exceptionOut(String tag, String msg, Exception e) {
        if (DEBUG) {
            Log.e(tag, msg, e);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void printStackTrace(Throwable e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }
}
