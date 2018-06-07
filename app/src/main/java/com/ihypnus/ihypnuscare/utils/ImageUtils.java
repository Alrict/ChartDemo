package com.ihypnus.ihypnuscare.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by ly on 2016/8/4.
 * 图片的工具类
 */
public class ImageUtils {

    private static final String TAG = "ImageUtils";

    private static final int BITMAP_DEFAULT_WIDTH = 1080;

    private static final int BITMAP_DEFAULT_HEIGHT = 1920;
    private int quality;

    /**
     * 获取一个bitmap在内存中所占的大小
     *
     * @param bitmap
     * @return
     */
    public static int getMomerySize(Bitmap bitmap) {
        int size = 0;
        if (bitmap == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            size = bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            size = bitmap.getByteCount();
        } else {
            size = bitmap.getRowBytes() * bitmap.getHeight();
        }
        return size;
    }

    /**
     * 获得本地图片,获取过程中做了尺寸压缩和像素压缩
     *
     * @param imagePath 图片的本地路径
     * @return
     */
    public static Bitmap getBitmap(String imagePath) {
        return getBitmap(imagePath, 0);
    }

    /**
     * 发布通知页面获取图片,尽量保证图片不被压缩
     *
     * @param imagePath
     * @param calculateType 尺寸压缩策略 0,压缩比往大一点的估算,1,四舍五入
     * @return
     */
    public static Bitmap getBitmap(String imagePath, int calculateType) {
        Bitmap bitmap = sampleCompress(imagePath, BITMAP_DEFAULT_WIDTH, BITMAP_DEFAULT_HEIGHT, calculateType);
        int degree = getBitmapDegree(imagePath);
        if (degree != 0) {
            bitmap = rotateBitmapByDegree(bitmap, degree);
        }
        return bitmap;
    }


    /**
     * 获得本地图片,获取过程中做了尺寸压缩
     *
     * @param imagePath     图片的本地路径
     * @param reqWidth      压缩后期望的宽
     * @param reqHeight     压缩后期望的高
     * @param calculateType 尺寸压缩策略
     * @return
     */
    public static Bitmap sampleCompress(String imagePath, int reqWidth, int reqHeight, int calculateType) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//      通过这个Bitmap获得图片的宽高
        BitmapFactory.decodeFile(imagePath, options);

//      计算压缩值
        int compressSize = calculateInSampleSize(options, reqWidth, reqHeight, imagePath, calculateType);
        LogOut.d(TAG, "图片的缩放值==" + compressSize);
        options.inSampleSize = compressSize;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
//      获得图片
        return BitmapFactory.decodeFile(imagePath, options);
    }


    /**
     * 计算图片尺寸的缩放值
     *
     * @param options
     * @param reqWidth      压缩后期望的宽
     * @param reqHeight     压缩后期望的高
     * @param filePath      图片文件路径
     * @param calculateType
     * @return 最小值为1, 表示不压缩;若为2,表示长宽各位原来的1/2;
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, String filePath, int calculateType) {
//        获取文件大小
//        long fileSize = FileSizeUtil.getFileSize(new File(filePath));
//        LogOut.d(TAG, "尺寸压缩前图片文件的大小==" + (fileSize / 1024) + "kb");
//        获取图片的长,宽
        int height = options.outHeight;
        int width = options.outWidth;
        if (height == 0 || width == 0) {
            try {
                // 从指定路径下读取图片，并获取其EXIF信息
                ExifInterface exifInterface = new ExifInterface(filePath);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (width > height) { //当图片的宽大于高时,交换宽,高的值
            int temp = height;
            height = width;
            width = temp;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            float valuation = ((calculateType == 0) ? 0.5f : 0);
            final int heightRatio = Math.round((float) height / (float) reqHeight + valuation);
            final int widthRatio = Math.round((float) width / (float) reqWidth + valuation);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize < 1 ? 1 : inSampleSize;
    }

    /**
     * 将bitmap转化为字符串,转化过程中进行质量压缩,默认为大于150KB则压缩
     *
     * @param image
     * @return
     */
    public static String bitmaptoString(Bitmap image) {
        return bitmaptoString(image, 150, Base64.NO_WRAP);
    }

    /**
     * 将bitmap转化为字符串,转化过程中进行质量压缩
     *
     * @param image
     * @param minImageSize 图片压缩的标准 ,KB
     * @param flags        BASE64压缩的格式
     * @return
     */
    public static String bitmaptoString(Bitmap image, int minImageSize, int flags) {
        if (image == null) {
            return null;
        }
        int quality = getQualityRate(getMomerySize(image), minImageSize);//根据图像的大小得到合适的有损压缩质量
        LogOut.d(TAG, "目前适用的有损压缩率是" + quality);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        LogOut.d(TAG, "上传时的图片大小==" + bytes.length / 1024 + "kb");
        String s = Base64.encodeToString(bytes, flags);
        return s;
    }

    /**
     * 需要多次上传的
     *
     * @param imgPath
     * @return
     */
