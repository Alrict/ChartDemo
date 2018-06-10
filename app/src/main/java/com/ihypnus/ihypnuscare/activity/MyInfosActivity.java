package com.ihypnus.ihypnuscare.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ImageUtils;
import com.ihypnus.ihypnuscare.utils.TakePhotosUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.kye.smart.multi_image_selector_library.MultiImageSelector;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 个人资料
 * @date: 2018/6/7 11:29
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class MyInfosActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mLlPhoto;
    private CircleImageView mCircleImageView;
    private RelativeLayout mLlName;
    private TextView mTvPersonName;
    private RelativeLayout mLlGender;
    private TextView mTvPersonGender;
    private RelativeLayout mLlHeight;
    private TextView mTvPersonHeight;
    private RelativeLayout mLlBodyWeight;
    private TextView mTvPersonBodyWeight;
    private final String TAG = MyInfosActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CLIP = 3;
    private File mOutputImageFile;
    private Bitmap mAvatarBitmap;

    @Override
    protected int setView() {
        return R.layout.activity_my_infos;
    }

    @Override
    protected void findViews() {
        //头像
        mLlPhoto = (RelativeLayout) findViewById(R.id.ll_photo);
        mCircleImageView = (CircleImageView) findViewById(R.id.avatar);

        //昵称
        mLlName = (RelativeLayout) findViewById(R.id.ll_name);
        mTvPersonName = (TextView) findViewById(R.id.tv_person_name);

        //性别
        mLlGender = (RelativeLayout) findViewById(R.id.ll_gender);
        mTvPersonGender = (TextView) findViewById(R.id.tv_person_gender);

        //身高
        mLlHeight = (RelativeLayout) findViewById(R.id.ll_height);
        mTvPersonHeight = (TextView) findViewById(R.id.tv_person_height);

        //体重
        mLlBodyWeight = (RelativeLayout) findViewById(R.id.ll_body_weight);
        mTvPersonBodyWeight = (TextView) findViewById(R.id.tv_person_body_weight);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        getSupportedActionBar().setVisibility(View.GONE);
        setTitle("个人资料");
    }

    @Override
    protected void initEvent() {

        mCircleImageView.setOnClickListener(this);
        mTvPersonName.setOnClickListener(this);
        mTvPersonGender.setOnClickListener(this);
        mTvPersonHeight.setOnClickListener(this);
        mTvPersonBodyWeight.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {

            case R.id.avatar:
                //头像
                createSelectDialog();

                break;

            case R.id.tv_person_name:
                //昵称


                break;

            case R.id.tv_person_gender:
                //性别


                break;

            case R.id.tv_person_height:
                //身高


                break;

            case R.id.tv_person_body_weight:
                //体重


                break;
        }

    }

    /**
     * 拍照或相册选择
     */
    private void createSelectDialog() {

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        View dialogview = LayoutInflater.from(this).inflate(R.layout.dialog_select, null);
        final Dialog selectDialog = new Dialog(this, R.style.upload_image_methods_dialog);
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

                if (!ViewUtils.isFastDoubleClick()) {
                    mOutputImageFile = TakePhotosUtils.takePicture(MyInfosActivity.this, REQUEST_CAMERA);
                }
                selectDialog.dismiss();
            }
        });

        // 从相册中选择
        dialogview.findViewById(R.id.get_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelector selector = MultiImageSelector.create(MyInfosActivity.this);
                selector.showCamera(false);
                selector.count(1);
                selector.multi();
                selector.origin(null);
                selector.start(MyInfosActivity.this, REQUEST_GALLERY);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    if (mOutputImageFile == null || !mOutputImageFile.exists()) {
                        return;
                    }

                    Intent clipIntent = new Intent(MyInfosActivity.this, ClipActivity.class);
                    clipIntent.putExtra("data", mOutputImageFile.getAbsolutePath());
                    startActivityForResult(clipIntent, REQUEST_CLIP);
                    break;
                case REQUEST_GALLERY:
                    ArrayList<String> selectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    Intent intent = new Intent(MyInfosActivity.this, ClipActivity.class);
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
                        final Bitmap bitmap = mAvatarBitmap;
//                        final String guid = mCardDetailBean.getGuid();
                        mCircleImageView.setImageBitmap(bitmap);
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
                default:
                    break;
            }

        }
    }
}
