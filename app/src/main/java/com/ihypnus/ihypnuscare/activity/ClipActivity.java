package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.actionbar.ActionBar;
import com.ihypnus.ihypnuscare.utils.ImageUtils;
import com.ihypnus.ihypnuscare.widget.ClipImageBorderView;
import com.ihypnus.ihypnuscare.widget.ClipZoomImageView;
import com.ihypnus.ihypnuscare.widget.ImageLoaderUrlGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
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
    private DisplayImageOptions mOptions;

    @Override
    protected int setView() {
        return R.layout.activity_clip;
    }

    @Override
    protected void findViews() {
        mClipZoomImageView = (ClipZoomImageView) findViewById(R.id.cziv_img);
        mClipImageBorderView = (ClipImageBorderView) findViewById(R.id.cibv_clip);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //这步必须要加
        mClipZoomImageView.setHorizontalPadding((int) getResources().getDimension(R.dimen.w100));
        mClipImageBorderView.setHorizontalPadding((int) getResources().getDimension(R.dimen.w100));
        setTitle("更换头像");
    }

    @Override
    protected void initEvent() {


        ActionBar bar = getSupportedActionBar();
        bar.setRightText("确定");
        bar.getRightText().setOnClickListener(new View.OnClickListener() {
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
        ImageLoader.getInstance().displayImage(url, mClipZoomImageView,mOptions, new SimpleImageLoadingListener() {
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
        // 设置图片下载期间显示的图片
//像将完全按比例缩小的目标大小
// 设置图片Uri为空或是错误的时候显示的图片
// 设置图片加载或解码过程中发生错误显示的图片
// 设置下载的图片是否缓存在内存中
// 设置下载的图片是否缓存在SD卡中  // 这个要设置为true
// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
// 创建配置过得DisplayImageOption对象
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub) // 设置图片下载期间显示的图片
                .imageScaleType(ImageScaleType.NONE)//像将完全按比例缩小的目标大小
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.mipmap.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(false) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中  // 这个要设置为true
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build();
    }
}
