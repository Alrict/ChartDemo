package com.ihypnus.ihypnuscare.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.StatusBarUtil;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: Activity基类
 * @date: 2018/5/14 13:42
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseActivity extends Activity {
    /**
     * 隐藏状态条 默认显示
     * 请在方法{@link #preCreate} 初始化这个变量
     */
    protected boolean mShowStatusBar = true;
    /**
     * 状态栏是否透明
     * 请在方法{@link #preCreate} 初始化这个变量
     */
    protected boolean mIsTransparentStatusBar = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preCreate();
        super.onCreate(savedInstanceState);
//        initState();
        setContentView(setView());


        findViews();
        init(savedInstanceState);
        initEvent();
        loadData();


        if (mShowStatusBar) {
            if (mIsTransparentStatusBar) {
                StatusBarUtil.setTranslucent(this);
            } else {
                setStatusBar(R.color.status_bar_default);
            }
        }
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

    /**
     * 此方法在Activity中方法onCreate之前调用此方法
     */
    protected void preCreate() {
    }

    public void setStatusBar(int color) {
//        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.title_layout_color));
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(color));
    }
}
