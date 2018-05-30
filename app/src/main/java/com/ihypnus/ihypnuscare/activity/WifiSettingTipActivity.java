package com.ihypnus.ihypnuscare.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.adapter.WifiListAdapter;
import com.ihypnus.ihypnuscare.thread.ConnectThread;
import com.ihypnus.ihypnuscare.thread.ListenerThread;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.utils.WifiSettingManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: wifi设置提示页面
 * @date: 2018/5/29 17:24
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class WifiSettingTipActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private Button mBtNext;
    private int PORT = 8089;
    private String IP = "192.168.4.1";
    private static final int WIFICIPHER_NOPASS = 1;
    private static final int WIFICIPHER_WEP = 2;
    private static final int WIFICIPHER_WPA = 3;

    public static final int DEVICE_CONNECTING = 1;//有设备正在连接热点
    public static final int DEVICE_CONNECTED = 2;//有设备连上热点
    public static final int SEND_MSG_SUCCSEE = 3;//发送消息成功
    public static final int SEND_MSG_ERROR = 4;//发送消息失败
    public static final int GET_MSG = 6;//获取新消息

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WifiSettingTipActivity.DEVICE_CONNECTING:
                    //有设备正在连接热点

                    break;

                case WifiSettingTipActivity.SEND_MSG_SUCCSEE:
                    //有设备正在连接热点

                    break;

            }
        }
    };

    /**
     * 热点名称
     */
    private static final String WIFI_HOTSPOT_SSID = "\"Hypnus_AP\"";

    private ListenerThread listenerThread = new ListenerThread(PORT, handler);
    private ListView mListView;
    private WifiListAdapter mWifiListAdapter;
    private WifiManager mWifiManager;
    private WifiSettingManager mWifiSettingManager;
    private WifiConfiguration mConfig;
    private ConnectThread mConnectThread;
    private Socket mSocket = null;
    private Gson mGson = new Gson();
    private CreateSocketThread mThread;

    @Override
    protected int setView() {
        return R.layout.activity_wifi_setting_tip;
    }

    @Override
    protected void findViews() {
        mIvBack = findViewById(R.id.iv_back);
        mBtNext = findViewById(R.id.bt_next);
        mListView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //注册广播
        initBroadcastReceiver();
        //初始化wifiManager
        mWifiSettingManager = WifiSettingManager.getInstance().initWifiManager(this);
        //扫描wifi
        mWifiSettingManager.startScan();
        //wifiManager
        mWifiManager = mWifiSettingManager.getWifiManager();

        mWifiListAdapter = new WifiListAdapter(this, R.layout.wifi_list_item);
        mListView.setAdapter(mWifiListAdapter);
        listenerThread.start();
    }

    @Override
    protected void initEvent() {

        mIvBack.setOnClickListener(this);
        mBtNext.setOnClickListener(this);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mWifiManager.disconnect();
                final HashMap<String, String> map = new HashMap<>();
                final ScanResult scanResult = mWifiListAdapter.getItem(position);
                String capabilities = scanResult.capabilities;
                int type = WIFICIPHER_WPA;
                if (!TextUtils.isEmpty(capabilities)) {
                    if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                        type = WIFICIPHER_WPA;
                    } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                        type = WIFICIPHER_WEP;
                    } else {
                        type = WIFICIPHER_NOPASS;
                    }
                }
                mConfig = mWifiSettingManager.isExsits(scanResult.SSID);
                if (mConfig == null) {
                    if (type != WIFICIPHER_NOPASS) {//需要密码
                        final EditText editText = new EditText(WifiSettingTipActivity.this);
                        final int finalType = type;
                        new AlertDialog.Builder(WifiSettingTipActivity.this).setTitle("请输入Wifi密码").setIcon(
                                android.R.drawable.ic_dialog_info).setView(
                                editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String ssid = scanResult.SSID;
                                String pwd = editText.getText().toString().trim();
                                setWifiMsg(ssid, pwd, map);


                            }
                        })
                                .setNegativeButton("取消", null).show();

                    } else {
                        String ssid = scanResult.SSID;
                        String pwd = "";
                        setWifiMsg(ssid, pwd, map);

                    }
                } else {
//                    connect(mConfig);
                }
            }
        });
    }

    /**
     * 设置呼吸机要接连的wifi ssid 和 pwd
     *
     * @param ssid ssid
     * @param pwd  密码
     * @param map  map
     */
    private void setWifiMsg(String ssid, String pwd, HashMap<String, String> map) {
        map.put("ssid", ssid);
        map.put("pwd", pwd);
        String msg = mGson.toJson(map);
        LogOut.d("llw", msg);
        if (mConnectThread != null) {
            mConnectThread.sendData(msg);
        } else {
            Log.w("AAA", "connectThread == null");
        }
    }

    /**
     * 创建socket
     */
    private void connectSocket() {
        mThread = new CreateSocketThread();
        mThread.start();


    }

    class CreateSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                if (mSocket == null) {
                    mSocket = new Socket(IP, PORT);
                }
                mConnectThread = new ConnectThread(mSocket, handler);
                mConnectThread.start();
            } catch (IOException e) {
                LogOut.d("llw", "scoket连接失败");
                e.printStackTrace();
            }
        }
    }


    /**
     * 连接wifi
     *
     * @param config
     */
    private void connect(WifiConfiguration config) {
        ToastUtils.showToastDefault(this, "连接中...");
        mWifiSettingManager.addNetwork(config);
    }

    @Override
    protected void loadData() {

        if (isWifiEnable(this)) {
            //wifi已连接
            connectAndSocket();

        } else {
            //未连接wifi
            //连接Hypnus_AP  无需密码
            mConfig = mWifiSettingManager.createWifiInfo("Hypnus_AP", "", WIFICIPHER_NOPASS);
            connect(mConfig);
        }


    }

    /**
     * 连接目标wifi并创建socket
     */
    private void connectAndSocket() {
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        String ssid = connectionInfo.getSSID();
        if (ssid.equals(WIFI_HOTSPOT_SSID)) {
            //连接的是目标wifi
            //创建socket连接
            connectSocket();
        } else {
            //否则断开连接并尝试连接热点Hypnus_AP
            mWifiManager.disconnect();
            //连接Hypnus_AP  无需密码
            mConfig = mWifiSettingManager.createWifiInfo("Hypnus_AP", "", WIFICIPHER_NOPASS);
            connect(mConfig);
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;

            case R.id.bt_next:
                //设置好了
                jumpToWifiSettingActivity();
                break;
            case R.id.bt_set_wifi:
                //下一步
                jumpToWifiSetting();
                break;
        }
    }

    /**
     * 进入系统wifi设置界面
     */
    private void jumpToWifiSetting() {

    }


    private void jumpToWifiSettingActivity() {
        setResult(RESULT_OK);
        finish();
    }


    /**
     * wifi状态改变广播
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            switch (action) {
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    Log.w("BBB", "SCAN_RESULTS_AVAILABLE_ACTION");
                    // wifi已成功扫描到可用wifi。
                    List<ScanResult> scanResults = mWifiSettingManager.getWifiList();
                    mWifiListAdapter.clear();
                    mWifiListAdapter.addAll(scanResults);
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    Log.w("BBB", "WifiManager.WIFI_STATE_CHANGED_ACTION");
                    int wifiState = intent.getIntExtra(
                            WifiManager.EXTRA_WIFI_STATE, 0);
                    switch (wifiState) {
                        case WifiManager.WIFI_STATE_ENABLED:
                            //获取到wifi开启的广播时，开始扫描
                            mWifiSettingManager.startScan();
                            break;
                        case WifiManager.WIFI_STATE_DISABLED:
                            //wifi关闭发出的广播
                            break;
                    }
                    break;
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    Log.w("BBB", "WifiManager.NETWORK_STATE_CHANGED_ACTION");
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                        ToastUtils.showToastDefault(WifiSettingTipActivity.this, "连接已断开");
                    } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        ToastUtils.showToastDefault(WifiSettingTipActivity.this, "已连接到网络:" + wifiInfo.getSSID());

                        Log.w("AAA", "wifiInfo.getSSID():" + wifiInfo.getSSID() + "  WIFI_HOTSPOT_SSID:" + WIFI_HOTSPOT_SSID);

                        NetworkInfo.DetailedState state = info.getDetailedState();
                        if (state == state.CONNECTING) {
                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "连接中...");
                        } else if (state == state.AUTHENTICATING) {
                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "正在验证身份信息...");
                        } else if (state == state.OBTAINING_IPADDR) {
                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "正在获取IP地址...");
                        } else if (state == state.CONNECTED) {
                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "连接成功");
                            connectAndSocket();
                        } else if (state == state.FAILED) {
                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "请检测您的手机是否成功连接Hypnus_AP热点");
                        }
                    }

                    break;
            }
        }
    };

    /**
     * 获取连接到热点上的手机ip
     *
     * @return
     */
    private ArrayList<String> getConnectedIP() {
        ArrayList<String> connectedIP = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }

    private void initBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);

        registerReceiver(receiver, intentFilter);
    }

    /**
     * 判断当前Wifi是否可用
     **/
    private boolean isWifiEnable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo mWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi == null || !mWifi.isConnected()) {
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
