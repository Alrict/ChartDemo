package com.ihypnus.ihypnuscare.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.ClipActivity;
import com.ihypnus.ihypnuscare.activity.FeedbackActivity;
import com.ihypnus.ihypnuscare.activity.HelpCenterActivity;
import com.ihypnus.ihypnuscare.activity.PersonalInformationActivity;
import com.ihypnus.ihypnuscare.activity.SettingActivity;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.ImageUtils;
import com.ihypnus.ihypnuscare.utils.TakePhotosUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.kye.smart.multi_image_selector_library.MultiImageSelector;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.namee.permissiongen.PermissionGen;

import static android.app.Activity.RESULT_OK;

/**
 * @Package com.ihypnus.ihypnuscare.fragment
 * @author: llw
 * @Description: 我的fragment
 * @date: 2018/5/28 18:35
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class MyIhyFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mLayoutPersonInfo;
    private RelativeLayout mLayoutHelp;
    private RelativeLayout mLayoutSuggestions;
    private RelativeLayout mLayoutSettings;
    private CircleImageView mCircleImageView;
    private File mOutputImageFile;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CLIP = 3;
    private Bitmap mAvatarBitmap;
    private ImageView mIvDefaultPhoto;

    @Override
    protected int setView() {
        return R.layout.fragment_my_ihy;
    }

    @Override
    protected void findViews() {
        mCircleImageView = (CircleImageView) findViewById(R.id.avatar);
        mIvDefaultPhoto = (ImageView) findViewById(R.id.iv_default_photo);
        mLayoutPersonInfo = (RelativeLayout) findViewById(R.id.layout_person_info);
        mLayoutHelp = (RelativeLayout) findViewById(R.id.layout_help);
        mLayoutSuggestions = (RelativeLayout) findViewById(R.id.layout_suggestions);
        mLayoutSettings = (RelativeLayout) findViewById(R.id.layout_settings);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initEvent() {
        mCircleImageView.setOnClickListener(this);
        mLayoutPersonInfo.setOnClickListener(this);
        mLayoutHelp.setOnClickListener(this);
        mLayoutSuggestions.setOnClickListener(this);
        mLayoutSettings.setOnClickListener(this);

    }

    @Override
    protected void loadData() {
        BaseDialogHelper.showLoadingDialog(mAct,true,"正在加载...");
        IhyRequest.getDeviceListInfos(Constants.JSESSIONID, true, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.layout_person_info:
                jumpToActivity(101, PersonalInformationActivity.class);
                break;

            case R.id.layout_help:
                jumpToActivity(102, HelpCenterActivity.class);
                break;

            case R.id.layout_suggestions:
                jumpToActivity(103, FeedbackActivity.class);
                break;

            case R.id.layout_settings:
                jumpToActivity(104, SettingActivity.class);
                break;
            case R.id.avatar:
                //头像
                createSelectDialog();

                break;
        }

    }

    /**
     * 拍照或相册选择
     */
    private void createSelectDialog() {

        WindowManager wm = (WindowManager) mAct.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        View dialogview = LayoutInflater.from(mAct).inflate(R.layout.dialog_select, null);
        final Dialog selectDialog = new Dialog(mAct, R.style.upload_image_methods_dialog);
        selectDialog.setContentView(dialogview);
        Window window = selectDialog.getWindow();
        WindowManager.LayoutParams dialogParams = window.getAttributes();
        dialogParams.gravity = Gravity.BOTTOM;
        dialogParams.width = width;
        window.setAttributes(dialogParams);

        // 相机拍照
        dialogview.findViewById(R.id.get_from_cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//检查相机权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestCameraPermission();
                } else {
                    //拍照
                    mOutputImageFile = TakePhotosUtils.takePicture(mAct, REQUEST_CAMERA);
                }
                selectDialog.dismiss();
            }
        });

        // 从相册中选择
        dialogview.findViewById(R.id.get_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelector selector = MultiImageSelector.create();
                selector.showCamera(false);
                selector.count(1);
                selector.multi();
                selector.origin(null);
                selector.start(mAct, REQUEST_GALLERY);
                selectDialog.dismiss();
            }
        });
        // 取消
        dialogview.findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
            }
        });
        selectDialog.show();
    }


    /**
     * 申请相机权限
     */
    private void requestCameraPermission() {

        PermissionGen.with(mAct)
                .addRequestCode(100)
                .permissions(Manifest.permission.CAMERA)
                .request();
        Log.i("llw", "相机权限未被授予，需要申请！");
        // 相机权限未被授予，需要申请！
        ActivityCompat.requestPermissions(mAct, new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA);

    }

    private void jumpToActivity(int requestCode, Class<?> cls) {
        Intent intent = new Intent(mAct, cls);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    //个人资料

                    break;

                case 102:
                    //帮助中心

                    break;

                case 103:
                    //意见反馈

                    break;

                case 104:
                    //设置

                    break;
                case REQUEST_CAMERA:
                    if (mOutputImageFile == null || !mOutputImageFile.exists()) {
                        return;
                    }

                    Intent clipIntent = new Intent(mAct, ClipActivity.class);
                    clipIntent.putExtra("data", mOutputImageFile.getAbsolutePath());
                    startActivityForResult(clipIntent, REQUEST_CLIP);
                    break;
                case REQUEST_GALLERY:
                    ArrayList<String> selectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    Intent intent = new Intent(mAct, ClipActivity.class);
                    if (selectPath != null && selectPath.size() > 0) {
                        intent.putExtra("data", selectPath.get(0));
                    }
                    startActivityForResult(intent, REQUEST_CLIP);
                    break;
                case REQUEST_CLIP:
                    if (data != null) {
                        String imagePath = data.getStringExtra("images_path");
                        mAvatarBitmap = ImageUtils.getBitmap(imagePath);
                        mCircleImageView.setImageBitmap(mAvatarBitmap);
                        mIvDefaultPhoto.setVisibility(View.GONE);
                        final Bitmap bitmap = mAvatarBitmap;
//                        final String guid = mCardDetailBean.getGuid();
//                        mCircleImageView.setImageBitmap(bitmap);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final String base64 = ImageUtils.bitmaptoString(bitmap);
//                                uploadAvatar(base64, guid);
//                                ImageLoader.getInstance().displayImage(url, mAvatar, options, new SimpleImageLoadingListener() {
//
//                                    @Override
//                                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                                        mLoadedBitmap = bitmap;
//                                        showQrCode(bitmap);
//                                    }
//
//                                    @Override
//                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                                        mLoadedBitmap = null;
//                                    }
//                                });
                            }
                        }).start();
                    }

                    break;
            }
        }
    }

    /**
     * 申请权限的回调，
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults 多个权限一起返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意相机权限
                //拍照
                mOutputImageFile = TakePhotosUtils.takePicture(mAct, REQUEST_CAMERA);
            } else {
                //拒接,再次申请权限
                requestCameraPermission();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
