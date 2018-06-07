package com.ihypnus.ihypnuscare.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ihypnus.ihypnuscare.R;


public class IhyBaseDialog extends BaseDialog {
    private DialogListener listener;

    public interface DialogListener {
        void bindView(View view, IhyBaseDialog kyeBaseDialog);
    }

    public IhyBaseDialog(Context context) {
        this(context, R.style.Theme_dialogLayout);
    }

    public IhyBaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public static IhyBaseDialog createKyeBaseDialog(Context context, int layout, DialogListener listener) {
        IhyBaseDialog kyeBaseDialog = new IhyBaseDialog(context);
        kyeBaseDialog.init(context, layout, listener);
        return kyeBaseDialog;
    }

    private void init(Context context, int layout, DialogListener listener) {
        this.listener = listener;
        View body = LayoutInflater.from(context).inflate(layout, null);
        bindView(body, this);
        setContentView(body);
    }

    public IhyBaseDialog bindView(View body, IhyBaseDialog kyeBaseDialog) {
        listener.bindView(body, kyeBaseDialog);
        show();
        return this;
    }


}
