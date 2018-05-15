package com.ihypnus.ihypnuscare.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/***
 * 用于检测Android具体信息工具辅助 类
 *
 * @author chenjy
 */
public class AndroidSystemHelper {

    private static final String TAG = "AndroidSystemHelper";

    //    /**
    //     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
    //     *
    //     * @param context
    //     * @return true 表示开启
    //     */
    //    public static final boolean isOPen(final Context context) {
    //        LocationManager locationManager = (LocationManager) context
    //                .getSystemService(Context.LOCATION_SERVICE);
    //        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
    //        boolean gps = locationManager
    //                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    //        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
    //        boolean network = locationManager
    //                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    //        if (gps || network) {
    //            return true;
    //        }
    //
    //        return false;
    //    }

    //    /**
    //     * 强制帮用户打开GPS
    //     *
    //     * @param context
    //     */
    //    public static final void openGPS(Context context) {
    //        Intent GPSIntent = new Intent();
    //        GPSIntent.setClassName("com.android.settings",
    //                "com.android.settings.widget.SettingsAppWidgetProvider");
    //        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
    //        GPSIntent.setData(Uri.parse("custom:3"));
    //        try {
    //            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
    //        } catch (CanceledException e) {
    //            e.printStackTrace();
    //        }
    //    }

    //    /**
    //     * 4.0后强制开启GPS代码： //4.0以上不允许发送广播 会报错! 此方法待考虑
    //     *
    //     * @param context
    //     */
    //    @SuppressWarnings("deprecation")
    //    public static final void turnGPSOn(Context context) {
    //        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
    //        intent.putExtra("enabled", true);
    //        context.sendBroadcast(intent);
    //
    //        String provider = Settings.Secure.getString(
    //                context.getContentResolver(),
    //                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    //        if (!provider.contains("gps")) { // if gps is disabled
    //            final Intent poke = new Intent();
    //            poke.setClassName("com.android.settings",
    //                    "com.android.settings.widget.SettingsAppWidgetProvider");
    //            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
    //            poke.setData(Uri.parse("3"));
    //            context.sendBroadcast(poke);
    //        }
    //    }

    //    /**
    //     * 获取当前应用的版本号：versionCode
    //     *
    //     * @throws Exception
    //     */
    //    public static int getPackageVersionCode(Context context) {
    //        // 获取packagemanager的实例
    //        PackageManager packageManager = context.getPackageManager();
    //        // getPackageName()是你当前类的包名，0代表是获取版本信息
    //        PackageInfo packInfo;
    //        int versionCode = 0;
    //        try {
    //            packInfo = packageManager.getPackageInfo(context.getPackageName(),
    //                    0);
    //            versionCode = packInfo.versionCode;
    //        } catch (NameNotFoundException e) {
    //            e.printStackTrace();
    //        }
    //        return versionCode;
    //    }

