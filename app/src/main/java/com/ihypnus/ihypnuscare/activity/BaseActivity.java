package com.ihypnus.ihypnuscare.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.actionbar.ActionBar;
import com.ihypnus.ihypnuscare.actionbar.ContentView;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StatusBarUtil;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    /**
     * activity根布局
     */
    private View mRootView;
    /**
     * 是否显示actionBar
     * 请在方法{@link #preCreate} 初始化这个变量
     */
    protected boolean mShowActionBar = true;
    private ContentView mContentView;
    private ActionBar mActionBar;
    /**
     * 内容背景设置为透明 true为透明 默认  false
     */
    private boolean mContentBgIsTransparent = false;


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
        setActionBarEvent();

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
        EventBus.getDefault().register(this);
    }

    public void setStatusBar(int color) {
//        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.title_layout_color));
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(color));
    }

    @Override
    public void setContentView(int layoutResID) {
        mRootView = View.inflate(this, R.layout.activity_base, null);
        if (mShowActionBar) {
            inflateActionBar();
        }
        ViewGroup baseContent = (ViewGroup) mRootView.findViewById(R.id.base_content);
        mContentView = (ContentView) mRootView.findViewById(R.id.base_content);
        LayoutInflater.from(this).inflate(layoutResID, baseContent);
        super.setContentView(mRootView);
        initContentBg(baseContent);
    }

    public ActionBar getSupportedActionBar() {
        if (mActionBar == null && mRootView != null) {
            inflateActionBar();
        }
        return mActionBar;
    }

    private void inflateActionBar() {
        if (mRootView != null) {
            ViewStub viewStubActionBar = (ViewStub) mRootView.findViewById(R.id.view_stub);
            viewStubActionBar.inflate();
            mActionBar = (ActionBar) mRootView.findViewById(R.id.action_bar);
        }
    }

    /**
     * 初始化内容背景
     */
    private void initContentBg(View contentView) {
        if (contentView == null) {
            return;
        }
        if (mContentBgIsTransparent) {
            contentView.setBackgroundResource(R.color.transparent);
        } else {
            contentView.setBackgroundResource(R.color.default_background_color);
        }
    }

    //************************************ Actionbar

    protected void onActionBarItemClick(View view, int position) {
        if (position == ActionBar.LEFT_ITEM) {
            back();
        }
    }

    protected void setLeftActionItem(int drawableId) {
        getSupportedActionBar().setLeftDrawable(drawableId);
    }

    protected void setRightActionItem(int drawableId) {
        getSupportedActionBar().setRightDrawable(drawableId);
        getSupportedActionBar().getRightView().setEnabled(true);
    }

    protected void setRightActionText(int textId) {
        getSupportedActionBar().setRightText(textId);
        getSupportedActionBar().getRightView().setEnabled(true);
    }

    protected void setRightActionText(String text) {
        getSupportedActionBar().setRightText(text);
        getSupportedActionBar().getRightView().setEnabled(true);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        getSupportedActionBar().setTitle(title);
    }

    @Override
    public void setTitle(@StringRes int strId) {
        super.setTitle(strId);
        getSupportedActionBar().setTitle(strId);
    }


    /**
     * 设置标题栏左上角的文案，左上角的icon可见
     */
    public void setLeftText(String text) {
        TextView mLeftTitleText = getSupportedActionBar().getLeftText();
        mLeftTitleText.setVisibility(View.VISIBLE);
        mLeftTitleText.setText("返回");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = (int) getResources().getDimension(R.dimen.w100);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mLeftTitleText.setLayoutParams(layoutParams);
    }


    /**
     * 设置actionBar相关事件
     */
    private void setActionBarEvent() {
        if (mActionBar != null && mShowActionBar) {
            mActionBar.setOnActionBarListener(new ActionBar.OnActionBarListener() {
                public void onActionBarItemClicked(int position) {
                    if (position == ActionBar.LEFT_ITEM) {
                        onActionBarItemClick(getSupportedActionBar().getLeftView(), position);
                    } else if (position == ActionBar.RIGHT_ITEM) {
                        onActionBarItemClick(getSupportedActionBar().getRightView(), position);
                    } else if (position == ActionBar.MIDDLE_ITEM) {
                        onActionBarItemClick(getSupportedActionBar().getMiddleView(), position);
                    }
                }
            });
        }
    }

    protected void back() {
        try {
            onBackPressed();
            showInput(false);
        } catch (Exception e) {
            LogOut.printStackTrace(e);
        }
    }

    /**
     * 是否关闭键盘
     *
     * @param show
     */
    public void showInput(boolean show) {
        try {
            if (show) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(this.getCurrentFocus().getApplicationWindowToken(), 0);
            } else {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(), 0);
            }
        } catch (NullPointerException e1) {

        } catch (Exception e) {
        }
    }

    /**
     * 防止防止快速点击事件
     */
    @Override
    public void onBackPressed() {
        if (ViewUtils.isFastDoubleClick(300)) {
            return;
        }

        if (!isFinishing()) {
            super.onBackPressed();
        } else {
            finish();
        }

    }

    @Subscribe
    public void onEventMainThread(BaseFactory.CloseAllEvent event) {
        finish();
    }

/*    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }*/

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseFactory.UpdateLanguageEvent event) {
        LogOut.d("llw", "baseActivity页面更新了语言");
        ViewUtils.updateViewLanguage(findViewById(android.R.id.content));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
