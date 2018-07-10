package com.ihypnus.ihypnuscare;

import android.app.Application;
import android.graphics.Bitmap;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.ihypnus.ihypnuscare.bean.LoginBean;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.net.NetRequestHelper;
import com.ihypnus.ihypnuscare.utils.HttpLog;
import com.ihypnus.ihypnuscare.utils.KyeSys;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.multilanguage.MultiLanguageUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;
import com.wenming.library.LogReport;
import com.wenming.library.save.imp.CrashWriter;
import com.wenming.library.util.LogUtil;


/**
 * @Package com.ihypnus.ihypnuscare
 * @author: llw
 * @Description:
 * @date: 2018/5/16 17:17
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class IhyApplication extends Application {

    public static IhyApplication mInstance;
    private boolean isDebug = BuildConfig.LOG_DEBUG;
    private LoginBean loginBean;
    public static OSS mOssClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //初始化多语言设置
        MultiLanguageUtil.init(this);
        // 初始化新网络框架请求
        NetRequestHelper.getInstance().init(this);
        initImageLoadConfig(this);
        initVariable();
        initLogReport();
//        initOSSClient();
    }

    private void initOSSClient() {
        String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
        String stsServer = "hypnus-app-resource.oss-cn-shanghai.aliyuncs.com";
//推荐使用OSSAuthCredentialsProvider。token过期可以及时更新
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        mOssClient = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
    }

    /**
     * 初始化相关变量
     */
    private void initVariable() {

        boolean isDebug = true;

        // 设置设置相关debug变量
        LogUtil.isDebug = isDebug;
        VolleyLog.DEBUG = isDebug;
        HttpLog.LOG_FLAG = isDebug;
        LogOut.DEBUG = isDebug;
    }

    /**
     * 本地报错日志
     */
    private void initLogReport() {

        if (isDebug) {
            //debug模式下启动本地报错日志保存
            LogReport.getInstance()
                    .setCacheSize(30 * 1024 * 1024)//支持设置缓存大小，超出后清空
                    .setLogDir(mInstance, KyeSys.getKrashPath() + "/")//定义路径为：sdcard/[app name]/
                    .setWifiOnly(false)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
                    .setLogSaver(new CrashWriter(mInstance))//支持自定义保存崩溃信息的样式
//                    .setEncryption(new AES()) //支持日志到AES加密或者DES加密，默认不开启
                    .init(mInstance);
        } else {
            //初始化腾讯MTA(用户操作行为/月活量等数据)
//            initMTA(mInstance);
            CrashReport.initCrashReport(mInstance);
        }

    }

    private void initMTA(Application app) {
        try {
            boolean isSuccess = StatService.startStatService(app, "id", StatConstants.VERSION);
            LogOut.d("MTA", "MTA初始化==" + isSuccess);
        } catch (MtaSDkException e) {
            LogOut.d("MTA", "MTA初始化失败" + e);
        }
    }

    public void setUser(LoginBean loginBean) {
        this.loginBean = loginBean;
        if (loginBean == null) {
            return;
        }
        String jsessionid = loginBean.getJSESSIONID();
        Constants.JSESSIONID = jsessionid;
        LoginBean.UserInfoBean userInfo = loginBean.getUserInfo();
        if (userInfo != null) {
            String defaultDeviceId = userInfo.getDefaultDeviceId();
            Constants.DEVICEID = defaultDeviceId;
        }
        Volley.me.addInitRequestHead("Cookie", "JSESSIONID=" + jsessionid);
    }

    public LoginBean getUserInfo() {
        return loginBean;
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
