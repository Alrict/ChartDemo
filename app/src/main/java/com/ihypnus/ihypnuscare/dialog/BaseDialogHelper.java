package com.ihypnus.ihypnuscare.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.widget.NormalDialog;

import java.lang.ref.WeakReference;


public class BaseDialogHelper {
    private static WeakReference<Dialog> mDialog;
    private NumberInputListener mOnNumberInputListener;

    public static NormalDialog showNormalDialog(Context context,
                                                String title,
                                                CharSequence message,
                                                String left,
                                                String right, final DialogListener listener) {

        NormalDialog normalDialog = new NormalDialog(context).setmCannelText(left).setmConfirmText(right).setmTileText(title).setmContent(message);
        normalDialog.setNormalDialogListenner(new DialogListener() {
            @Override
            public void onClick(BaseType baseType) {
                if (baseType == BaseType.NO) {
                    listener.onClick(BaseType.NO);
                } else if (baseType == BaseType.OK) {
                    listener.onClick(BaseType.OK);
                }
            }

            @Override
            public void onItemClick(long postion, String s) {

            }
        });
        normalDialog.setCanceledOnTouchOutside(false);
        normalDialog.show();
        return normalDialog;
    }

    /**
     * 小人动画弹窗
     *
     * @param context
     * @param canAbort
     * @param msg
     */
    public static void showLoadingDialog(Context context, boolean canAbort, String msg) {
        if (mDialog != null && null != getDialog() && context != null && !((Activity) context).isFinishing()) {
            getDialog().dismiss();
        }
        Dialog dialog = LoadingDialogHelper.createLoadingDialog(context, canAbort, msg);
        mDialog = new WeakReference<>(dialog);
        dialog.show();
    }


    /**
     * 小人动画弹窗
     *
     * @param context
     * @param canAbort
     * @param msg
     */
    public static void showLoadingDialog(Context context, boolean canAbort, String msg, DialogInterface.OnCancelListener listener) {
        if (mDialog != null && null != getDialog() && context != null && !((Activity) context).isFinishing()) {
            getDialog().dismiss();
        }
        Dialog dialog = LoadingDialogHelper.createLoadingDialog(context, canAbort, msg);
        dialog.setOnCancelListener(listener);
        mDialog = new WeakReference<>(dialog);
        dialog.show();
    }

    /**
     * 销毁掉小人动画弹窗
     */
    public static void dismissLoadingDialog() {
        try {
            if (mDialog != null && mDialog.get() != null && getDialog().isShowing()) {
                getDialog().dismiss();
            }
        } catch (Exception e) {
            LogOut.printStackTrace(e);
        } finally {
            mDialog = null;
        }
    }

    public static Dialog getDialog() {
        return mDialog.get();
    }


    public static void showSimpleDialog(Context context, final String mTitle, final String mMessage) {
        showSimpleDialog(context, mTitle, mMessage, "", null);
    }


    public static void showSimpleDialog(Context context, final String mTitle, final String mMessage, final String ButtonName, final DialogListener listener) {
        IhyBaseDialog kyeBaseDialog = IhyBaseDialog.createKyeBaseDialog(context, R.layout.layout_normal_simple_dialog, new IhyBaseDialog.DialogListener() {
            @Override
            public void bindView(View view, final IhyBaseDialog kyeBaseDialog) {
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView message = (TextView) view.findViewById(R.id.content);
                Button btnOk = (Button) view.findViewById(R.id.button);
                if (!TextUtils.isEmpty(ButtonName)) {
                    btnOk.setText(ButtonName);
                }
                title.setText(mTitle);
                message.setText(mMessage);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kyeBaseDialog.dismiss();
                        if (listener != null) {
                            listener.onClick(BaseType.OK);
                        }
                    }
                });
            }
        });
        kyeBaseDialog.setCancelable(false);
        kyeBaseDialog.setCanceledOnTouchOutside(false);
    }


    /**
     * 错误提示框
     *
     * @param context
     * @param msg
     */
    public static void showMsgTipDialog(Context context, final String msg) {
        IhyBaseDialog kyeBaseDialog = IhyBaseDialog.createKyeBaseDialog(context, R.layout.dialog_msg_tip, new IhyBaseDialog.DialogListener() {
            @Override
            public void bindView(View view, final IhyBaseDialog kyeBaseDialog) {

                TextView message = (TextView) view.findViewById(R.id.content);
                Button btnOk = (Button) view.findViewById(R.id.button);

                message.setText(msg);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kyeBaseDialog.dismiss();
                    }
                });
            }
        });
        kyeBaseDialog.setCancelable(false);
        kyeBaseDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 输入数字的弹窗
     *
     * @param context
     * @param mTitle
     * @param listener
     */
    public static void showInputNumberDialog(Context context, final String mTitle, final String errMsg, final NumberInputListener listener) {
        IhyBaseDialog kyeBaseDialog = IhyBaseDialog.createKyeBaseDialog(context, R.layout.layout_input_height_dialog, new IhyBaseDialog.DialogListener() {
            @Override
            public void bindView(View view, final IhyBaseDialog kyeBaseDialog) {
                TextView title = (TextView) view.findViewById(R.id.title);
                final EditText input = (EditText) view.findViewById(R.id.content);
                Button cancel = (Button) view.findViewById(R.id.cancel);
                Button confirm = (Button) view.findViewById(R.id.confirm);

                title.setText(mTitle);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kyeBaseDialog.dismiss();

                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number = input.getText().toString().trim();
                        if (!StringUtils.isNullOrEmpty(number)) {
                            kyeBaseDialog.dismiss();
                            if (listener != null) {
                                listener.onNumberInputListener(number);
                            }
                        } else {
                            ToastUtils.showToastDefault(errMsg);
                        }
                    }
                });
            }
        });
        kyeBaseDialog.setCancelable(false);
        kyeBaseDialog.setCanceledOnTouchOutside(false);
    }

    public static void showInputNameDialog(Context context, final String mTitle, final NumberInputListener listener) {
        IhyBaseDialog kyeBaseDialog = IhyBaseDialog.createKyeBaseDialog(context, R.layout.layout_input_dialog, new IhyBaseDialog.DialogListener() {
            @Override
            public void bindView(View view, final IhyBaseDialog kyeBaseDialog) {
                TextView title = (TextView) view.findViewById(R.id.title);
                final EditText input = (EditText) view.findViewById(R.id.content);
                Button cancel = (Button) view.findViewById(R.id.cancel);
                Button confirm = (Button) view.findViewById(R.id.confirm);

                title.setText(mTitle);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kyeBaseDialog.dismiss();

                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number = input.getText().toString().trim();

                        if (!StringUtils.isNullOrEmpty(number) ) {
                            kyeBaseDialog.dismiss();
                            if (listener != null) {
                                listener.onNumberInputListener(number);
                            }
                        } else {
                            ToastUtils.showToastDefault("请输入您的昵称");
                        }


                    }
                });
            }
        });
        kyeBaseDialog.setCancelable(false);
        kyeBaseDialog.setCanceledOnTouchOutside(false);
    }

    public interface NumberInputListener {
        void onNumberInputListener(String l);
    }

    private void setOnNumberInputListener(NumberInputListener listener) {
        mOnNumberInputListener = listener;
    }

}