//    public static String bitmaptoString(String imgPath, int minImageSize) {
//        long fileSize = FileSizeUtil.getFileSize(new File(imgPath));
//        LogOut.d(TAG, "质量压缩前的大小==" + (fileSize / 1024) + "kb");
//        String s = "";
//        if ((fileSize / 1024) > minImageSize) {
//            Bitmap image = getBitmap(imgPath);
//            int quality = getQualityRate(fileSize / 1024);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            LogOut.d(TAG, "目前适用的有损压缩率是" + quality);
//            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//            byte[] bytes = baos.toByteArray();
//            LogOut.d(TAG, "上传时的图片大小==" + bytes.length / 1024 + "kb");
//            s = Base64.encodeToString(bytes, Base64.NO_WRAP);
//            recycleBitmap(image);
//        } else {
//            LogOut.d(TAG, "不进行质量压缩");
//            s = Base64FileUtils.fileToBase64String(imgPath);
//        }
//        return s;
//    }


    /**
     * 将bitmap转化为字符串,并获取上传文件流的大小，转化过程中进行质量压缩
     *
     * @param image
     * @param minImageSize 图片压缩的标准 ,KB
     * @param flags        BASE64压缩的格式
     * @return
     */
    public static String bitmaptoStringAndSize(Bitmap image, int minImageSize, int flags) {

        int quality = getQualityRate(getMomerySize(image), minImageSize);//根据图像的大小得到合适的有损压缩质量
        LogOut.d(TAG, "目前适用的有损压缩率是" + quality);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        LogOut.d(TAG, "上传时的图片大小==" + bytes.length / 1024 + "kb");
        String s = Base64.encodeToString(bytes, flags) + "," + bytes.length / 1024;
        return s;
    }

    /**
     * 根据图像的大小得到合适的有损压缩质量，因为此时传入的bitmap大小已经比较合适了，靠近1000*1000，所以根据其内存大小进行质量压缩
     *
     * @param size
     * @return
     */
    public static int getQualityRate(int size, int minImageSize) {
        int mb = size >> 20;//除以100万，也就是m
        int kb = size >> 10;
        LogOut.d(TAG, "准备按照图像大小计算压缩质量，大小是" + kb + "KB,兆数是" + mb);
        if (mb > 20) {
            return 55;
        } else if (mb > 10) {
            return 65;
        } else if (mb > 3) {//目标压缩大小 100k，这里可根据实际情况来判断
            return 75;
        } else if (mb >= 2) {
            return 85;
        } else if (kb > 500) {
            return 95;
        } else {
            return 100;
        }
    }

    /**
     * @param fileSize
     * @return
     */
    public static int getQualityRate(long fileSize) {
        if (fileSize > 1000) {
            return 80;
        } else if (fileSize > 500) {
            return 90;
        }
        if (fileSize > 200) {
            return 95;
        } else {
            return 100;
        }
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @param minImageSize 图片压缩的标准,KB
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int minImageSize) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > minImageSize) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);


