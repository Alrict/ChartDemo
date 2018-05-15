package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;

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
public class SplashActivity
        extends BaseActivity
        implements View.OnClickListener
{

    private final static int REQUEST_CODE = 100;
    private ImageView mImSplash;
    private TextView  mTvJumpOver;
    private long      mStartTime;
    /**
     * 手动点击跳转到下个界面 当点击跳过按钮就设置为true
     */
    private boolean isJumpOver = false;

    @Override
    protected int setView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void findViews() {
        mImSplash = (ImageView) findViewById(R.id.im_splash);
        mTvJumpOver = (TextView) findViewById(R.id.tv_jump_over);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        // 防止重复开启界面
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        mStartTime = System.currentTimeMillis();
//        if (adInfo != null) {
//            //本地有缓存启动页图片，那么加载显示它
//            mInitAdinfo = adInfo;
//            loadCacheImage(adInfo.getSmallImg());
//            // TIME 秒后跳转到下个界面
//            //            startCountDown(TIME);
//        } else {
//            // 由于不用加载缓存图片
//            isLoadCacheImgComplete = true;
//            //显示本地默认图片,等待app初始化完后立即跳转到下一个界面
//            showJumpOverBtn(false);
//        }
        showJumpOverBtn(false);
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
                //                if (!AppInitHelper.getInstance().appIsInit()) {
                //                    return;
                //                }
                isJumpOver = true;
                jumpToNextActivity();
                break;
            case R.id.im_splash: //点击图片跳转到H5界面
                //                if (!AppInitHelper.getInstance().appIsInit()) {
                //                    return;
                //                }
                //                if (mInitAdinfo == null) {
                //                    return;
                //                }
                //                stopCountDown();
                //                jumpToActivity();
                //                if (PrefUtils.getSplashAdInfo() != null) {
                //                    mAdinfo = PrefUtils.getSplashAdInfo();
                //                }
                //                if (!StringUtils.isNullOrEmpty(mInitAdinfo.getUrl())) { //Url
                //                    KyeJumpManager.jumpMessageDetailActivity(SplashActivity.this, String.valueOf(mInitAdinfo.getId()), mInitAdinfo.getClassName(), mInitAdinfo.getAdName(), mInitAdinfo.getUrl(), mInitAdinfo.getAdName(), mInitAdinfo.getModule(), String.valueOf(mInitAdinfo.getClassid()));
                //                    isJumpH5Web = true;
                //                } else if (!StringUtils.isNullOrEmpty(mInitAdinfo.getAccessUrl())) { //accessUrl
                //                    KyeJumpManager.jumpMessageDetailActivity(SplashActivity.this, String.valueOf(mInitAdinfo.getId()), mInitAdinfo.getClassName(), mInitAdinfo.getAdName(), mInitAdinfo.getAccessUrl(), mInitAdinfo.getAdName(), mInitAdinfo.getModule(), String.valueOf(mInitAdinfo.getClassid()));
                //                    isJumpH5Web = true;
                //                } else {
                //                    mImSplash.setEnabled(false);
                //                }
                break;
        }
    }

    /**
     * 显示跳过按钮
     */
    private void showJumpOverBtn(boolean showBtn) {
//        if (showBtn && AppInitHelper.getInstance().appIsInit()) {
//            mTvJumpOver.setVisibility(View.VISIBLE);
//            mTvJumpOver.setEnabled(true);
//            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
//            alphaAnimation.setDuration(getResources().getInteger(R.integer.animation_default_duration));
//            mTvJumpOver.startAnimation(alphaAnimation);
//        } else {
//            mTvJumpOver.setVisibility(View.INVISIBLE);
//        }
    }

    /**
     * 跳转页面
     */
    private void jumpToNextActivity() {
        //        boolean isFirstInstall = KyeSys.getBoolean(KyeSys.FIRST_START_KEY, true);
        //        UserInfo userInfo = getUserInfo();
        //        LogOut.i(TAG, "第一次安装？->" + isFirstInstall);
        //        if (isFirstInstall || userInfo == null) {
        //            gotoLoginActivity();
        //        } else {
        //            if (userInfo.isLogin() && LoginHelper.getInstance().isCanOneKeyLogin()) {
        //                LogOut.i(TAG, "SplashActivity jumpTo HomeActivity");
        //                gotoHomeActivity();
        //            } else {
        //                gotoLoginActivity();
        //            }
        //        }
        gotoLoginActivity();
    }

    private void delayJumpToNext() {
        // App 初始化完成
        //        if (AppInitHelper.getInstance().appIsInit()) {
        //            long delayTime = 0;
        //            final int thousand = 1000;
        //            if (mInitAdinfo == null) {
        //                delayTime = (long) (TIME2 * thousand);
        //            } else {
        //                delayTime = TIME * thousand;
        //            }
        //            mHandler.sendEmptyMessageDelayed(SPLASH_TIME_IS_OVER, delayTime);
        //        }
    }

    //跳转到登录页面
    private void gotoLoginActivity() {
        Intent in = new Intent(this, LoginActivity.class);
        startActivity(in);
        finish();
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
