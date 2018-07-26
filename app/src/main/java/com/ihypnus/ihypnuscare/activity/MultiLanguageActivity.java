package com.ihypnus.ihypnuscare.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.multilanguage.CommSharedUtil;
import com.ihypnus.multilanguage.LanguageType;
import com.ihypnus.multilanguage.MultiLanguageUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 多语言设置
 * @date: 2018/6/12 14:03
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class MultiLanguageActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mLayoutSimplifiedChinese;
    private RelativeLayout mLayoutTraditionalChinese;
    private RelativeLayout mLayoutEnglish;
    private ImageView mIvSimplifiedChinese;
    private ImageView mIvTraditionalChinese;
    private ImageView mIvEnglish;
    private int savedLanguageType;
    private RelativeLayout mLayoutFollowsytem;
    private ImageView mIvFollowsystem;

    @Override
    protected int setView() {
        return R.layout.activity_multi_language;
    }

    @Override
    protected void findViews() {

        mLayoutFollowsytem = findViewById(R.id.layout_followsytem);
        mIvFollowsystem = findViewById(R.id.iv_followsystem);

        mLayoutSimplifiedChinese = findViewById(R.id.layout_simplified_chinese);
        mIvSimplifiedChinese = findViewById(R.id.iv_simplified_chinese);

        mLayoutTraditionalChinese = findViewById(R.id.layout_traditional_chinese);
        mIvTraditionalChinese = findViewById(R.id.iv_traditional_chinese);

        mLayoutEnglish = findViewById(R.id.layout_english);
        mIvEnglish = findViewById(R.id.iv_english);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(R.string.setting_language_title);
        savedLanguageType = MultiLanguageUtil.getInstance().getLanguageType();
        if (savedLanguageType == LanguageType.LANGUAGE_FOLLOW_SYSTEM) {
            setFollowSytemVisible();
        } else if (savedLanguageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            setTraditionalVisible();
        } else if (savedLanguageType == LanguageType.LANGUAGE_EN) {
            setEnglishVisible();
        } else if (savedLanguageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            setSimplifiedVisible();
        } else {
            setSimplifiedVisible();
        }
    }

    //简体中文
    private void setSimplifiedVisible() {
        mIvSimplifiedChinese.setVisibility(View.VISIBLE);
        mIvTraditionalChinese.setVisibility(View.INVISIBLE);
        mIvEnglish.setVisibility(View.INVISIBLE);
        mIvFollowsystem.setVisibility(View.INVISIBLE);
    }

    //英文
    private void setEnglishVisible() {
        mIvSimplifiedChinese.setVisibility(View.INVISIBLE);
        mIvTraditionalChinese.setVisibility(View.INVISIBLE);
        mIvEnglish.setVisibility(View.VISIBLE);
        mIvFollowsystem.setVisibility(View.INVISIBLE);
    }

    //繁体中文
    private void setTraditionalVisible() {
        mIvSimplifiedChinese.setVisibility(View.INVISIBLE);
        mIvTraditionalChinese.setVisibility(View.VISIBLE);
        mIvFollowsystem.setVisibility(View.INVISIBLE);
        mIvEnglish.setVisibility(View.INVISIBLE);
    }

    //跟随系统
    private void setFollowSytemVisible() {
        mIvFollowsystem.setVisibility(View.VISIBLE);
        mIvSimplifiedChinese.setVisibility(View.INVISIBLE);
        mIvTraditionalChinese.setVisibility(View.INVISIBLE);
        mIvEnglish.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initEvent() {
        mLayoutFollowsytem.setOnClickListener(this);
        mLayoutSimplifiedChinese.setOnClickListener(this);
        mLayoutTraditionalChinese.setOnClickListener(this);
        mLayoutEnglish.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        int selectedLanguage = 0;
        switch (v.getId()) {
            case R.id.layout_followsytem:
                setSimplifiedVisible();
                selectedLanguage = LanguageType.LANGUAGE_FOLLOW_SYSTEM;
                switchLanguage(Locale.CHINA);

                break;
            case R.id.layout_simplified_chinese:
                setSimplifiedVisible();
                selectedLanguage = LanguageType.LANGUAGE_CHINESE_SIMPLIFIED;
                switchLanguage(Locale.SIMPLIFIED_CHINESE);

                break;

            case R.id.layout_traditional_chinese:
                setTraditionalVisible();
                selectedLanguage = LanguageType.LANGUAGE_CHINESE_TRADITIONAL;
                switchLanguage(Locale.TRADITIONAL_CHINESE);
                break;

            case R.id.layout_english:
                setEnglishVisible();
                selectedLanguage = LanguageType.LANGUAGE_EN;
                switchLanguage(Locale.ENGLISH);
                break;
        }
//        MultiLanguageUtil.init(getApplicationContext());
//        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        CommSharedUtil.getInstance(this).putInt(MultiLanguageUtil.SAVE_LANGUAGE, selectedLanguage);
        EventBus.getDefault().post(new BaseFactory.UpdateLanguageEvent());
        finish();
    }

    public void switchLanguage(Locale locale) {
        Constants.LANGUAGE_TYPE = locale;

        Configuration config = getResources().getConfiguration();// 获得设置对象
        Resources resources = getResources();// 获得res资源对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale; // 简体中文
        resources.updateConfiguration(config, dm);
    }

//    private void onChangeAppLanguage(String newLanguage) {
//        MultiLanguageUtil.getInstance().changeAppLanguage(getActivity(), newLanguage);
//        AppLanguageUtils.changeAppLanguage(App.getContext(), newLanguage);
//        getActivity().recreate();
//    }
}
