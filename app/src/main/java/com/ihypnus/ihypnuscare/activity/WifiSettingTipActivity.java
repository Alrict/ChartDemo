package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.adapter.WifiListAdapter;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.utils.WifiSettingManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
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
    private String IP = "192.168.43.146";
    //    private String IP = "192.168.4.1";
    private static final int WIFICIPHER_NOPASS = 1;
    private static final int WIFICIPHER_WEP = 2;
    private static final int WIFICIPHER_WPA = 3;

    public static final int DEVICE_CONNECTING = 1;//有设备正在连接热点
    public static final int DEVICE_CONNECTED = 2;//有设备连上热点
    public static final int SEND_MSG_SUCCSEE = 3;//发送消息成功
    public static final int SEND_MSG_ERROR = 4;//发送消息失败
    public static final int GET_MSG = 6;//获取新消息

    private ListView mListView;
    private WifiListAdapter mWifiListAdapter;
    private WifiManager mWifiManager;
    private WifiSettingManager mWifiSettingManager;
    private WifiConfiguration mConfig;
    private Socket mSocket = null;
    private Gson mGson = new Gson();
    private CreateSocketThread mThread;
    private Button mBtSetWifi;
    private static final int GET_LOCATION_INFO = 122;
    private OutputStream mOutputStream;
    private String mSsid = "";


    @Override
    protected int setView() {
        return R.layout.activity_wifi_setting_tip;
    }

    @Override
    protected void findViews() {
        mIvBack = findViewById(R.id.iv_back);
        mBtSetWifi = findViewById(R.id.bt_set_wifi);
        mBtNext = findViewById(R.id.bt_next);
        mListView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mWifiListAdapter = new WifiListAdapter(this, R.layout.wifi_list_item);
        mListView.setAdapter(mWifiListAdapter);

        //注册广播
        initBroadcastReceiver();

        //初始化wifiManager
        mWifiSettingManager = WifiSettingManager.getInstance().initWifiManager(this);
        //扫描wifi
        mWifiSettingManager.startScan();

        //wifiManager
        mWifiManager = mWifiSettingManager.getWifiManager();

    }

    @Override
    protected void initEvent() {

        mIvBack.setOnClickListener(this);
        mBtSetWifi.setOnClickListener(this);
        mBtNext.setOnClickListener(this);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mWifiManager.disconnect();
                final ScanResult scanResult = mWifiListAdapter.getItem(position);
                if (scanResult != null) {
                    mSsid = scanResult.SSID;
                    if (mSsid != null) {
                        jumpToDialogActivity();
                    } else {
                        showIndeterminateProgressDialog(true, getResources().getString(R.string.wifi_permission));
                    }
                } else {
                    showIndeterminateProgressDialog(true, getResources().getString(R.string.wifi_permission));
                }

            }
        });
    }

    /**
     * 跳转至输入wifi密码dialog页面
     */
    private void jumpToDialogActivity() {
        Intent intent = new Intent(this, WifiDialogActivity.class);
        startActivityForResult(intent, 122);
    }

    /**
     * 设置呼吸机要接连的wifi ssid 和 pwd
     *
     * @param ssid ssid
     * @param pwd  密码
     */
    private void setWifiMsg(String ssid, String pwd) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ssid", ssid);
        map.put("pwd", pwd);
        String msg = mGson.toJson(map);
        Log.d("llw", msg);
        if (isWifiEnable(this)) {
            connectSocket(msg);
        } else {
            showIndeterminateProgressDialog(true, getResources().getString(R.string.wifi_permission_connect_target));
        }

    }

    /**
     * 创建socket
     */
    private void connectSocket(String msg) {
        mThread = new CreateSocketThread(msg);
        mThread.start();
    }

    class CreateSocketThread extends Thread {

        String msg;

        public CreateSocketThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            super.run();
            try {
                if (mSocket == null) {
                    mSocket = new Socket(IP, PORT);
                }
                mOutputStream = mSocket.getOutputStream();

                if (mSocket == null || mSocket.isClosed()) {
                    initSocket();
                }
                if (msg != null) {

                    mOutputStream.write(msg.getBytes());

                    //发送完一条数据后，需要再写入“\r\n”，否则可能服务端不能实时收到数据。
                    mOutputStream.write("\r\n".getBytes());

                    mOutputStream.flush();
                    closeScoket();
                }

            } catch (UnknownHostException e) {

                e.printStackTrace();
                closeScoket();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showIndeterminateProgressDialog(true, getResources().getString(R.string.socket_err));
                    }
                });
                Log.e("llw", "网络异常,scoket连接失败");

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showIndeterminateProgressDialog(true, getResources().getString(R.string.socket_err));
                    }
                });
                Log.d("llw", "scoket连接失败" + e.toString());
                closeScoket();
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
        showIndeterminateProgressDialog(true, "连接中...");
