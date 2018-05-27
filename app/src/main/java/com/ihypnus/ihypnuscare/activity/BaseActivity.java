package com.ihypnus.ihypnuscare.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: Activity基类
 * @date: 2018/5/14 13:42
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseActivity extends Activity {

    public SystemBarTintManager mTintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initState();
        setContentView(setView());


        findViews();
        init(savedInstanceState);
        initEvent();
        loadData();
    }

    /**
     * 设置activity的布局文件
     *
     * @return 布局文件的resId
     */
    protected abstract int setView();

    /**
     * 获取界面元素
     */
    protected abstract void findViews();

    /**
     * 对象的初始化工作
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 设置事件
     */
    protected abstract void initEvent();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * 设置状态栏
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
