package com.ihypnus.ihypnuscare.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.ihypnus.ihypnuscare.IhyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ConcurrentModificationException;


/**
 * @author yzw
 * @version V1.0
 * @company 跨越速运
 * @Description
 * @date 2015/12/15
 */
public class KyeSys {

    private static final String TAG = "KyeSys";

    public static final String KYE_ROOT_FILE_NAME = "iHynusCare";

    public static final String KYE_CRASH = "crashlog";

    public static final String KYE_HIDE_FOLDER = ".hidefolder";

    public static final String KYE_DATA = "data";

    public static final String KYE_VOICE = "v";
    public static final String KYE_NOTICE = "notice";         //通知消息保存文件夹
    public static final String KYE_COMMENT = "notice/comment";         //通知消息评论保存文件夹


    public static String LOCAl_AD_KEY = "adLocalAdress";
    public static String AD_IS_CLOSE = "ad_is_close";
    public final static String FIRST_START_KEY = "first_start";
    public final static String KYE_WEBCACH = "webcach";         // webView的缓存文件夹
    public final static String KYE_WEB_APP_CACH = "webappcach"; // webView的app缓存文件夹
    public final static String KYE_PHOTO_CACH = "photo";        // 照片的缓存文件夹
    public static String DOWNLOAD_FILE = "downloadFile";
    public static String VIDEO_FILE = "videoFile"; // 视屏文件

    protected static Context mContext = IhyApplication.mInstance;

