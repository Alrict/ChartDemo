package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

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

    @Override
    protected int setView() {
        return R.layout.activity_multi_language;
    }

    @Override
    protected void findViews() {
        mLayoutSimplifiedChinese = findViewById(R.id.layout_simplified_chinese);
        mIvSimplifiedChinese = findViewById(R.id.iv_simplified_chinese);

        mLayoutTraditionalChinese = findViewById(R.id.layout_traditional_chinese);
        mIvTraditionalChinese = findViewById(R.id.iv_traditional_chinese);

        mLayoutEnglish = findViewById(R.id.layout_english);
        mIvEnglish = findViewById(R.id.iv_english);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("语言设置");
    }

    @Override
    protected void initEvent() {
        mLayoutSimplifiedChinese.setOnClickListener(this);
        mLayoutTraditionalChinese.setOnClickListener(this);
        mLayoutEnglish.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        getDefaultLanguage();
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.layout_simplified_chinese:
                mIvSimplifiedChinese.setVisibility(View.VISIBLE);
                mIvTraditionalChinese.setVisibility(View.INVISIBLE);
                mIvEnglish.setVisibility(View.INVISIBLE);
                switchLanguage("zh_simple");

                break;

            case R.id.layout_traditional_chinese:
                mIvSimplifiedChinese.setVisibility(View.VISIBLE);
                mIvTraditionalChinese.setVisibility(View.INVISIBLE);
                mIvEnglish.setVisibility(View.INVISIBLE);
                switchLanguage("zh_traditional");
                break;

            case R.id.layout_english:
                mIvSimplifiedChinese.setVisibility(View.INVISIBLE);
                mIvTraditionalChinese.setVisibility(View.INVISIBLE);
                mIvEnglish.setVisibility(View.VISIBLE);
                switchLanguage("en");
                break;
        }
    }

    private void getDefaultLanguage() {
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (config.locale == Locale.TRADITIONAL_CHINESE) {
            mIvSimplifiedChinese.setVisibility(View.INVISIBLE);
            mIvTraditionalChinese.setVisibility(View.VISIBLE);
            mIvEnglish.setVisibility(View.INVISIBLE);
        } else if (config.locale == Locale.ENGLISH) {
            mIvSimplifiedChinese.setVisibility(View.INVISIBLE);
            mIvTraditionalChinese.setVisibility(View.INVISIBLE);
            mIvEnglish.setVisibility(View.VISIBLE);
        } else {
            mIvSimplifiedChinese.setVisibility(View.VISIBLE);
            mIvTraditionalChinese.setVisibility(View.INVISIBLE);
            mIvEnglish.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 切换语言
     *
     * @param language
     */

    private void switchLanguage(String language) {

        //设置应用语言类型

        Resources resources = getResources();

        Configuration config = resources.getConfiguration();

        DisplayMetrics dm = resources.getDisplayMetrics();

        if (language.equals("zh_simple")) {
            //简体中文
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals("en")) {
            //英文
            config.locale = Locale.ENGLISH;
        } else if (language.equals("zh_traditional")) {
            //繁体中文
            config.locale = Locale.TRADITIONAL_CHINESE;

        }

        resources.updateConfiguration(config, dm);

        //更新语言后，destroy当前页面，重新绘制

        finish();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // 杀掉进程
       android.os.Process.killProcess(android.os.Process.myPid());
       System.exit(0);


    }
}