//        if (image!=null)
//            image.recycle();

        return bitmap;
    }


    /**
     * base64Data 转化成bitmap对象
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * base64Data 转化成bitmap对象
     *
     * @param base64Data base64编码数据
     * @param flag       BASE64压缩的格式
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data, int flag) {
        byte[] bytes = Base64.decode(base64Data, flag);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /***
     * 图片的缩放方法
     *
     * @param bitmap    ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap matrixCompress(Bitmap bitmap, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        float biggerSize = height > width ? height : width;
        float smallerSize = height < width ? height : width;
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / smallerSize;
        float scaleHeight = ((float) newHeight) / biggerSize;
        if (scaleWidth >= 1 || scaleHeight >= 1) {
            return bitmap;
        }
        float rate = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
        // 缩放图片动作
        matrix.postScale(rate, rate);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
        recycleBitmap(bitmap);
        return result;
    }

    /**
     * 回收bitmap图片
     *
     * @param bitmap
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 根据文件路径删除本地imageloder缓存数据
     *
     * @param imagePath
     */
    public static void deleteImagelogerCache(String imagePath) {
        DiskCacheUtils.removeFromCache("file://" + imagePath, ImageLoader.getInstance().getDiskCache());
        MemoryCacheUtils.removeFromCache("file://" + imagePath, ImageLoader.getInstance().getMemoryCache());
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 第三：图片按比例大小压缩方法（根据Bitmap图片压缩）：
     * 默认会调用质量比例压缩方法
     *
     * @param image
     * @param maxSize 图片目标大小 单位:KB
     * @return
     */
    public static Bitmap comp(Bitmap image, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        //        return compressImage(bitmap, maxSize);//压缩好比例大小后再进行质量压缩 // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    /**
     * 将bitmap转化为字符串,并获取上传文件流的大小，转化过程中进行质量压缩
     *
     * @param image
     * @param minImageSize 图片压缩的标准 ,KB
     * @param path         bitmap保存的文件路径名
     * @return
     */
    public static void bitmaptoFile(Bitmap image, int minImageSize, String path) {

        LogOut.d(TAG, "bitmap图片的大小==" + (getMomerySize(image) >> 10));
        int quality = getQualityRate(getMomerySize(image), minImageSize);//根据图像的大小得到合适的有损压缩质量
        LogOut.d(TAG, "目前适用的有损压缩率是" + quality);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        LogOut.d(TAG, "上传时的图片大小==" + bytes.length / 1024 + "kb");
        createFile(path, bytes);
    }

    //将byte数组写入文件
    public static void createFile(String path, byte[] content) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(content);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取图片文件的保存路径
     */
    public static File createImageSaveFile(String fileName) {
        String dir = KyeSys.getPhotoCachPath();
        return new File(dir, fileName + ".jpg");
    }

    /**
     * 保存图片到本地,并通知相册更新更新
     */
    public static void saveImage2Local(Context context, String imageCachePath, String saveFileName) {
        File imageSaveFile = createImageSaveFile(saveFileName);
        boolean isSuccess = KyeSys.fileInputAndOutPut(imageCachePath, imageSaveFile);
        if (isSuccess) {
            Intent scanImageIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(imageSaveFile);
            scanImageIntent.setData(uri);
            context.sendBroadcast(scanImageIntent);
            ToastUtils.showToastDefault(context,"保存图片成功");
        } else {
            ToastUtils.showToastDefault(context,"保存图片失败");
        }
    }

    /**
     * 保存图片到本地,并通知相册更新更新
     */
    public static void saveImageLocal(Context context, String imageCachePath, String saveFileName) {
        File imageSaveFile = createImageSaveFile(saveFileName);
        boolean isSuccess = KyeSys.fileInputAndOutPut(imageCachePath, imageSaveFile);
        if (isSuccess) {
            Intent scanImageIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(imageSaveFile);
            scanImageIntent.setData(uri);
            context.sendBroadcast(scanImageIntent);
        }
    }

    /**
     * 分享图片到其他应用(如QQ或微信)
     */
    public static void shareImage(Context context, String path) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/png");
            context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
        } else {
            ToastUtils.showToastDefault(context,"图片分享失败");
        }
    }

    /**
     * 保存图片到本地
     *
     * @param bm
     */
    public static void saveBitmap(Bitmap bm, String picName, String path) {
        try {
            File f = new File(path, picName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
//                Log.i(TAG, "已经保存");
        } catch (Exception e) {
        }
    }

    /**
     * 保存View为图片的方法
     */
    public static boolean saveBitmap(View v, String name) {
        String fileName = name + ".jpg";
        Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        v.draw(canvas);
        File f = new File(KyeSys.getPhotoCachPath(), fileName);
        if (f.exists()) {
            return true;
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存View为图片的方法
     */
    public static boolean saveBitmap(View v, String name, Context context) {
        String fileName = name + ".jpg";
        Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        v.draw(canvas);
        File f = new File(KyeSys.getPhotoCachPath(), fileName);
        if (f.exists()) {
            return true;
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Intent scanImageIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(f);
            scanImageIntent.setData(uri);
            context.sendBroadcast(scanImageIntent);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将Bimap保存为缓存文件中
     *
     * @param context
     * @param FileName
     * @param bitmap
     * @return 缓存文件的路径
     */
    public static String saveCacheImage(Context context, String FileName, Bitmap bitmap) {
        boolean isSuccess = true;
        File cacheDir = FileUtils.createCacheDir(context);
        File file = new File(cacheDir, FileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            isSuccess = false;
        } catch (IOException e) {
            isSuccess = false;
        } catch (Exception e) {
            isSuccess = false;
        }
        if (isSuccess) {
            return file.getPath();
        } else {
            return "";
        }

    }

    /**
     * 将Bimap保存为缓存文件中
     *
     * @param bitmap
     * @return 缓存文件的路径
     */
    public static boolean saveCacheImage(File file, Bitmap bitmap) {
        boolean isSuccess = true;
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            isSuccess = false;
        } catch (IOException e) {
            isSuccess = false;
        } catch (Exception e) {
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 判断图片是存在本地，否则进行存储并返回路径
     *
     * @param context
     * @param imageId
     * @param imageName
     */
    public static String saveBitmap(Context context, int imageId, String imageName) {
        try {
            String path = KyeSys.getPhotoCachPath();
            File file = new File(path, imageName);
            if (!file.exists()) {
                ImageUtils.saveBitmap(BitmapFactory.decodeResource(context.getResources(), imageId), imageName, path);
            }

            return file.getPath();
        } catch (Exception e) {
        }
        return "";
    }


    public static byte[] getBitmapByte(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } else {
            return null;
        }
    }

}