//        ToastUtils.showToastDefault(this, "连接中...");
        mWifiSettingManager.addNetwork(config);
    }

    @Override
    protected void loadData() {
        checkWifiPermission();
    }

    private void checkWifiPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GET_LOCATION_INFO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case GET_LOCATION_INFO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WifiSettingManager.getInstance().initWifiManager(WifiSettingTipActivity.this).startScan();
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
//                    showToast("get");
                } else {
                    // 没有获取到权限，做特殊处理
                    checkWifiPermission();
                }
                break;
            default:
                break;
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
//                jumpToWifiSetting();
                BaseDialogHelper.showLoadingDialog(WifiSettingTipActivity.this, true, "正在刷新...");
                WifiSettingManager.getInstance().initWifiManager(this).startScan();
                break;
        }
    }

    /**
     * 进入系统wifi设置界面
     */
    private void jumpToWifiSetting() {

    }


    private void jumpToWifiSettingActivity() {
        BaseDialogHelper.showMsgTipDialog(this, "设备不存在");
//        setResult(RESULT_OK);
//        finish();
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
                    BaseDialogHelper.dismissLoadingDialog();
                    List<ScanResult> scanResults = mWifiSettingManager.getWifiList();
                    mWifiListAdapter.clear();
                    mWifiListAdapter.addAll(scanResults);
                    if (isWifiEnable(WifiSettingTipActivity.this)) {
                        //wifi已连接
//                        connectAndSocket();

                    } else {
                        //未连接wifi
                        //连接Hypnus_AP  无需密码
//            mConfig = mWifiSettingManager.createWifiInfo("Hypnus_AP", "", WIFICIPHER_NOPASS);
//            mConfig = mWifiSettingManager.createWifiInfo("Hypnus_AP", "", WIFICIPHER_NOPASS);
                        mConfig = mWifiSettingManager.createWifiInfo("alrict", "123456789", 3);
//                        connect(mConfig);
                    }
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
                        showIndeterminateProgressDialog(true, "连接已断开");
//                        ToastUtils.showToastDefault(WifiSettingTipActivity.this, "连接已断开");
                    } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        showIndeterminateProgressDialog(true, "已连接到网络");

                        NetworkInfo.DetailedState state = info.getDetailedState();
                        if (state == state.CONNECTING) {
                            showIndeterminateProgressDialog(true, "连接中...");
//                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "连接中...");
                        } else if (state == state.AUTHENTICATING) {
                            showIndeterminateProgressDialog(true, "正在验证身份信息...");
//                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "正在验证身份信息...");
                        } else if (state == state.OBTAINING_IPADDR) {
                            showIndeterminateProgressDialog(true, "正在获取IP地址..");
//                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "正在获取IP地址...");
                        } else if (state == state.CONNECTED) {
                            showIndeterminateProgressDialog(true, "连接成功");
                            IP = getIp();
                            LogOut.d("llw", "当前链接wifi的ip:" + IP);
                            mWifiSettingManager.startScan();
//                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "连接成功");
//                            connectAndSocket();
                        } else if (state == state.FAILED) {
                            showIndeterminateProgressDialog(true, "请检测您的手机是否成功连接Hypnus_AP热点");
//                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "请检测您的手机是否成功连接Hypnus_AP热点");
                        }
                    }

                    break;
            }
        }
    };

    //连接socket时，不能直接在UI主线程中，不然会包：android.os.NetworkOnMainThreadException错误
    private void initSocket() {

        try {
            mSocket = new Socket(IP, PORT);
            mOutputStream = mSocket.getOutputStream();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("llw", "网络异常");

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("llw", "网络异常");

        }

    }

    //④关闭Socket连接

    private void closeScoket() {
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            mOutputStream = null;
        }

        try {
            if (mSocket != null) {
                mSocket.close();
            }

        } catch (IOException e1) {
            e1.printStackTrace();

        } finally {
            mSocket = null;
        }

    }

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

    private void showIndeterminateProgressDialog(boolean horizontal, String content) {
        ToastUtils.showToastDefault(this, content);
//        if (mProgresslDialog != null) {
//            mProgresslDialog.dismiss();
//        }
//        mProgresslDialog = new MaterialDialog.Builder(this)
//                .title("WiFi连接状态")
//                .content(content)
//                .progress(true, 0)
//                .progressIndeterminateStyle(horizontal)
//                .show();
    }

    private String getIp() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //检查Wifi状态
        if (!wm.isWifiEnabled())
            wm.setWifiEnabled(true);
        WifiInfo wi = wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        String ip = intToIp(ipAdd);
        return ip;
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 122 && data != null) {
            String password = data.getStringExtra("PASSWORD");
            setWifiMsg(mSsid, password);
        }
    }

}
