package com.ihypnus.ihypnuscare.utils;


import android.annotation.SuppressLint;
import android.nfc.FormatException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static final String TAG = "DateTimeUtils";

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(long currentTimeMillis) {
        Date currentTime = new Date(currentTimeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间 年月日
     *
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String getStringDateByMounthDay(long currentTimeMillis) {
        Date currentTime = new Date(currentTimeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获得该月最后一天
     *
     * @return
     */
    public static int getLastDayOfMonth(long currentTimeMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTimeMillis);
//        //设置年份
//        cal.set(Calendar.YEAR,year);
//        //设置月份
//        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    /**
     * 获取当前日期一周的时间范围
     *
     * @return
     * @throws FormatException
     */
    public static String getEnglishWeekRangDates(long currentTimeMils) {
        String startTime = getMonthDayDateTime(currentTimeMils - (7L * 24L * 60L * 60L * 1000L));
        String endTime = getMonthDayDateTime(currentTimeMils);
        if (currentTimeMils <= 0) {
            return "";
        } else if (startTime.split("-").length == 2 && endTime.split("-").length == 2) {
            String[] startTimesplit = startTime.split("-");
            String[] endTimesplit = endTime.split("-");
            String month1 = startTimesplit[0];
            String startMonth = EN_MONTH[Integer.parseInt(month1) - 1];
            String startDay = startTimesplit[1];
            String endMonth = endTimesplit[0];
            endMonth = EN_MONTH[Integer.parseInt(endMonth) - 1];
            String endDay = endTimesplit[1];
            LogOut.d("llw", startMonth + startDay + "~" + endMonth + endDay);
            if (startDay.substring(0, 1).equals("0")) {
                startDay = startDay.replace("0", "");
            }
            if (endDay.substring(0, 1).equals("0")) {
                endDay = endDay.replace("0", "");
            }
            return startDay + startMonth + "~" + endDay + endMonth;
        } else {
            return "";
        }

    }

    public static String getWeekRangDates(long currentTimeMils) {
        String startTime = getMonthDayDateTime(currentTimeMils - (7L * 24L * 60L * 60L * 1000L));
        String endTime = getMonthDayDateTime(currentTimeMils);
        if (currentTimeMils <= 0) {
            return "";
        } else if (startTime.split("-").length == 2 && endTime.split("-").length == 2) {
            String[] startTimesplit = startTime.split("-");
            String[] endTimesplit = endTime.split("-");
            String startMonth = startTimesplit[0] + "月";
            String startDay = startTimesplit[1] + "日";
            String endMonth = endTimesplit[0] + "月";
            String endDay = endTimesplit[1] + "日";
            LogOut.d("llw", startMonth + startDay + "~" + endMonth + endDay);
            return startMonth + startDay + "~" + endMonth + endDay;
        } else {
            return "";
        }

    }

    /**
     * 返回年月日形式的时间日期
     *
     * @param currentTimeMillis
     * @return
     */
    public static String getStringDateTime(long currentTimeMillis) {
        Date currentTime = new Date(currentTimeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }

    public static String getMonthDayDateTime(long currentTimeMillis) {
        Date currentTime = new Date(currentTimeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    public static String getCurrentTime() {
        String str = android.text.format.DateFormat.format("yyyy-M-d kk:mm:ss", Calendar.getInstance().getTime()).toString();//获得系统时间
        return str;
    }

    public static String getCurrentTimeNoSecond() {
        String str = android.text.format.DateFormat.format("yyyy-MM-dd kk:mm", Calendar.getInstance().getTime()).toString();//获得系统时间
        return str;
    }

    public static String getCusomerTime() {
        String str = android.text.format.DateFormat.format("yyyy-MM-ddTHH:mm:ss", Calendar.getInstance().getTime()).toString();//获得系统时间
        return str;
    }

    public static String getCusomerTime2() {
        String str = android.text.format.DateFormat.format("yyyy-MM-ddTHH:mm", Calendar.getInstance().getTime()).toString();//获得系统时间
        return str;
    }

    public static String getCurrentDate() {
        String str = android.text.format.DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()).toString();//获得系统时间
        return str;
    }

    /**
     * 获取当前的月份
     *
     * @return yyyy-MM
     */
    public static String getCurrentMonth() {
        String str = android.text.format.DateFormat.format("yyyy-MM", Calendar.getInstance().getTime()).toString();//获得系统时间
        return str;
    }

    /**
     * 获取当前的月份
     *
     * @return MM
     */
    public static String getCurrentMonthOnly() {
        String str = android.text.format.DateFormat.format("MM", Calendar.getInstance().getTime()).toString();//获得系统时间
        return str;
    }


    public static String getLastMonthDate() {

        int lastYear;
        int lastMonth;
        int lastDay;

        lastMonth = Calendar.getInstance().get(Calendar.MONTH) - 1;
        if (lastMonth == -1) {
            lastYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
            lastMonth = 11;
        } else {
            lastYear = Calendar.getInstance().get(Calendar.YEAR);
        }

        lastDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if (lastDay == 31) {
            if (lastMonth == 3 || lastMonth == 5 || lastMonth == 8 || lastMonth == 10) {
                lastDay = 30;
            }
        }

        if (lastMonth == 1) {
            if (lastDay == 31 || lastDay == 30 || lastDay == 29) {
                lastDay = judge(lastYear) ? 29 : 28;
            }
        }

        String lastMonthStr;
        String lastDayStr;

        if (lastMonth + 1 < 10)
            lastMonthStr = "0" + (lastMonth + 1);
        else
            lastMonthStr = lastMonth + 1 + "";

        if (lastDay < 10)
            lastDayStr = "0" + lastDay;
        else
            lastDayStr = lastDay + "";

        String str = lastYear + "-" + lastMonthStr + "-" + lastDayStr;
        return str;
    }

    /**
     * 将时间格式为yyyy-MM-dd HH:mm:ss转换为long
     */
    public static long getOderTime(String time) {
        long millionSeconds;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            millionSeconds = sdf.parse(time).getTime();//毫秒
            return millionSeconds;
        } catch (Exception e) {
            LogOut.d(TAG, "error==" + e.toString());
        }
        return 0;
    }

    /**
     * 获得当前系统日期（24小时）
     *
     * @return
     */
    public static String getSystemDateTime() {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String date = myFmt2.format(now);
        return date;
    }

    /**
     * 获得当前系统日期（24小时）年月日小时 分
     *
     * @return
     */
    public static String getSystemDateTimeToOrder() {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String date = myFmt2.format(now);
        return date;
    }

    public static String getSysDate() {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyyMMddHH");
        Date now = new Date();
        String date = myFmt2.format(now);
        return date;
    }

    /**
     * 获得当前系统日期（24小时）年月日小时 分
     *
     * @return
     */
    public static String getSystemDayDate() {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd 23:59");
        Date now = new Date();
        String date = myFmt2.format(now);
        return date;
    }

    /**
     * 获得当前系统日期(只获取年，用于航班日期判断显示日期有用)
     *
     * @return
     */
    public static String getFightDateTime() {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy");
        Date now = new Date();
        String date = myFmt2.format(now);
        return date;
    }

    /**
     * 获取30天前那一天日期的时间戳
     *
     * @return time
     */
    public static long getLastMonthTime() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //判断每月的具体天数
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day += 1;
                break;
            //对于2月份需要判断是否为闰年
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    day -= 1;
                    break;
                } else {
                    day -= 2;
                    break;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                break;
            default:
                break;
        }

        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);// 设置为30天前的日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * 获取明天零点的时间戳
     *
     * @return time
     */
    public static long getNextDayTime() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        calendar.set(Calendar.MONTH, month - 1);
//        calendar.set(Calendar.DAY_OF_MONTH, day);// 设置为30天前的日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTimeInMillis();
    }

    /**
     * 时间是否大于今天.
     *
     * @param date   时间
     * @param patten 时间格式
     * @return 大于返回true, 否则返回false.
     */
    public static boolean isGreaterToday(String date, String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return System.currentTimeMillis() >= d.getTime() ? false : true;
    }

    /**
     * xxfff add
     * (年月日)日期比较大小，判断是否大于当前日期 已取货选择日期有用到
     *
     * @param currentDate 当前选择的日期
     * @param isMoreThan  true 为不得大于当前日期，false 为必须大于当前日期
     * @return （isMoreThan=true result=-1 表示选择日期大于当前日期,result=0 表示日期相等,result=1 表示选择日期小于当前日期）
     * （isMoreThan=false result=-1 表示选择日期小于当前日期,result=0 表示日期相等,result=1 表示选择日期大于当前日期）
     * result = -2 表示日期格式错误
     */
    public static int dateCompare(String currentDate, boolean isMoreThan) {
        String s1 = currentDate;
        String s2 = DateTimeUtils.getSystemDateTimeToOrder();
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        int result;
        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
            if (isMoreThan == true) {
                result = c2.compareTo(c1);
            } else {
                result = c1.compareTo(c2);
            }
        } catch (java.text.ParseException e) {
            result = -2;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * xxfff add
     * (年月日)日期比较大小，判断是否大于当前日期 已取货选择日期有用到
     *
     * @param currentDate 当前选择的日期
     * @param isMoreThan  true 为不得大于当前日期，false 为必须大于当前日期
     * @return （isMoreThan=true result=-1 表示选择日期大于当前日期,result=0 表示日期相等,result=1 表示选择日期小于当前日期）
     * （isMoreThan=false result=-1 表示选择日期小于当前日期,result=0 表示日期相等,result=1 表示选择日期大于当前日期）
     * result = -2 表示日期格式错误
     */
    public static int dateCompare2(String currentDate, boolean isMoreThan) {
        String s1 = currentDate;
        String s2 = DateTimeUtils.getSystemDayDate();
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        int result;
        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
            if (isMoreThan == true) {
                result = c2.compareTo(c1);
            } else {
                result = c1.compareTo(c2);
            }
        } catch (java.text.ParseException e) {
            result = -2;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * (比较时间.)
     * <h3>Version</h3>1.0
     * <h3>CreateTime</h3> 2016/12/6,9:56
     * <h3>UpdateTime</h3> 2016/12/6,9:56
     * <h3>CreateAuthor</h3>（Geoff）
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param time1 时间1（格式 yyyy-MM-dd）
     * @param time2 时间2（格式 yyyy-MM-dd）
     * @return 1(time1 > time2) 、 0(time1 == time2) 、 -1(除1，0的情况，都会返回-1)
     */
    public static int compareTime(String time1, String time2) {
        int status = -1;
        Date date1;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(time1);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(time2);
            long timeMillis1 = date1.getTime();
            long timeMillis2 = date2.getTime();

            if (timeMillis1 == timeMillis2) {
                status = 0;
            } else if (timeMillis1 > timeMillis2) {
                status = 1;
            } else {
                status = -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            status = -1;
        }

        return status;
    }

    /**
     * (判断是否在某个时间段内.)
     * <h3>Version</h3>1.0
     * <h3>CreateTime</h3> 2016/12/6,9:56
     * <h3>UpdateTime</h3> 2016/12/6,9:56
     * <h3>CreateAuthor</h3>（Geoff）
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param time1 时间1（格式 yyyy-MM-dd）
     * @param time2 时间2（格式 yyyy-MM-dd）
     * @param time3 时间3（格式 yyyy-MM-dd）
     * @return time1在time2与time3之间
     */
    public static boolean compareTime(String time1, String time2, String time3) {
        boolean status = false;
        try {
            java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Calendar c1 = java.util.Calendar.getInstance();
            java.util.Calendar c2 = java.util.Calendar.getInstance();
            java.util.Calendar c3 = java.util.Calendar.getInstance();
            c1.setTime(df.parse(time1));
            c2.setTime(df.parse(time2));
            c3.setTime(df.parse(time3));
            int i = c1.compareTo(c2);
            int j = c1.compareTo(c3);
            if ((i == 0 || i == 1) && (j == 0 || j == -1)) {
                status = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /**
     * 比较当前时间与之前保存时间（是否超过6小时）
     *
     * @param milliseconds
     * @return
     */
    public static boolean CompareTwoTime(long milliseconds) {
        Date now = new Date();
        Date lastdate = new Date(milliseconds);
        long l = now.getTime() - lastdate.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
//        long min=((l/(60*1000))-day*24*60-hour*60);
        if (hour >= 6 || day >= 1) {
            return true;
        }
//        if (min>= 1) {
//            return true;
//        }
        return false;
    }

    /**
     * 判断是否是闰年
     *
     * @param year
     * @return
     */
    public static boolean judge(int year) {
        boolean yearleap = (year % 400 == 0) || (year % 4 == 0) && (year % 100 != 0);//采用布尔数据计算判断是否能整除
        return yearleap;
    }

    /**
     * 判断是否大于当天的日期
     * result=-1 表示选择日期小于当前日期,result=0 表示日期相等,result=1 表示选择日期大于当前日期
     *
     * @param spSate 当前时间
     * @return
     */
    public static int dateCompareToday(String spSate) {
        String s1 = spSate;
        String s2 = DateTimeUtils.getCurrentSystemDate();
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        int result;
        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
            result = c1.compareTo(c2);
        } catch (java.text.ParseException e) {
            result = -2;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获得当前系统日期（24小时）年月日小时 分秒
     *
     * @return
     */
    public static String getCurrentSystemDate() {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String date = myFmt2.format(now);
        return date;
    }

    /**
     * 判断是否在时间范围内
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static boolean isInAdTime(String startDate, String endDate) {
        if (startDate == null || endDate == null) return true;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date star = null, end = null;
        try {
            star = format.parse(startDate);
            end = format.parse(endDate);
        } catch (ParseException e) {

            LogOut.e(TAG, e.toString());
        }
        if (star == null || end == null) return true;
        long currentTimeLong = System.currentTimeMillis();
        if (currentTimeLong > star.getTime() && currentTimeLong < end.getTime())
            return true;
        return false;
    }

    /**
     * 获取当天之前或之后的日期
     *
     * @param day 相隔的天数
     * @return
     */
    public static String getLastDate(int day) {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, day);    //得到前一天
        String yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return yestedayDate;
    }

    public static String getLastDate(String timeStr, int day) {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(df.parse(timeStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE, day);    // 得到n天
        String yestedayDate = df.format(calendar.getTime());
        return yestedayDate;
    }


    /**
     * 通过字符串的时间参数 返回日期对象
     *
     * @return 字符串时间对应的 日期对象
     */
    public static Date getDateTimeFromString(String timeStr) {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return myFmt2.parse(timeStr);
        } catch (ParseException e) {
            return new Date(System.currentTimeMillis());
        }
    }

    /**
     * 与当前时间对比  是否是同一天
     *
     * @param time
     * @return
     */
    public static boolean isTheSameDay(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        if (year == currentYear && month == currentMonth && day == currentDay) {
            return true;
        }
        return false;
    }

    /**
     * 将时间格式为yyyy-MM-dd HH:mm:ss转换为long
     */
    public static long getSimpleLongTime(String time) {
        long millionSeconds;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            millionSeconds = sdf.parse(time).getTime();//毫秒
            return millionSeconds;
        } catch (Exception e) {
            LogOut.printStackTrace(e);
        }
        return 0;
    }

    /**
     * 根据自定义格式获得当前系统日期时间
     *
     * @return
     */
    public static String getPatternSystemDate(String pattern) {
        SimpleDateFormat myFmt2 = new SimpleDateFormat(pattern);
        Date now = new Date();
        String date = myFmt2.format(now);
        return date;
    }

    /**
     * xxfff add
     *
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    /**
     * 日期降序排序
     * xxfff add
     */
//    public static void dateSort(List<AdInfo> list) {
//        Collections.sort(list, new Comparator<AdInfo>() {
//            /**
//             *
//             * @param lhs
//             * @param rhs
//             * @return an integer < 0 if lhs is less than rhs, 0 if they are
//             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
//             */
//            @Override
//            public int compare(AdInfo lhs, AdInfo rhs) {
//                try {
//
//                } catch (Exception e) {
//                    LogOut.printStackTrace(e);
//                    return -1;
//                }
//                Date date1 = stringToDate(lhs.getCreateDate());
//                Date date2 = stringToDate(rhs.getCreateDate());
//                // 对日期字段进行升序，如果欲降序可采用after方法
//                if (date1.before(date2)) {
//                    return 1;
//                }
//                return -1;
//            }
//        });
//    }

    /**
     * xxfff add
     *
     * @param dateString
     * @return
     */
    public static Date stringToDateLuYous(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    /**
     * xxfff add
     *
     * @param dateString
     * @return
     */
    public static String formatDate(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = simpleDateFormat.parse(dateString);
            return date.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 日期降序排序
     * xxfff add
     */
//    public static void dateSortLuYous(List<FastFreightBean.LuYous> list) {
//        Collections.sort(list, new Comparator<FastFreightBean.LuYous>() {
//            /**
//             *
//             * @param lhs
//             * @param rhs
//             * @return an integer < 0 if lhs is less than rhs, 0 if they are
//             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
//             */
//            @Override
//            public int compare(FastFreightBean.LuYous lhs, FastFreightBean.LuYous rhs) {
//                try {
//                    Date date1 = stringToDateLuYous(lhs.getLastTime());
//                    Date date2 = stringToDateLuYous(rhs.getLastTime());
//                    // 对日期字段进行升序，如果欲降序可采用after方法
//                    if (date1.before(date2)) {
//                        return 1;
//                    }
//                } catch (Exception e) {
//                    LogOut.printStackTrace(e);
//                    return -1;
//                }
//                return -1;
//            }
//        });
//    }

    /**
     * 根据自定义格式及date获取对应时间
     *
     * @param date
     * @param patten
     * @return
     */
    public static String getTimeFromDate(Date date, String patten) {
        String str = android.text.format.DateFormat.format(patten, date).toString();//获得对应格式的时间
        return str;
    }

    /**
     * 获取指定日后 后 dayAddNum 天的 日期
     *
     * @param dayAddNum 增加天数 格式为int;
     * @return
     * @ param day  日期，格式为String："2016-9-17";
     */
    public static String getDateStr(String day, int dayAddNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (nowDate != null) {
            Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateOk = simpleDateFormat.format(newDate2);
            return dateOk;
        }
        return "";
    }

    /**
     * 获取指定month的 日期
     *
     * @param i 增加月数 格式为int;
     * @return
     * @ param day  日期，格式为String："2016-9-17";
     */
    public static String getMonthStr(String s, int i) {
        String[] date = s.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);
        int changeYear = year + i / 12;
        int changeI = i % 12;
        int monthI = month + changeI;
        int changeMonth = month;
        if (monthI <= 0) {
            changeYear -= 1;
            changeMonth = 12 + monthI;
        } else if (monthI > 12) {
            changeYear += 1;
            changeMonth = monthI - 12;
        } else {
            changeMonth += changeI;
        }
        if (day == 31) {
            if ((String.valueOf(changeMonth)).matches("[4,6,9]|[1][1]")) {
                day = 30;
            }
        }
        if (day == 31 || day == 30 || day == 29) {
            if (changeMonth == 2) {
                if (changeYear % 4 == 0) {
                    day = 29;
                } else {
                    day = 28;
                }
            }
        }
        String _1 = "-";
        String _2 = "-";
        if (changeMonth < 10) {
            _1 = "-0";
        }
        if (day < 10) {
            _2 = "-0";
        }
        return changeYear + _1 + changeMonth + _2 + day;
    }

    /**
     * (获取当月最后一天.)
     * <h3>Version</h3>1.0
     * <h3>CreateTime</h3> 2017/1/17,16:06
     * <h3>UpdateTime</h3> 2017/1/17,16:06
     * <h3>CreateAuthor</h3>（Geoff）
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @return
     */
    public static String getMonthLastDay() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormater.format(cal.getTime());
    }

    /**
     * (获取当月第一天.)
     * <h3>Version</h3>1.0
     * <h3>CreateTime</h3> 2017/1/17,16:06
     * <h3>UpdateTime</h3> 2017/1/17,16:06
     * <h3>CreateAuthor</h3>（lcs）
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @return
     */
    public static String getMonthFirstDay() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateFormater.format(cal.getTime());
    }

    public static Date stringOrDate(String str) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (date == null) {
                date = getDateTimeFromString(getSystemDateTime());
            }
        }
        return date;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔天数  * @param date1  * @param date2  * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    public static String showTimeCount(long time) {
        String timeCount = "";
        long hourc = time / 3600000;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600000) / (60000);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        if (hour.equals("00")) {
            timeCount = minue + ":" + sec;
        } else {
            timeCount = hour + ":" + minue + ":" + sec;
        }

        return timeCount;
    }

    /**
     * 判断给的时间，跟当前时间比较大小（即是否过了这个点）
     * yyyy-MM-dd HH
     *
     * @param time time2 为当前时间
     * @return1(time1 > time2) 、 0(time1 == time2) 、 -1(除1，0的情况，都会返回-1)
     */
    public static int compareCurrentTime(String time) {
        int status = -1;
        Date date1;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH").parse(time);
            long timeMillis1 = date1.getTime();
            long timeMillis2 = System.currentTimeMillis();

            if (timeMillis1 == timeMillis2) {
                status = 0;
            } else if (timeMillis1 > timeMillis2) {
                status = 1;
            } else {
                status = -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            status = -1;
        }

        return status;
    }

    /**
     * 判断s1时间是否大于s2时间
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isErrorTime(String s1, String s2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date startDate = sdf.parse(s1);
            Date endDate = sdf.parse(s2);
            if (startDate.compareTo(endDate) <= 0) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
            days = 0;
        }
        return days;
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param startTime
     * @param endTimes
     * @return
     */

    public boolean isBelong(String startTime, String endTimes) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(startTime);
            endTime = df.parse(endTimes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar date = Calendar.getInstance();
        date.setTime(now);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    private static final String[] CN_NUMBER = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    //将日期转化成中文格式
    public static String date2Chinese(String date) throws FormatException {
        StringBuilder sb = new StringBuilder();
        String[] split = date.split("-");
        if (split.length != 3) {
            throw new FormatException();
        }
        //解析月份
        int mon = Integer.parseInt(split[1]);
        if (mon < 10) {
            sb.append(CN_NUMBER[mon]);
        } else if (mon < 20) {
            if (mon == 10) {
                sb.append("十");
            } else {
                sb.append("十").append(CN_NUMBER[mon % 10]);
            }
        }
        sb.append("月");
        //解析日份
        int day = Integer.parseInt(split[2]);
        if (day < 10) {
            sb.append(CN_NUMBER[day]);
        } else if (day < 20) {
            if (day == 10) {
                sb.append("十");
            } else {
                sb.append("十").append(CN_NUMBER[day % 10]);
            }
        } else if (day < 30) {
            if (day == 20) {
                sb.append("二十");
            } else {
                sb.append("二十").append(CN_NUMBER[day % 10]);
            }
        } else {
            sb.append("三十").append(CN_NUMBER[day % 10]);
        }
        sb.append("日");
        return sb.toString();
    }

    //日
    private static final String[] EN_DAY = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    //月份
    private static final String[] EN_MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

    //将日期转化成英文格式
    public static String date2English(String date) throws FormatException {
        StringBuilder sb = new StringBuilder();
        String[] split = date.split("-");
        if (split.length != 3) {
            throw new FormatException();
        }
        //解析日份
        int day = Integer.parseInt(split[2]);
        sb.append(day);
        //解析月份
        int mon = Integer.parseInt(split[1]) - 1;
        sb.append(EN_MONTH[mon]);

        return sb.toString();
    }

    public static long getCurrentTimeMills() {
        return System.currentTimeMillis();
    }
}