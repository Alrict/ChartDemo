package com.ihypnus.ihypnuscare.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.LogOut;


public class LoadingDialog extends Dialog {
    private static final String TAG = "LoadingDialog";
    private Animation mReLoadingAnim;
    private TextView loadingTv;
    private Activity mContext;
    private ImageView xlistview_header_progressbar;

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContext(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setContext(context);
    }

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
        setContext(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.progress_bar, null);
        loadingTv = (TextView) mView.findViewById(R.id.loading_tv);
        xlistview_header_progressbar = (ImageView) mView.findViewById(R.id.xlistview_header_progressbar);
        //旋转动画
        mReLoadingAnim = AnimationUtils.loadAnimation(context, R.anim.login_code_loading);
        LinearInterpolator lin = new LinearInterpolator();
        mReLoadingAnim.setInterpolator(lin);
        setContentView(mView);

    }


    /**
     * 开始播放
     */
    protected void animStart() {
        if (mReLoadingAnim != null) {
            xlistview_header_progressbar.startAnimation(mReLoadingAnim);
        }
    }

    /**
     * 停止播放
     */
    protected void animStop() {
        if (mReLoadingAnim != null) {
            xlistview_header_progressbar.clearAnimation();
        }
    }

    private void setContext(Context context) {
        if (context instanceof Activity) {
            mContext = (Activity) context;
        }
    }

    public void setLoadingText(String loadingText) {
        if (loadingText != null) {
            loadingTv.setText(loadingText);
        }
    }

    @Override
    public void show() {
        if (isActivityActive()) {
            try {
                animStart();
                super.show();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void dismiss() {

        if (isActivityActive()) {
            try {
                animStop();
                super.dismiss();
            } catch (Exception e) {
                LogOut.d(TAG, "error==" + e.toString());
            }
        }
    }

    protected boolean isActivityActive() {
        return mContext == null || !mContext.isFinishing();
    }
}
