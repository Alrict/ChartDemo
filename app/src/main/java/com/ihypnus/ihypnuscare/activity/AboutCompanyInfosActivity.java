package com.ihypnus.ihypnuscare.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 关于界面
 * @date: 2018/6/12 14:05
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class AboutCompanyInfosActivity extends BaseActivity {
    @Override
    protected int setView() {
        return R.layout.activity_about_company_infos;
    }

    @Override
    protected void findViews() {
        TextView tv_verson = (TextView) findViewById(R.id.tv_verson);
        String appVersionCode = getAPPVersionCode(this);
        tv_verson.setText(appVersionCode);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getResources().getString(R.string.tv_about));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }


    /**
     * 获取apk的版本号 currentVersionCode
     *
     * @param ctx
     * @return
     */
    private String getAPPVersionCode(Context ctx) {
        int currentVersionCode = 0;
        String appVersionName = "";
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }
}
