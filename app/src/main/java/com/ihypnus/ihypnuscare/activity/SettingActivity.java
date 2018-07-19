package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ihypnus.ihypnuscare.IhyApplication;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 设置
 * @date: 2018/6/7 11:32
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvMultiLanguage;
    private ImageView mIvAbout;
    private RelativeLayout mLayoutMultiLanguage;
    private RelativeLayout mLayoutAbout;
    private Button mBtnLoginOut;
    private RelativeLayout mLayoutChangePwd;

    @Override
    protected int setView() {
//        EventBus.getDefault().register(this);
        return R.layout.activity_setting;
    }

    @Override
    protected void findViews() {
        mLayoutMultiLanguage = findViewById(R.id.layout_multi_language);
        mIvMultiLanguage = findViewById(R.id.iv_multi_language);
        mLayoutAbout = findViewById(R.id.layout_about);
        mIvAbout = findViewById(R.id.iv_about);
        mLayoutChangePwd = findViewById(R.id.layout_change_pwd);
        mBtnLoginOut = findViewById(R.id.btn_login_out);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(R.string.tv_setting);
    }

    @Override
    protected void initEvent() {
        mLayoutMultiLanguage.setOnClickListener(this);
        mLayoutAbout.setOnClickListener(this);
        mLayoutChangePwd.setOnClickListener(this);
        mBtnLoginOut.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.layout_multi_language:
                jumpToActivity(MultiLanguageActivity.class, 0);
                break;

            case R.id.layout_about:
                jumpToActivity(AboutCompanyInfosActivity.class, 0);
                break;
            case R.id.layout_change_pwd:
                jumpToActivity(ChanedPassWordActivity.class, 0);
                break;
            case R.id.btn_login_out:
                BaseDialogHelper.showNormalDialog(SettingActivity.this, getResources().getString(R.string.tip_msg), getString(R.string.tv_exit_current_account), getString(R.string.ok), getString(R.string.cancel), new DialogListener() {
                    @Override
                    public void onClick(BaseType baseType) {
                        if (baseType == BaseType.NO) {
                            loginOut();
                        }
                    }

                    @Override
                    public void onItemClick(long postion, String s) {

                    }
                });
                break;
        }
    }

    /**
     * 是否退出登录
     */
    private void loginOut() {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.tv_logout_loading));
        IhyRequest.userLogout(Constants.JSESSIONID, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                Volley.me.removeInitRequestHead("Accept");
                EventBus.getDefault().post(new BaseFactory.CloseAllEvent());
                IhyApplication.mInstance.setUser(null);
                jumpToActivity(LoginActivity.class, 1);
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    private void jumpToActivity(final Class<?> cls, int type) {
        if (type == 0) {
            Intent intent = new Intent(SettingActivity.this, cls);
            startActivity(intent);
        } else {
            BaseDialogHelper.showSimpleDialog(this, null, getString(R.string.tv_reset_pwd_succee), getString(R.string.ok), new DialogListener() {
                @Override
                public void onClick(BaseType baseType) {
                    Intent intent = new Intent(SettingActivity.this, cls);
                    startActivity(intent);
                }

                @Override
                public void onItemClick(long postion, String s) {

                }
            });
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseFactory.UpdateLanguageEvent event) {
        LogOut.d("llw", "SettingActivity页面更新了语言");
        ViewUtils.updateViewLanguage(findViewById(android.R.id.content));
    }

}
