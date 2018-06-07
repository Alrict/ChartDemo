package com.ihypnus.ihypnuscare;

import android.app.Application;
import android.graphics.Bitmap;

import com.ihypnus.ihypnuscare.net.NetRequestHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * @Package com.ihypnus.ihypnuscare
 * @author: llw
 * @Description:
 * @date: 2018/5/16 17:17
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class IhyApplication extends Application {

    public static IhyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // 初始化新网络框架请求
        NetRequestHelper.getInstance().init(this);
        initImageLoadConfig(this);
    }

    /**
     * 初始化Imageloader
     */
    private DisplayImageOptions initImageLoadConfig(Application app) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub) // 设置图片下载期间显示的图片
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//像将完全按比例缩小的目标大小
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.mipmap.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中 一定要设置缓存磁盘，不然base64图片不显示
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration.Builder build =
                new ImageLoaderConfiguration.Builder(app)
                        .defaultDisplayImageOptions(options)
                        .threadPriority(Thread.MAX_PRIORITY)
                        .memoryCache(new WeakMemoryCache())
                        .denyCacheImageMultipleSizesInMemory()
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .diskCacheSize(200 * 1024 * 1024)  //  200 Mb sd卡(本地)缓存的最大值
                        .diskCacheFileCount(200); // 可以缓存的文件数量

//                .imageDownloader(new CustomerImageDownloader(this))
        if (BuildConfig.DEBUG) {
            // 是否打印log日志
            build.writeDebugLogs();
        }
        ImageLoaderConfiguration config = build.build();
        ImageLoader.getInstance().init(config);
        return options;

    }

}
