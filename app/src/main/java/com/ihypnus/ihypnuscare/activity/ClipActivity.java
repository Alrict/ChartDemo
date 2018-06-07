package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ImageUtils;
import com.ihypnus.ihypnuscare.widget.ClipImageBorderView;
import com.ihypnus.ihypnuscare.widget.ClipZoomImageView;
import com.ihypnus.ihypnuscare.widget.ImageLoaderUrlGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description:
 * @date: 2018/6/7 17:34
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ClipActivity extends BaseActivity {

    private ClipZoomImageView mClipZoomImageView;
    private ProgressBar mProgressBar;
    private ClipImageBorderView mClipImageBorderView;
    private TextView mTvConfirm;

    @Override
    protected int setView() {
        return R.layout.activity_clip;
    }

    @Override
    protected void findViews() {
        mClipZoomImageView = (ClipZoomImageView) findViewById(R.id.cziv_img);
        mClipImageBorderView = (ClipImageBorderView) findViewById(R.id.cibv_clip);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //这步必须要加
        mClipZoomImageView.setHorizontalPadding((int) getResources().getDimension(R.dimen.w100));
        mClipImageBorderView.setHorizontalPadding((int) getResources().getDimension(R.dimen.w100));
    }

    @Override
    protected void initEvent() {

        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap headIcon = mClipZoomImageView.clip();
                String headIconPath = ImageUtils.saveCacheImage(ClipActivity.this, "head.jpg", headIcon);
                Intent intent = getIntent();
                intent.putExtra("images_path", headIconPath);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Intent intent = getIntent();
        String imgPath = intent.getStringExtra("data");
//        设置图片
        ImageLoaderUrlGenerator imageLoaderUrlGenerator = new ImageLoaderUrlGenerator();
        String url = imageLoaderUrlGenerator.getImageLoaderWrapUrl(imgPath);
        ImageLoader.getInstance().displayImage(url, mClipZoomImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                mProgressBar.setVisibility(View.GONE);
                mClipZoomImageView.setImageResource(R.mipmap.bg_ban);
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void loadData() {

    }
}
