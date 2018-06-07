package com.ihypnus.ihypnuscare.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ihypnus.ihypnuscare.utils.LogOut;


/**
 * 自定义dialog集类
 */
public abstract class BaseDialog extends Dialog {

    private static final String TAG = "BaseDialog";
    private Activity mContext;

    public BaseDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContext(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        setContext(context);
    }

    public BaseDialog(Context context) {
        super(context);
        setContext(context);
    }

    private void setContext(Context context) {
        if (context instanceof Activity) {
            mContext = (Activity) context;
        }
    }

    @Override
    public void show() {
        if (isActivityActive()) {
            try {
                super.show();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void dismiss() {
        if (isActivityActive()) {
            try {
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
