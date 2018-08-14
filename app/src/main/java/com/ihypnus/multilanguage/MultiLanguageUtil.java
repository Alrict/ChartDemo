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
    private Context mContext;
    public static final String SAVE_LANGUAGE = "save_language";
    private static HashMap<String, Locale> mAllLanguages = new HashMap<String, Locale>(3) {{
        put(ConstantLanguages.ENGLISH, Locale.ENGLISH);
        put(ConstantLanguages.SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
        put(ConstantLanguages.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
    }};

    public static void init(Context mContext) {
        if (instance == null) {
            synchronized (MultiLanguageUtil.class) {
                if (instance == null) {
                    instance = new MultiLanguageUtil(mContext);
                }
            }
        }
    }

    public static MultiLanguageUtil getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You must be init MultiLanguageUtil first");
        }
        return instance;
    }

    private MultiLanguageUtil(Context context) {
        this.mContext = context;
    }

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
    public void setApplicationLanguage(Context context) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = getLanguageLocale();
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

    @TargetApi(Build.VERSION_CODES.N)
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

    public void setConfiguration() {
        Locale targetLocale = getLanguageLocale();
        Configuration configuration = mContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
//            configuration.setLocales(new LocaleList(targetLocale));
        } else {
            configuration.locale = targetLocale;
        }
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setConfiguration(Context context) {
        Locale targetLocale = getLanguageLocale();
        LogOut.d("llw", "設置系統語言類型：" + targetLocale.toString());
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
            configuration.setLocales(new LocaleList(targetLocale));
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
    public Locale getLanguageLocale() {
        int languageType = CommSharedUtil.getInstance(mContext).getInt(MultiLanguageUtil.SAVE_LANGUAGE, 0);
        LogOut.d("llw", "语言类型" + languageType);
        if (languageType == LanguageType.LANGUAGE_FOLLOW_SYSTEM) {
            Locale sysLocale = getSysLocale();
            return sysLocale;
        } else if (languageType == LanguageType.LANGUAGE_EN) {
            return Locale.ENGLISH;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            return Locale.TRADITIONAL_CHINESE;
        }
        getSystemLanguage(getSysLocale());
        Log.e(TAG, "getLanguageLocale" + languageType + languageType);
        return Locale.SIMPLIFIED_CHINESE;
    }

    private String getSystemLanguage(Locale locale) {
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    //以上获取方式需要特殊处理一下
    public Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            locale = mContext.getResources().getConfiguration().locale;
        } else {
            locale = mContext.getResources().getConfiguration().getLocales().get(0);
        }
        return locale;
    }

    /**
     * 更新语言
     *
     * @param languageType
     */

    public void updateLanguage(int languageType) {
        CommSharedUtil.getInstance(mContext).putInt(MultiLanguageUtil.SAVE_LANGUAGE, languageType);
        MultiLanguageUtil.getInstance().setConfiguration();
        EventBus.getDefault().post(new OnChangeLanguageEvent(languageType));
    }

    public String getLanguageName(Context context) {
        int languageType = CommSharedUtil.getInstance(context).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_EN) {
            return mContext.getString(R.string.setting_language_english);
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return mContext.getString(R.string.setting_simplified_chinese);
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            return mContext.getString(R.string.setting_traditional_chinese);
        }
        return mContext.getString(R.string.setting_language_auto);
    }

    /**
     * 获取到用户保存的语言类型
     *
     * @return
     */
    public int getLanguageType() {
        int languageType = CommSharedUtil.getInstance(mContext).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
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
            MultiLanguageUtil.getInstance().setConfiguration();
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getInstance().getLanguageLocale();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
}
