package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.SP;
import com.ihypnus.ihypnuscare.utils.StringUtils;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 启动页，app初始化在这个页面做，只有app初始化后才能进行Activity跳转
 * @date: 2018/5/14 13:43
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener {

    private final static int REQUEST_CODE = 100;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private ImageView mImSplash;
    private TextView mTvJumpOver;
    private long mStartTime;
    /**
     * 手动点击跳转到下个界面 当点击跳过按钮就设置为true
     */
    private boolean isJumpOver = false;
    private SP mSP;
    private CountDownTimer mCountDownTimer;
    private TextView mTvCountdown;

    @Override
    protected int setView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void findViews() {
        mImSplash = (ImageView) findViewById(R.id.im_splash);
        mTvCountdown = (TextView) findViewById(R.id.tv_countdown);
        mTvJumpOver = (TextView) findViewById(R.id.tv_jump_over);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getSupportedActionBar().setVisibility(View.GONE);
        // 防止重复开启界面
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        mStartTime = System.currentTimeMillis();
        // TIME 秒后跳转到下个界面
        startCountDown(2);
    }

    @Override
    protected void initEvent() {
        mTvJumpOver.setOnClickListener(this);
        mImSplash.setOnClickListener(this);
        mImSplash.setEnabled(false);
    }

    @Override
    protected void loadData() {
        // 权限控制
        // 权限申请 读写外部存储
        PermissionGen.with(this)
                .addRequestCode(REQUEST_CODE)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump_over:
                isJumpOver = true;
                jumpToNextActivity();
                break;
            case R.id.im_splash: //点击图片跳转到H5界面

                break;
        }
    }


    /**
     * 跳转页面
     */
    private void jumpToNextActivity() {
        gotoLoginActivity();
    }


    /**
     * 停止计时器
     */
    private void stopCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    /**
     * 开始倒计时
     */
    private void startCountDown(int time) {
        final int countDownInterval = 1000;
        mCountDownTimer = new CountDownTimer(time * countDownInterval + countDownInterval, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                long second = millisUntilFinished / countDownInterval;
                mTvCountdown.setText(second + "");
            }

            @Override
            public void onFinish() {
                LogOut.i(TAG, "onFinish");

                jumpToNextActivity();
            }
        };
        mCountDownTimer.start();
    }

    //跳转到登录页面
    private void gotoLoginActivity() {
        mSP = SP.getSP(Constants.LOGIN_ACCOUNT_PASSWORD);
        String login_account = mSP.getString(Constants.LOGIN_ACCOUNT);
        String login_password = mSP.getString(Constants.LOGIN_PASSWORD);
        if (!StringUtils.isNullOrEmpty(login_account) && !StringUtils.isNullOrEmpty(login_password)) {
            Intent in = new Intent(this, HomeActivity.class);
            startActivity(in);
            finish();
        } else {
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
            finish();
        }

    }

    public void jumpHomeActivity() {
        Intent in = new Intent(this, HomeActivity.class);
        startActivity(in);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (AppInitHelper.getInstance().appIsInit()) {
//                isJumpOver = true;
//            }
            jumpToNextActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 权限控制
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 权限控制
    @PermissionSuccess(requestCode = REQUEST_CODE)
    public void doSuccessRequestPermissions() {
        initAndLoadData();
    }

    // 权限控制
    @PermissionFail(requestCode = REQUEST_CODE)
    public void doFailRequestPermissions() {
//        showPermissionDialog(getString(R.string.storage));
    }

    /**
     * 显示权限对话框
     */
    private void showPermissionDialog(String permissionName) {
        final String title = "权限申请";
        final String content = "在设置-应用-跨越内部版-权限中开启" + permissionName + "权限，以保证功能的正常使用";
//        DialogHelper.showNormalDialog(this, title, content,
//                                      "去设置", "取消", 0, new DialogSimpleCallback() {
//                    @Override
//                    public void onButtonClick(int tag, DialogButtonType dialogButtonType, Object object) {
//                        DialogHelper.dismiss();
//                        if (dialogButtonType == DialogButtonType.OK) {
//                            AndroidSystemHelper.gotoAppDetailSetting(SplashActivity.this);
//                            finish();
//                        } else {
//                            finish();
//                        }
//                    }
//                }, null);
    }

    /**
     * 后台加载广告,并缓存在本地,并且app初始化
     */
    private void initAndLoadData() {
//        final long delayTimeInit = 0;
//        ThreadTask.getInstance().executorOtherThread(new Runnable() {
//            @Override
//            public void run() {
//                // 后台加载广告,并缓存在本地
//                getAd();
//            }
//        }, ThreadTask.ThreadPeriod.PERIOD_HIGHT);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 初始化app
//                AppInitHelper.getInstance().initApp(GlobalApplication.instance);
//                if (isLoadCacheImgComplete) {
//                    delayJumpToNext();
//                    if (mInitAdinfo != null) {
//                        showJumpOverBtn(true);
//                    } else {
//                        showJumpOverBtn(false);
//                    }
//                }
//            }
//        }, delayTimeInit);
    }
}