    /**
     * 获取当前进程名称
     *
     * @param context 上下文
     * @return 进程名称
     */
    public static String getProcessName(Context context) {

        int                                         myPid       = android.os.Process.myPid();
        ActivityManager                             am          = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == myPid) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    /**
     * 获取当前应用的版本号名称：versionName
     *
     * @throws NameNotFoundException
     * @throws Exception
     */
    public static final String getPackageVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo    = null;
        String      versionName = "";
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (NameNotFoundException e) {
            LogOut.e("NameNotFoundException", "getPackageVersionName: " + e.getMessage());
        }
        return versionName;
    }


    /**
     * 客户端软件版本名
     *
     * @param context
     * @return
     */
    public static String getClientVersionName(Context context) {
        return AndroidSystemHelper.getPackageVersionName(context);
    }

    /**
     * 获取当前应用的版本号：versionCode
     *
     * @throws Exception
     */
    //    public static int getPackageVersionCode() {
    //        int versionCode = 0;
    //        try {
    //            // 获取packagemanager的实例
    //            PackageManager packageManager = GlobalApplication.instance.getPackageManager();
    //            // getPackageName()是你当前类的包名，0代表是获取版本信息
    //            PackageInfo packInfo;
    //            packInfo = packageManager.getPackageInfo(GlobalApplication.instance.getPackageName(), 0);
    //            versionCode = packInfo.versionCode;
    //        } catch (NameNotFoundException e) {
    //            LogOut.e(TAG, e.getMessage());
    //        } catch (Exception e) {
    //            LogOut.e(TAG, e.getMessage());
    //        }
    //        return versionCode;
    //
    //    }

    //    /**
    //     * 获取AndroidManifest.xml配置数据
    //     *
    //     * @param context
    //     * @param metaDataName
    //     * @return
    //     */
    //    public static String getMetaData(Context context, String metaDataName) {
    //
    //        Object value = "";
    //        PackageManager packageManager = context.getPackageManager();
    //        ApplicationInfo applicationInfo;
    //        try {
    //            applicationInfo = packageManager.getApplicationInfo(
    //                    context.getPackageName(), 128);
    //            if (applicationInfo != null && applicationInfo.metaData != null) {
    //                value = applicationInfo.metaData.get(metaDataName);
    //            }
    //        } catch (NameNotFoundException e) {
    //            throw new RuntimeException(
    //                    "Could not read the name in the manifest file.", e);
    //        }
    //        if (value == null) {
    //            throw new RuntimeException("The name '" + metaDataName
    //                    + "' is not defined in the manifest file's meta data.");
    //        }
    //        LogOut.d("CHANNEL_MARK", "CHANNEL_MARK:" + value.toString());
    //        return (value == null) ? "0" : value.toString();
    //    }
    @SuppressWarnings("unused")
    public static int getScreenHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager()
               .getDefaultDisplay()
               .getMetrics(metric);
        int   width      = metric.widthPixels; // 屏幕宽度（像素）
        int   height     = metric.heightPixels; // 屏幕高度（像素）
        float density    = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        int   densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        return height;
    }

    @SuppressWarnings("unused")
    public static int getScreenWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager()
               .getDefaultDisplay()
               .getMetrics(metric);
        int   width      = metric.widthPixels; // 屏幕宽度（像素）
        int   height     = metric.heightPixels; // 屏幕高度（像素）
        float density    = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        int   densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        return width;
    }

    //    /**
    //     * 让系统扫描内存和sdcard卡 更新媒体库
    //     *
    //     * @param filePath
    //     * @param context
    //     */
    //    public static void scanPhotos(String[] filePath, final Context context) {
    //
    //        MediaScannerConnection.scanFile(context, filePath, null,
    //                new MediaScannerConnection.OnScanCompletedListener() {
    //                    @Override
    //                    public void onScanCompleted(String path, Uri uri) {
    //                        LogOut.d("ScanPhotos", "path: " + path + "\t uri: "
    //                                + uri);
    //                    }
    //                });
    //    }


    /**
     * 获取图片屏幕对应的DP值
     *
     * @param dp
     * @param context
     * @return
     */
    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                               dp,
                                               context.getResources()
                                                      .getDisplayMetrics());
    }

    // 调用隐藏系统默认的输入法
    public static void HideKeyboard(Activity mContext) {
        try {
            ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    mContext.getCurrentFocus()
                            .getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            LogOut.printStackTrace(e);
        }

    }

    // 显示虚拟键盘
    public static void ShowKeyboard(View v) {

        InputMethodManager imm = (InputMethodManager) v.getContext()
                                                       .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    // 隐藏虚拟键盘
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                                                       .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 获取Sim卡的号码
     * （如果获取不到，则获取当前登录用户的电话号码）
     */
    //    public static String getTelephoneNumber(Context context) {
    //        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    //        if (ActivityCompat.checkSelfPermission(context,
    //                                               Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
    //                context,
    //                Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
    //                context,
    //                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
    //        {
    //            // TODO: Consider calling
    //            //    ActivityCompat#requestPermissions
    //            // here to request the missing permissions, and then overriding
    //            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //            //                                          int[] grantResults)
    //            // to handle the case where the user grants the permission. See the documentation
    //            // for ActivityCompat#requestPermissions for more details.
    //            return TODO;
    //        }
    //        String tel      = manager.getLine1Number();
    //        int    simState = manager.getSimState();
    //        if (simState == TelephonyManager.SIM_STATE_READY && !TextUtils.isEmpty(tel)) {
    //            return tel;
    //        } else {
    //            UserInfo userInfo = GlobalApplication.instance.getUserInfo();
    //            if (null != userInfo) {
    //                return userInfo.getTelePhone();
    //            }
    //        }
    //        return null;
    //    }

    /**
     * 获取参数的鉴权串
     *
     * @return
     */
    //    public static String getIdentifier(Map<String, String> params) {
    //        if (params == null) {
    //            return null;
    //        } else {
    //            Set keySet = params.keySet();
    //            String[] keys = new String[keySet.size()];
    //            params.keySet().toArray(keys);
    //            Arrays.sort(keys);
    //            StringBuilder sb = new StringBuilder();
    //            sb.append(HttpRequest.K);
    //            String[] var5 = keys;
    //            int var6 = keys.length;
    //
    //            for (int var7 = 0; var7 < var6; ++var7) {
    //                String key = var5[var7];
    //                String value = params.get(key);
    //                if (!TextUtils.isEmpty(value.trim())) {
    //                    sb.append(key + value);
    //                }
    //            }
    //            return InternalUtils.encryptionMD5(sb.toString()).toUpperCase();
    //        }
    //    }

    //    /**
    //     * 获取手机唯一标识
    //     *
    //     * @return
    //     */
    //    public static String getMobileID(Context context) {
    //        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    //        String tmDevice = tm.getDeviceId();
    //        return tmDevice;
    //    }


    //    /**
    //     * 获取手机相关配置信息
    //     *
    //     * @param context
    //     * @return
    //     */
    //    public static String getMobileInfo(Context context) {
    //        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    //        StringBuilder sb = new StringBuilder();
    //        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
    //        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
    //        sb.append("\nLine1Number = " + tm.getLine1Number());
    //        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
    //        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
    //        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
    //        sb.append("\nNetworkType = " + tm.getNetworkType());
    //        sb.append("\nPhoneType = " + tm.getPhoneType());
    //        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
    //        sb.append("\nSimOperator = " + tm.getSimOperator());
    //        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
    //        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
    //        sb.append("\nSimState = " + tm.getSimState());
    //        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
    //        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
    //        return sb.toString();
    //    }

    /**
     * 判断当前网络是否可用
     **/
    public static boolean isNetWorkEnable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext()
                                                                   .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 判断当前Wifi是否可用
     **/
    public static boolean isWifiEnable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext()
                                                                   .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo mWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi == null || !mWifi.isConnected()) {
            return false;
        }
        return true;
    }

    //    /**
    //     * 获取库Phon表字段
    //     **/
    //    public static final String[] PHONES_PROJECTION = new String[]{
    //            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    //
    //    /**
    //     * 联系人显示名称
    //     **/
    //    public static final int PHONES_DISPLAY_NAME_INDEX = 0;
    //
    //    /**
    //     * 电话号码
    //     **/
    //    public static final int PHONES_NUMBER_INDEX = 1;
    //
    //    /**
    //     * 头像ID
    //     **/
    //    public static final int PHONES_PHOTO_ID_INDEX = 2;
    //
    //    /**
    //     * 联系人的ID
    //     **/
    //    public static final int PHONES_CONTACT_ID_INDEX = 3;

    //    /**
    //     * 联系人名称
    //     **/
    //    private ArrayList<String> mContactsName = new ArrayList<String>();
    //
    //    /**
    //     * 联系人头像
    //     **/
    //    private ArrayList<String> mContactsNumber = new ArrayList<String>();
    //
    //    /**
    //     * 联系人头像
    //     **/
    //    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();


    private String KEY_TIME = "time";

    private final int time = 15 * 24 * 60 * 60 * 1000;
    //    private final int time = 60 * 1000;
    //    private boolean dateIsOK() {
    //        long preTime = KyeSys.getLong(KEY_TIME, 0);
    //
    //        long currentTime = Calendar.getInstance().getTimeInMillis();
    //
    //        if (currentTime - preTime < time) {
    //            return false;
    //        }
    //        return true;
    //    }

    //    /**
    //     * 获取联系人Cursor
    //     *
    //     * @param baseActivity
    //     * @return
    //     */
    //    public static Cursor getPhoneCursor(Activity baseActivity) {
    //        ContentResolver resolver = null;
    //        Cursor phoneCursor = null;
    //        try {
    //
    //            resolver = baseActivity.getContentResolver();
    //            phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
    //                    PHONES_PROJECTION, null, null, null);
    //        } catch (Exception e) {
    //            LogOut.e("Exception ","getPhoneCursor: "+e.getMessage());
    //        }
    //        return phoneCursor;
    //    }

    //    /**
    //     * 更新允许访问通讯录状态
    //     *
    //     * @param baseActivity
    //     */
    //    public static int updateAllowContFlag(BaseActivity baseActivity) {
    //        Cursor phoneCursor = getPhoneCursor(baseActivity);
    //        if (phoneCursor == null) {
    //            baseActivity.getKyeApplication().allowContFlag = 0;
    //        } else {
    //            if (phoneCursor.getCount() == 0) {
    //                baseActivity.getKyeApplication().allowContFlag = 0;
    //            } else {
    //                baseActivity.getKyeApplication().allowContFlag = 1;
    //            }
    //        }
    //        return baseActivity.getKyeApplication().allowContFlag;
    //    }

    //    public void saveFile(String text) {
    //        File dir = new File(Environment.getExternalStorageDirectory() + "/kye_express/");
    //        if (!dir.exists()) {
    //            dir.mkdirs();
    //        }
    //
    //        FileOutputStream fos = null;
    //        File file = new File(dir.getPath() + "/key_contact.txt");
    //        if (!file.exists()) {
    //            try {
    //                file.createNewFile();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //        try {
    //            fos = new FileOutputStream(file.getPath(), false);
    //            fos.write(text.getBytes());
    //            fos.flush();
    //            fos.close();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //            if (fos != null) {
    //                try {
    //                    fos.close();
    //                } catch (IOException e1) {
    //                    e1.printStackTrace();
    //                }
    //            }
    //        }
    //    }

    //    //打开系统联系人，查找
    //    public static void LauncherContactSelector(Activity act) {
    //        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.ContactsContract.Contacts.CONTENT_URI);
    //        act.startActivityForResult(intent, 1);
    //    }


    /**
     * 获取设备Mac地址
     */
    //    public static String getMacAddress() {
    //        String macAddress = null;
    //        WifiManager wifiManager =
    //                (WifiManager) GlobalApplication.instance.getBaseContext().getSystemService(Context.WIFI_SERVICE);
    //        WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());
    //
    ////        if (wifiManager!=null && !wifiManager.isWifiEnabled()) {
    ////            //必须先打开，才能获取到MAC地址
    ////            wifiManager.setWifiEnabled(true);
    ////            wifiManager.setWifiEnabled(false);
    ////        }
    //        if (null != info) {
    //            macAddress = info.getMacAddress();
    //        }
    //        return macAddress;
    //    }


    /**
     * Display metrics display metrics.
     *
     * @param context the context
     * @return the display metrics
     */
    public static DisplayMetrics displayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay()
                     .getMetrics(dm);
        return dm;
    }

    /**
     * 复制文本到剪切板
     *
     * @param text
     */
    public static void copyText(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(text);
    }

    /**
     * 通过读取设备的ROM版本号、厂商名、CPU型号和其他硬件信息来组合出一串15位的号码
     *
     * @return
     */
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL")
                                .get(null)
                                .toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获得APP包名
     *
     * @param context
     * @return
     */
    public static String getAppPackage(Context context) {
        PackageInfo info;
        String      packageNames = "";
        try {
            info = context.getPackageManager()
                          .getPackageInfo(context.getPackageName(), 0);
            // 当前版本的包名
            packageNames = info.packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageNames;
    }

    /**
     * 通过文件名得到图片ID
     *
     * @param context
     * @param resImgNames 图片文件名 如 ic_launcher
     * @param fileName    文件名 如 drawable或 mipmap
     * @return 如果返回0则没有找到图片
     */
    public static int resId(Context context, String resImgNames[], String fileName) {
        int resID = 0;
        for (int i = 0; i < resImgNames.length; i++) {
            resID = context.getResources()
                           .getIdentifier(resImgNames[i], fileName, getAppPackage(context));
        }
        return resID;
    }

    /**
     * 通过文件名得到图片ID
     *
     * @param context
     * @param resImgName 图片文件名 如 ic_launcher
     * @param fileName   文件名 如 drawable或 mipmap
     * @return 如果返回0则没有找到图片
     */
    public static int resId(Context context, String resImgName, String fileName) {
        int resID = context.getResources()
                           .getIdentifier(resImgName, fileName, getAppPackage(context));
        return resID;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param telephoneNum
     */
    public static void call(Context context, String telephoneNum) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telephoneNum));
            context.startActivity(intent);
        } catch (SecurityException e) {
            LogOut.printStackTrace(e);

        }

    }

    /**
     * 获取textView内容行数
     *
     * @param tv      textView
     * @param content 需要测量的内容
     * @return 返回行数
     */
    public static int getLineCount(TextView tv, String content, int textViewWidth) {
        TextPaint    paint        = tv.getPaint();
        StaticLayout staticLayout = new StaticLayout(content,
                                                     paint,
                                                     textViewWidth,
                                                     Layout.Alignment.ALIGN_NORMAL,
                                                     1.0f,
                                                     0.0f,
                                                     false);
        return staticLayout.getLineCount();
    }

    /**
     * 获取 这个app的启动 intent  com.sdu.didi.gui
     *
     * @param c           上下文
     * @param packageName app包名
     * @return Intent
     */
    public static Intent getIntent(Context c, String packageName) {
        Intent intent = c.getPackageManager()
                         .getLaunchIntentForPackage(packageName);
        return intent;
    }

    /**
     * 跳转到权限设置界面
     */
    public static void gotoAppDetailSetting(Activity context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    public static final  String SYS_EMUI                       = "sys_emui";
    public static final  String SYS_MIUI                       = "sys_miui";
    private static final String KEY_MIUI_VERSION_CODE          = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME          = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE      = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL             = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION               = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    public static String getSystem() {
        String SYS = "";
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(
                    KEY_MIUI_VERSION_NAME,
                    null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null)
            {
                SYS = SYS_MIUI;//小米
            } else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null || prop.getProperty(
                    KEY_EMUI_VERSION,
                    null) != null || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null)
            {
                SYS = SYS_EMUI;//华为
            }
        } catch (IOException e) {
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

}
