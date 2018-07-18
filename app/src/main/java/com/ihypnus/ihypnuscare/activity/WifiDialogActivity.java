package com.ihypnus.ihypnuscare.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.adapter.WifiListAdapter;
import com.ihypnus.ihypnuscare.utils.ToastUtils;

import java.util.ArrayList;

/**
 * @Package com.kye.smart.wifitext.thread
 * @author: llw
 * @Description: wifi dialog 样式的activity
 * @date: 2018/6/4 13:41
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class WifiDialogActivity extends Activity {


    private ListView mListView;
    private WifiListAdapter mWifiListAdapter;
    private ArrayList<ScanResult> mWifiResultList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_wifi_dialog);
        bindViews();
        initData();
        initEvent();
    }

    private void bindViews() {
        mListView = (ListView) findViewById(R.id.listView);
        mWifiListAdapter = new WifiListAdapter(this, R.layout.wifi_list_item);
        mListView.setAdapter(mWifiListAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        mWifiResultList = intent.getParcelableArrayListExtra("WIFI_RESULT");
        mWifiListAdapter.clear();
        mWifiListAdapter.addAll(mWifiResultList);
    }

    private void initEvent() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mWifiManager.disconnect();
                final ScanResult scanResult = mWifiListAdapter.getItem(position);
                if (scanResult != null) {
                    String ssid = scanResult.SSID;
                    if (ssid != null) {
                        Intent intent = getIntent();
                        intent.putExtra("SSID", ssid);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtils.showToastDefault(getString(R.string.tv_toast_show_corect_wiifi));
                    }
                } else {
                    ToastUtils.showToastDefault(getString(R.string.tv_toast_show_corect_wiifi));
                }

            }
        });
    }
}
