package com.ihypnus.ihypnuscare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ihypnus.ihypnuscare.R;

/**
 * @Package com.kye.smart.wifitext.thread
 * @author: llw
 * @Description:
 * @date: 2018/6/4 13:41
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class WifiDialogActivity extends Activity implements View.OnClickListener {

    private EditText mEtWifiPassWord;
    private Button mBtnCancel;
    private Button mBtnEnsure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_wifi_dialog);
        bindViews();
        initEvent();
    }

    private void initEvent() {
        mBtnCancel.setOnClickListener(this);
        mBtnEnsure.setOnClickListener(this);
    }

    private void bindViews() {
        mEtWifiPassWord = (EditText) findViewById(R.id.et_wifi_pass_word);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnEnsure = (Button) findViewById(R.id.btn_ensure);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_ensure:
                String passWord = mEtWifiPassWord.getText().toString().trim();
                if (passWord.length() > 0 && passWord.length() < 8) {
                    Toast.makeText(WifiDialogActivity.this, getResources().getString(R.string.toast_err_tip), Toast.LENGTH_SHORT).show();
                    mEtWifiPassWord.setText("");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("PASSWORD", passWord);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
