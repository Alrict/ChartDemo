package com.ihypnus.ihypnuscare.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.ihypnus.ihypnuscare.IhyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version V1.0
 * @createAuthor yzw
 * @createDate 2016/8/20 16:40
 * @updateAuthor
 * @updateDate
 * @company 跨越速运
 * @description 文件读写
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class FileUtils {

    public static void saveFileContent(String fileName, String str) {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + fileName;
        } else {
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + fileName;
        }
        FileOutputStream outStream=null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
        } catch (Exception e) {
            LogOut.printStackTrace(e);
        } finally {
            if (outStream!=null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存日志
     *
     * @param logStr 日志内容
     */
    public static void saveLog(String logStr) {
        saveFileContent("ppy.log", logStr);
    }

    public static void saveLog() {
        IhyApplication act = IhyApplication.mInstance;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = formatter.format(new Date());
        StringBuilder sb = new StringBuilder();
//        sb.append("time: " + time + "\n");
//        sb.append("userId: " + act.getUserInfo().getUserid() + "\n");
//        sb.append("telephone: " + act.getUserInfo().getTelePhone() + "\n");
//        sb.append("uuid: " + act.getUserInfo().getUUID() + "\n");
        FileUtils.saveLog(sb.toString());
    }

//    public static void saveLog(Object obj, String errorCode, String errMsg, String requestMethod) {
//        try {
//            boolean isNetWorkEnable = AndroidSystemHelper.isNetWorkEnable(GlobalApplication.getInstance());
//            FileUtils.saveFileContent("login.log", new Gson().toJson(obj) + "\n" + isNetWorkEnable);
//            if (isNetWorkEnable) {
//                upLoadErrorLogHelper.getInstance().uploadErrorLog(GlobalApplication.getInstance().getUserInfo().getTelePhone(), new Gson().toJson(obj), errorCode + " " + errMsg, requestMethod);
//            }
//        } catch (Exception e) {
//            LogOut.printStackTrace(e);
//        }
//
//    }

    /**
     * 创建缓存目录
     */
    public static File createCacheDir(Context act) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//是否有SD卡
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                appCacheDir = act.getExternalCacheDir();
            }
            if (appCacheDir == null) {//有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + act.getPackageName() + "/cache/");
            }
        } else {
            appCacheDir = act.getCacheDir();
        }
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }
        return appCacheDir;
    }

    /**
     * 创建缓存文件,可以清楚
     *
     * @param act
     * @param fileName
     * @return
     */
    public static File createCachaFile(Context act, String fileName) {
        try {
            File cacheDir = FileUtils.createCacheDir(act);
            File cacheFile = new File(cacheDir, fileName);
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }
            return cacheFile;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 创建数据路径,不会和缓存一起清除
     */
    public static File createRootDir(Context act) {
        File saveFile = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            saveFile = new File(Environment.getExternalStorageDirectory(), "data");
        } else {
            saveFile = new File(Environment.getDataDirectory(), "data");
        }
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        return saveFile;
    }


}
