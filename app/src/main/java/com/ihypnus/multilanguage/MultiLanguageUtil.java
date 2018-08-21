package com.ihypnus.multilanguage;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.config.ConstantLanguages;
import com.ihypnus.ihypnuscare.utils.LogOut;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Locale;

/**
 * 多语言切换的帮助类
 */
public class MultiLanguageUtil {

    private static final String TAG = "MultiLanguageUtil";
    private static MultiLanguageUtil instance;

    public static final String SAVE_LANGUAGE = "save_language";
    private static HashMap<String, Locale> mAllLanguages = new HashMap<String, Locale>(3) {{
        put(ConstantLanguages.ENGLISH, Locale.ENGLISH);
        put(ConstantLanguages.SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
        put(ConstantLanguages.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
    }};


    public static MultiLanguageUtil getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You must be init MultiLanguageUtil first");
        }
        return instance;
    }

//    private MultiLanguageUtil(Context context) {
//        this.mContext = context;
//    }

    public static Context attachBaseContext(Context context, Locale language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            Resources resources = context.getResources();
            Locale locale = getLocaleByLanguage(language);
            LogOut.d("llw", "android M以下版本中的locale:" + locale.toString());
            int type = 2;
            if (locale == Locale.ENGLISH) {
                LogOut.d("llw", "android M以下版本中的type:Locale.ENGLISH");
                type = 1;
            } else if (locale == Locale.TRADITIONAL_CHINESE) {
                LogOut.d("llw", "android M以下版本中的type:Locale.TRADITIONAL_CHINESE");
                type = 3;
            }
            CommSharedUtil.getInstance(context).putInt(MultiLanguageUtil.SAVE_LANGUAGE, type);
            Configuration configuration = resources.getConfiguration();
            LogOut.d("llw", "android M以下版本最终设置的语言类型:" + locale);
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, null);
            return context;
        }
    }

    /**
     * 设置语言类型
     */
    public static void setApplicationLanguage(Context context) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = getLanguageLocale(context);
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.getApplicationContext().createConfigurationContext(config);
            Locale.setDefault(locale);
        }
        resources.updateConfiguration(config, dm);
    }


    private static Context updateResources(Context context, Locale language) {
        Locale.setDefault(language);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(language);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = language;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
     /*   LogOut.d("llw", "updateResources中的语言类型:" + language.toString());
        Resources resources = context.getResources();
        Locale locale = getLocaleByLanguage(language);
        LogOut.d("llw", "updateResources中的locale:" + locale.toString());
        int type = 2;
        if (locale == Locale.ENGLISH) {
            type = 1;
        } else if (locale == Locale.TRADITIONAL_CHINESE) {
            type = 3;
        }
        CommSharedUtil.getInstance(context).putInt(MultiLanguageUtil.SAVE_LANGUAGE, type);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);*/
    }

    public static Locale getLocaleByLanguage(Locale language) {
        LogOut.d("llw", "getLocaleByLanguage中的语言类型:" + language.toString());
        if (language == Locale.ENGLISH || language == Locale.TRADITIONAL_CHINESE || language == Locale.SIMPLIFIED_CHINESE) {
            return language;
        } else {
            Locale locale = Locale.getDefault();
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

    private static boolean isSupportLanguage(Locale language) {
        return mAllLanguages.containsValue(language);
    }

    /**
     * 设置语言
     */

    public static void setConfiguration(Context context) {
        Locale targetLocale = getLanguageLocale(context);
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
//            configuration.setLocales(new LocaleList(targetLocale));
        } else {
            configuration.locale = targetLocale;
        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }


    /**
     * 如果不是英文、简体中文、繁体中文，默认返回简体中文
     *
     * @return
     */
    public static Locale getLanguageLocale(Context context) {
        int languageType = CommSharedUtil.getInstance(context).getInt(MultiLanguageUtil.SAVE_LANGUAGE, 0);
        LogOut.d("llw", "用户设置的语言类型:" + languageType);
        if (languageType == LanguageType.LANGUAGE_EN) {
            return Locale.ENGLISH;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            return Locale.TRADITIONAL_CHINESE;
        } /*else {
            return Locale.SIMPLIFIED_CHINESE;
        }*/
        return getSysLocale(context);

    }

    public static void onConfigurationChanged(Context context) {
        saveSystemCurrentLanguage(context);
        setLocal(context);
        setApplicationLanguage(context);
    }

    public static Context setLocal(Context context) {
        return updateResources(context, getLanguageLocale(context));
    }


    public static void saveSystemCurrentLanguage(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        Log.d(TAG, locale.getLanguage());
        int type = 2;
        if (locale == Locale.ENGLISH) {
            type = 1;
        } else if (locale == Locale.TRADITIONAL_CHINESE) {
            type = 3;
        }
        CommSharedUtil.getInstance(context).putInt(MultiLanguageUtil.SAVE_LANGUAGE, type);
    }

    private String getSystemLanguage(Locale locale) {
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    //以上获取方式需要特殊处理一下
    public static Locale getSysLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().locale;
        } else {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        }
        LogOut.d("llw", "未获取到用户设置语言,当前系统语言为:" + locale);
        return locale;
    }

    /**
     * 更新语言
     *
     * @param languageType
     */

    public static void updateLanguage(int languageType, Context context) {
        CommSharedUtil.getInstance(context).putInt(MultiLanguageUtil.SAVE_LANGUAGE, languageType);
        setConfiguration(context);
        EventBus.getDefault().post(new OnChangeLanguageEvent(languageType));
    }

    public String getLanguageName(Context context) {
        int languageType = CommSharedUtil.getInstance(context).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_EN) {
            return context.getString(R.string.setting_language_english);
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return context.getString(R.string.setting_simplified_chinese);
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            return context.getString(R.string.setting_traditional_chinese);
        }
        return context.getString(R.string.setting_language_auto);
    }

    /**
     * 获取到用户保存的语言类型
     *
     * @return
     */
    public static int getLanguageType(Context context) {
        int languageType = CommSharedUtil.getInstance(context).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return LanguageType.LANGUAGE_CHINESE_SIMPLIFIED;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            return LanguageType.LANGUAGE_CHINESE_TRADITIONAL;
        } else if (languageType == LanguageType.LANGUAGE_FOLLOW_SYSTEM) {
            return LanguageType.LANGUAGE_FOLLOW_SYSTEM;
        }
        Log.e(TAG, "getLanguageType" + languageType);
        return languageType;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            MultiLanguageUtil.getInstance().setConfiguration(context);
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getInstance().getLanguageLocale(context);
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
}
