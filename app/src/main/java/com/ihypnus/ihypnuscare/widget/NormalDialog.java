package com.ihypnus.ihypnuscare.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.dialog.BaseDialog;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;


/**
 * @version V1.0
 * @createAuthor llw
 * @createDate 2017/5/15 0015
 * @updateAuthor
 * @updateDate
 * @company
 * @description 公用的dialog
 */
public class NormalDialog extends BaseDialog implements View.OnClickListener {

    private final static String TAG = "NormalDialog";

    private Button btnCannel;
    private Button btnConfirm;
    private TextView tvTitle;
    private TextView tvContent;

    private String mTileText = "温馨提示";
    private CharSequence mContent = "默认内容";
    private String mCannelText = "取消";
    private String mConfirmText = "确定";
    private Context mContext;

    private DialogListener listener;

    public NormalDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }


    public NormalDialog(Context context) {
        this(context, R.style.Theme_dialogLayout);
    }

    private void init(Context context) {
        this.mContext = context;
        View body = LayoutInflater.from(context).inflate(R.layout.layout_normal_dialog, null);
        btnCannel = (Button) body.findViewById(R.id.button1);
        btnConfirm = (Button) body.findViewById(R.id.button2);
        tvTitle = (TextView) body.findViewById(R.id.title);
        tvContent = (TextView) body.findViewById(R.id.content);
        btnCannel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        setData();
        setContentView(body);
    }


    private void setData() {
        tvTitle.setText(mTileText);
        tvContent.setText(mContent);
        btnConfirm.setText(mConfirmText);
        btnCannel.setText(mCannelText);
    }


    public NormalDialog setNormalDialogListenner(DialogListener listener) {
        this.listener = listener;
        return this;
    }


    @Override
    public void onClick(View v) {
        dismiss();
        if (v == btnCannel) {
            if (listener != null) {
                listener.onClick(BaseType.NO);
            }
        } else if (v == btnConfirm) {
            if (listener != null) {
                listener.onClick(BaseType.OK);
            }
        }
    }

    public NormalDialog setmTileText(String mTileText) {
        this.mTileText = mTileText;
        setData();
        return this;
    }

    public NormalDialog setmContent(CharSequence mContent) {
        this.mContent = mContent;
        setData();
        return this;
    }

    public NormalDialog setmConfirmText(String mConfirmText) {
        this.mConfirmText = mConfirmText;
        setData();
        return this;
    }

    public NormalDialog setmCannelText(String mCannelText) {
        this.mCannelText = mCannelText;
        setData();
        return this;
    }

}
