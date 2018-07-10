# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembers class android.support.v7.widget.RecyclerView$ViewHolder {
   public final View *;
}
-dontwarn com.yanzhenjie.recyclerview.swipe.**

-keep class com.yanzhenjie.recyclerview.swipe.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn android.support.**
-ignorewarnings

#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
    void on*Event*(**);
}

#保护注解
-keepattributes *Annotation*
-keepattributes Signature

-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep public class * extends com.ihypnus.ihypnuscare.bean.BaseModel{
    *;
}

-keep public class com.tencent.smtt.utils.LogFileUtils {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLog {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLogClient {
	public <fields>;
	public <methods>;
}
#   阿里oss
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**

-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

-keep public class javax.**

-keep class * implements java.io.Serializable # 保持 Serializable 不被混淆

-keepclasseswithmembers class * {   # 保持EventBus中接收方法不被混淆
    public void onEventMainThread(*);
}


-keep public class * extends android.support.v4.app.Fragment {
    private void permission*();
}

##################友盟分享不混淆################
#SocialSDk-email
-dontwarn com.umeng.socialize.**
-keep class com.umeng.socialize.**{*;}
#SocialSDK-QQZone_1;
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}
#SocialSDK-Sina
-dontwarn com.sina.sso.**
-keep class com.sina.sso.**{*;}
##################imageloader不混淆################
#imageloader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.**{*;}
##################zxing扫码库不混淆################
#zxing.jar
-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{*;}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#v4/v7
-dontwarn android.support.**
-keep class android.support.**{*;}

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.FragmentActivity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化  不优化输入的类文件
-dontoptimize

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#忽略警告
-ignorewarning
#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

##################权限管理不混淆################
#permissiongen.jar
-dontwarn kr.co.namee.permissiongen.**
-keep class kr.co.namee.permissiongen.**{*;}

-keep @kr.co.namee.permissiongen.PermissionFail class * {*;}
-keep @kr.co.namee.permissiongen.PermissionSuccess class * {*;}
-keep class * {
    @kr.co.namee.permissiongen.PermissionFail <fields>;
    @kr.co.namee.permissiongen.PermissionSuccess <fields>;
}
-keepclassmembers class * {
    @kr.co.namee.permissiongen.PermissionFail <methods>;
    @kr.co.namee.permissiongen.PermissionSuccess <methods>;
}



