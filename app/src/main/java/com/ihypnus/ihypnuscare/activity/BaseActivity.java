package com.ihypnus.ihypnuscare.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: Activity基类
 * @date: 2018/5/14 13:42
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
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


}
