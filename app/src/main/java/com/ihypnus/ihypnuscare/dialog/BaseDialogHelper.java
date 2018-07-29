package com.ihypnus.ihypnuscare.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.contrarywind.view.WheelView;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.adapter.CommentAdapter;
import com.ihypnus.ihypnuscare.adapter.NormalStringAdapter;
import com.ihypnus.ihypnuscare.bean.UsageInfos;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.widget.NormalDialog;

import java.lang.ref.WeakReference;
import java.util.List;


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
                if (StringUtils.isNullOrEmpty(mTitle)) {
                    title.setVisibility(View.GONE);
                } else {
                    title.setText(mTitle);
                }
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

    public static void showNumberWheelDialog(final Context context, final int min, final int max, final int currentIndex, final String title, final NumberInputListener listener) {
        IhyBaseDialog kyeBaseDialog = IhyBaseDialog.createKyeBaseDialog(context, R.layout.layout_height_wheel_dialog, new IhyBaseDialog.DialogListener() {
            @Override
            public void bindView(View view, final IhyBaseDialog kyeBaseDialog) {
                TextView tvTitle = (TextView) view.findViewById(R.id.title);
                final WheelView wv = (WheelView) view.findViewById(R.id.wv);
                TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
                TextView tvOk = (TextView) view.findViewById(R.id.tv_ok);
                tvTitle.setText(title);
                wv.setTextColorCenter(Color.BLACK);
                NumericWheelAdapter adapter = new NumericWheelAdapter(min, max);
                wv.setAdapter(adapter);
                wv.setCyclic(true);// 可循环滚动
                wv.setCurrentItem(currentIndex);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kyeBaseDialog.dismiss();
                    }
                });

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kyeBaseDialog.dismiss();
                        if (listener != null) {
                            listener.onNumberSelectListener(min + wv.getCurrentItem());
                        }
                    }
                });


            }
        });
        kyeBaseDialog.setCancelable(false);
        kyeBaseDialog.setCanceledOnTouchOutside(false);
    }

    public static void showInputNameDialog(final Context context, final String mTitle, final NumberInputListener listener) {
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

                        if (!StringUtils.isNullOrEmpty(number)) {
                            kyeBaseDialog.dismiss();
                            if (listener != null) {
                                listener.onNumberInputListener(number);
                            }
                        } else {
                            ToastUtils.showToastDefault(context.getString(R.string.tv_toast_nickname));
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
        void onNumberSelectListener(int value);
    }

    private void setOnNumberInputListener(NumberInputListener listener) {
        mOnNumberInputListener = listener;
    }


    /**
     * listview样式的dialog
     *
     * @param context
     * @param mTitle
     * @param button
     * @param list
     */
    public static void showListDialog(final Context context, final String mTitle, final String button, final List<UsageInfos.UsetimesBean> list, final DialogListener listener) {
        IhyBaseDialog kyeBaseDialog = IhyBaseDialog.createKyeBaseDialog(context, R.layout.dialog_list, new IhyBaseDialog.DialogListener() {
            @Override
            public void bindView(View view, final IhyBaseDialog kyeBaseDialog) {
                TextView title = (TextView) view.findViewById(R.id.title);
                ListView lv = (ListView) view.findViewById(R.id.lv);
                Button btn = (Button) view.findViewById(R.id.button);
                NormalStringAdapter adapter = new NormalStringAdapter(context, list);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String starttime = list.get(position).getStarttime();
                        String endTime = list.get(position).getEndTime();

                        if (listener != null) {
                            listener.onItemClick(position, starttime + "," + endTime);
                        }
                        kyeBaseDialog.dismiss();
                    }
                });
                if (StringUtils.isNullOrEmpty(mTitle)) {
                    title.setVisibility(View.GONE);
                } else {
                    title.setVisibility(View.VISIBLE);
                    title.setText(mTitle);
                }
                if (!StringUtils.isNullOrEmpty(button)) {
                    btn.setText(button);
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kyeBaseDialog.dismiss();

                    }
                });

            }
        });
        kyeBaseDialog.setCancelable(false);
        kyeBaseDialog.setCanceledOnTouchOutside(false);
    }

    public static void showListDialog(final Activity context, final String mTitle, final String button, final List<String> list, final DialogListener listener) {
        IhyBaseDialog kyeBaseDialog = IhyBaseDialog.createKyeBaseDialog(context, R.layout.dialog_list, new IhyBaseDialog.DialogListener() {
            @Override
            public void bindView(View view, final IhyBaseDialog kyeBaseDialog) {
                TextView title = (TextView) view.findViewById(R.id.title);
                ListView lv = (ListView) view.findViewById(R.id.lv);
                Button btn = (Button) view.findViewById(R.id.button);
                CommentAdapter adapter = new CommentAdapter(context, list);
                lv.setAdapter(adapter);
                if (StringUtils.isNullOrEmpty(mTitle)) {
                    title.setVisibility(View.GONE);
                } else {
                    title.setVisibility(View.VISIBLE);
                    title.setText(mTitle);
                }
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if (listener != null) {
                            listener.onItemClick(i, list.get(i));
                        }
                        kyeBaseDialog.dismiss();
                    }
                });
                if (!StringUtils.isNullOrEmpty(button)) {
                    btn.setText(button);
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kyeBaseDialog.dismiss();

                    }
                });

            }
        });
        kyeBaseDialog.setCancelable(false);
        kyeBaseDialog.setCanceledOnTouchOutside(false);
    }

}
