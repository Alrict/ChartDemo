package com.ihypnus.ihypnuscare.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by ly on 2016/8/9.
 * 相机拍照
 */
public class TakePhotosUtils {
    /**
     * 跳转到相机页面拍照,并返回创建的图片文件
     * 文件名用时间戳命名
     */
    public static File takePicture(Activity act, int requestCode, Class<? extends Activity> clazz) {
        File appCacheDir = null;
        //先验证手机是否有sdcard
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//是否有SD卡
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                appCacheDir = act.getExternalCacheDir();
            }
            if (appCacheDir == null) {//有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + act.getPackageName() + "/cache/");
            }
        } else {
            ToastUtils.showToastInCenter(act, "未检出到SD卡,无法调取相机");
            return appCacheDir;
        }
//        创建目录
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }
        String imageName = String.valueOf(System.currentTimeMillis());
        File imageFile = new File(appCacheDir, imageName);
//        创建文件
        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (clazz != null) {
                Intent intent = new Intent(act, clazz);
                Uri u = Uri.fromFile(imageFile);
                intent.putExtra("OUTPUT", u);
                act.startActivityForResult(intent, requestCode);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Uri uri;
                if (Build.VERSION.SDK_INT >= 24) {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());
                    uri = FileProvider.getUriForFile(act, "com.ihypnus.fileprovider",imageFile);
//                    uri = act.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                } else {
                    uri = Uri.fromFile(imageFile);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                act.startActivityForResult(intent, requestCode);
            }
        } catch (SecurityException e) {
            ToastUtils.showToastDefault(act, "请检查相机权限是否打开");
        } catch (Exception e) {

        }
        return imageFile;
    }

    /**
     * 跳转到自定义相机页面拍照,并返回创建的图片文件
     * 文件名用时间戳命名
     */
    public static File takePicture(Activity act, int requestCode) {

        return takePicture(act, requestCode, null);
    }

    public static File takePicture(Fragment fragment, int requestCode) {
        return takePicture(fragment, requestCode, null);
    }

    public static File takePicture(Fragment fragment, int requestCode, Class<? extends Activity> clazz) {
        Activity act = fragment.getActivity();
        if (act == null) return null;

        File appCacheDir = null;
        //先验证手机是否有sdcard
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//是否有SD卡
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                appCacheDir = act.getExternalCacheDir();
            }
            if (appCacheDir == null) {//有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + act.getPackageName() + "/cache/");
            }
        } else {
            ToastUtils.showToastInCenter(act, "未检出到SD卡,无法调取相机");
            return appCacheDir;
        }
//        创建目录
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }
        String imageName = String.valueOf(System.currentTimeMillis());
        File imageFile = new File(appCacheDir, imageName);
//        创建文件
        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (clazz != null) {
                Intent intent = new Intent(act, clazz);
                Uri u = Uri.fromFile(imageFile);
                intent.putExtra("OUTPUT", u);
                fragment.startActivityForResult(intent, requestCode);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri u = Uri.fromFile(imageFile);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                fragment.startActivityForResult(intent, requestCode);
            }
        } catch (SecurityException e) {
            ToastUtils.showToastDefault(act, "请检查相机权限是否打开");
        }
        return imageFile;
    }
}
