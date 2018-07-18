package com.ihypnus.ihypnuscare.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 */
public class TimeUtils {
    /**
     * 根据时间秒数获取秒表时间，限60分钟
     *
     * @param second
     * @return
     */
    public static String getWatchTime(int second) {
        String times = "";
        if (second < 60) {
            times = "00:" + getSecondTime(second);
        } else if (second <= 3600) {
            int minute = second / 60;
            int seconds = second % 60;
            times = getSecondTime(minute) + ":" + getSecondTime(seconds);
        }
        return times;
    }

    /**
     * 根据时间秒数获取时分
     *
     * @param second
     * @return
     */
    public static String getHourAndMinute(int second) {
        String times = "";
        if (second < 60) {
            times = second + "秒";
        } else {
            int hour = second / 3600;
            int minute = (second % 3600) / 60;
            if (hour <= 0) {
                if (minute > 0) {
                    times = minute + "分";
                }
            } else {
                if (minute > 0) {
                    times = hour + "小时" + minute + "分";
                } else {
                    times = hour + "小时";
                }
            }
        }
        return times;
    }

    /**
     * 根据时间获取时，分，秒位上的单个字符串
     *
     * @param second
     * @return
     */
    public static String getSecondTime(int second) {
        String seconds;
        if (second < 10) {
            seconds = "0" + second;
        } else {
            seconds = "" + second;
        }
        return seconds;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeLong(String date_str, String format) {
//        LogOut.i("xxfff", "date_str==" + date_str);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 数字格式化 比如0  格式化后变 00 返回
     *
     * @param num
     * @return
     */
    public static String zeroFormat(int num) {
        if (num < 10 && num >= 0) {
            return "0" + num;
        }
        return num + "";
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param time   毫秒
     * @param format 格式
     * @return
     */
    public static String timeStamp2DateStr(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 今天是周几
     *
     * @return
     */
    public static String getWeek() {
        String str[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};//字符串数组
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(rightNow.DAY_OF_WEEK);//获取时间
        return str[day - 1];
    }
}
