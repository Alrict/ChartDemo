package com.ihypnus.ihypnuscare.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;

/**
 * loadingDialog管理
 */
public class LoadingDialogHelper {


    private static Dialog dialog;

    /**
     * 显示没有回调dialog
     *
     * @param context  上下文
     * @param canAbort 是否可以撤销
     * @param msg      显示内容
     */
    public static void showLoadingDialog(Context context, boolean canAbort, String msg) {
        showCallBackLoadingDialog(context, canAbort, msg, null);
    }

    /**
     * 拥有回调的dialog
     *
     * @param context  上线文
     * @param canAbort 是否可以撤销
     * @param msg      显示内容
     * @param listener 回调监听
     */
    public static void showCallBackLoadingDialog(Context context, boolean canAbort, String msg, DialogInterface.OnCancelListener listener) {
        if (null != dialog && !((Activity) context).isFinishing()) {
            dialog.dismiss();
        }
        dialog = createLoadingDialog(context, canAbort, msg);
        if (listener != null) {
            dialog.setOnCancelListener(listener);
        }
        dialog.show();
    }

    /**
     * 撤销loadingdialog
     */
    public static void dismissLoadingDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            LogOut.printStackTrace(e);
        } finally {
            dialog = null;
        }
    }

    /**
     * 创建一个请求时的加载动画
     *
     * @param context
     * @param canAbort
     * @return
     */
    public static Dialog createLoadingDialog(Context context, boolean canAbort, String msg) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        if (!StringUtils.isNullOrEmpty(msg)) {
            dialog.setLoadingText(msg);
        }
        dialog.setCancelable(canAbort);
        return dialog;
    }

}