    public KyeSys() {
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void hidKeyBoard() {
        try {
            InputMethodManager e = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (((Activity) mContext).getCurrentFocus() != null && ((Activity) mContext).getCurrentFocus().getWindowToken() != null) {
                e.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), 2);
            }
        } catch (Exception var1) {
            LogOut.d(TAG, "error==" + var1.toString());
        }
    }

    public static SharedPreferences getSharedPreferences(String spName) {
        SharedPreferences sp;
        if (spName != null) {
            sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        } else {
            sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return sp;
    }

    public static String getLoginFolder() {
        File file = mContext.getExternalCacheDir();
        if (file.exists()) {
            file = mContext.getCacheDir();
        } else {
            file = mContext.getExternalCacheDir();
        }

        return file.getAbsolutePath() + File.separator;
    }

    public static String getCWHideFolder() {
        String path = getKyeRoot() + File.separator + KYE_HIDE_FOLDER;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        return path;
    }

    public static File getAppCacheFile() {
        return new File(getCWHideFolder());
    }

    public static String getString(String key) {
        return getSharedPreferences(key).getString(key, null);
    }

    public static String getString(String spName, String key, String defValue) {
        return getSharedPreferences(spName).getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreferences(null).getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return getSharedPreferences(null).getLong(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences(null).getBoolean(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return getSharedPreferences(null).getString(key, defValue);
    }

    public static int getInt(String spName, String key, int defValue) {
        return getSharedPreferences(null).getInt(key, defValue);
    }

    public static long getLong(String spName, String key, int defValue) {
        return getSharedPreferences(null).getLong(key, (long) defValue);
    }

    public static boolean getBoolean(String spName, String key, boolean defValue) {
        return getSharedPreferences(null).getBoolean(key, defValue);
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(null).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(null).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 设置当前弹窗对象的ID
     *
     * @param id
     */
    public static void setFloatWindowAdInforId(String id) {
        SharedPreferences.Editor editor = getSharedPreferences(null).edit();
        editor.putString("AdInfoId", id);
        editor.commit();
    }

    /**
     * 获取当前弹窗对象的ID
     *
     * @return
     */
    public static String getFloatWindowAdInfoId() {
        return getSharedPreferences(null).getString("AdInfoId", "");
    }


    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(null).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(null).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取序列化对象
     *
     * @param objPath 对象路径
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object getSerializableObject(String objPath) {
        ObjectInputStream in = null;
        Object retValue = null;
        try {
            in = new ObjectInputStream(new FileInputStream(objPath));
            retValue = in.readObject();
        } catch (NullPointerException e) {
            LogOut.printStackTrace(e);
        } catch (IOException e) {
            LogOut.printStackTrace(e);
        } catch (ClassNotFoundException e) {
            LogOut.printStackTrace(e);
        } catch (Exception e) {
            LogOut.printStackTrace(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                LogOut.d(TAG, "error==" + e.toString());
            }
        }
        return retValue;
    }

    public static Object getSerializableObject(File objFile) {
        if (objFile == null) {
            return null;
        }
        return getSerializableObject(objFile.getAbsolutePath());
    }

    /**
     * 序列化对象
     *
     * @param file  对象存储路径
     * @param value
     * @throws IOException
     */
    public static boolean setSerializableObject(File file, Object value) {
        boolean isSuccess = true;
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(value);
            out.flush();
        } catch (IOException e) {
            isSuccess = false;
            LogOut.d(TAG, "error==" + e.toString());
        } catch (ConcurrentModificationException e) {
            LogOut.e("error", "ConcurrentModificationException崩溃了");
            isSuccess = false;
            LogOut.printStackTrace(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                isSuccess = false;
                LogOut.d(TAG, "error==" + e.toString());
            }

        }
        return isSuccess;
    }

    /**
     * @param path  序列化对象的路径 +文件名
     * @param value 序列化对象
     * @throws IOException
     */
    public static void setSerializableObject(String path, Object value) throws IOException {
        setSerializableObject(new File(path), value);
    }

    /**
     * 获取包信息
     *
     * @return
     */
    public static PackageInfo getSystemPackageInfo() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var1) {
            LogOut.d(TAG, "error==" + var1.toString());
            return null;
        }
    }

    /**
     * 获取versionName
     *
     * @return
     */
    public static String getSystemVersionName() {
        PackageInfo pi = getSystemPackageInfo();
        return pi != null ? pi.versionName : null;
    }

    /**
     * 获取versionCode
     *
     * @return versionCode
     */
    public static int getSystemVersionCode() {
        PackageInfo pi = getSystemPackageInfo();
        return pi != null ? pi.versionCode : -1;
    }

    /**
     * 拨打电话
     *
     * @param act
     * @param phoneNum
     */
    public static void callKyePhone(Activity act, String phoneNum) {

        Intent in = new Intent("android.intent.action.CALL", Uri.parse("tel:"
                + phoneNum));
        act.startActivity(in);
    }


    /**
     * 获取app根目录
     *
     * @return
     */
    public static String getKyeRoot() {
        return getKyeRootFolder().getAbsolutePath();
    }

    public static String getUploadFilePath() {
        String path = getKyeRoot() + File.separator + DOWNLOAD_FILE;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取崩溃日志路径
     *
     * @return 获取崩溃日志路径
     */
    public static String getKrashPath() {
        String path = getKyeRoot() + File.separator + KYE_CRASH;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取崩溃日志路径
     *
     * @return 获取崩溃日志路径
     */
    public static String getVoicePath() {
        String path = getKyeRoot() + File.separator + KYE_VOICE;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取崩溃日志路径
     *
     * @return 获取崩溃日志路径
     */
    public static String getDataPath() {
        String path = getKyeRoot() + File.separator + KYE_DATA;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 视屏路径
     *
     * @return
     */
    public static String getVideoPath() {
        String path = getKyeRoot() + File.separator + VIDEO_FILE;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static File getKyeRootFolder() {
        File root = new File(getSDCardFolder(), KYE_ROOT_FILE_NAME);
        if (!root.exists()) {
            root.mkdirs();
        }
        return root;
    }

    public static File getSDCardFolder() {
        File root = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            root = Environment.getExternalStorageDirectory();
        } else {
            root = Environment.getDataDirectory();
        }

        return root;
    }


    /**
     * 获取webView的缓存路径
     *
     * @return
     */
    public static String getWebCachPath() {
        String path = KyeSys.getKyeRoot() + File.separator + KYE_WEBCACH;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取webView的缓存路径
     *
     * @return
     */
    public static String getWebAppCachPath() {
        String path = KyeSys.getKyeRoot() + File.separator + KYE_WEB_APP_CACH;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取照片的缓存路径
     *
     * @return
     */
    public static String getPhotoCachPath() {
        String path = KyeSys.getKyeRoot() + File.separator + KYE_PHOTO_CACH;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 将内容保存到本地,并保存时间戳
     *
     * @param cacheFile
     * @param timeStamp
     * @param content
     */
    public static void write2Disk(File cacheFile, String timeStamp, String content) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(cacheFile));
            //            将时间写入文件
            writer.write(timeStamp);
            //            换行
            writer.newLine();
            //            将数据写入磁盘
            writer.write(content);

        } catch (IOException e) {
            LogOut.d(TAG, "error==" + e.toString());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LogOut.d(TAG, "error==" + e.toString());
                }
            }
        }
    }


    /**
     * 获取保存通知消息路径
     *
     * @return 获取保存通知消息路径
     */
    public static String getNocicePath() {
        String path = getKyeRoot() + File.separator + KYE_NOTICE;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取保存通知消息评论路径
     *
     * @return 获取保存通知消息路径
     */
    public static String getNociceCommentPath() {
        String path = getKyeRoot() + File.separator + KYE_COMMENT;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static boolean fileInputAndOutPut(String oldPath, File newFile) {
        //声明输入输出流
        FileInputStream fis = null;
        FileOutputStream fos = null;
        boolean isSuccess = true;
        try {
            File oldFile = new File(oldPath);
            fis = new FileInputStream(oldFile);
            //要写入的文件路径
            fos = new FileOutputStream(newFile, false);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
        } catch (Exception e) {
            LogOut.printStackTrace(e);
            isSuccess = false;
        } finally {
            try {
                //关闭输入输出流
                fis.close();
                fos.close();
            } catch (Exception e) {
                LogOut.printStackTrace(e);
            }
        }
        return isSuccess;
    }
}