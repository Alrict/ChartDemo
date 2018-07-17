package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.utils.WifiSettingManager;

import org.greenrobot.eventbus.EventBus;

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
 * @Description: 配置wifi界面
 * @date: 2018/7/1 23:13
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class WifiSettingHistoryActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TextView mTvSsid;
    private EditText mTvPwd;
    private CheckBox mCbVisible;
    private Button mBtnConfirm;
    private LinearLayout mLayoutSsid;
    private LinearLayout mLayoutPwd;
    private Button mBtNext;
    private int PORT = 8089;

//    private String IP = "192.168.43.192";
        private String IP = "192.168.4.1"; //正式IP
    private static final int WIFICIPHER_NOPASS = 1;
    private static final int WIFICIPHER_WEP = 2;
    private static final int WIFICIPHER_WPA = 3;

    public static final int DEVICE_CONNECTING = 1;//有设备正在连接热点
    public static final int DEVICE_CONNECTED = 2;//有设备连上热点
    public static final int SEND_MSG_SUCCSEE = 3;//发送消息成功
    public static final int SEND_MSG_ERROR = 4;//发送消息失败
    public static final int GET_MSG = 6;//获取新消息

    private WifiManager mWifiManager;
    private WifiSettingManager mWifiSettingManager;
    private Socket mSocket = null;
    private Gson mGson = new Gson();
    private CreateSocketThread mThread;
    private Button mBtSetWifi;
    private static final int GET_LOCATION_INFO = 122;
    private OutputStream mOutputStream;
    private String mNewDeviceId;
    private ArrayList<ScanResult> mScanResults;
    private String mSSID;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtils.showToastDefault("wifi配置成功");
                    EventBus.getDefault().post(new BaseFactory.CloseActivityEvent(NewDeviceInformationActivity.class));
                    EventBus.getDefault().post(new BaseFactory.CloseActivityEvent(AddNwedeviceActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected int setView() {
        return R.layout.activity_wifi_setting_history;
    }

    @Override
    protected void findViews() {
        mLayoutSsid = (LinearLayout) findViewById(R.id.layout_ssid);
        mLayoutPwd = (LinearLayout) findViewById(R.id.layout_pwd);
        mTvSsid = (TextView) findViewById(R.id.tv_ssid);
        mTvPwd = (EditText) findViewById(R.id.tv_pwd);
        mCbVisible = (CheckBox) findViewById(R.id.cb_visible);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("配置WiFi");
        mScanResults = new ArrayList<>();

        //注册广播
        initBroadcastReceiver();
        //初始化wifiManager
        mWifiSettingManager = WifiSettingManager.getInstance().initWifiManager(this);
        BaseDialogHelper.showLoadingDialog(this, true, "正在扫描wifi...");
        //扫描wifi
        mWifiSettingManager.startScan();
        //wifiManager
        mWifiManager = mWifiSettingManager.getWifiManager();
    }

    @Override
    protected void initEvent() {
        mCbVisible.setOnCheckedChangeListener(this);
        mLayoutSsid.setOnClickListener(this);
        mLayoutPwd.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String passWord = mTvPwd.getText().toString();
        if (isChecked) {
            //设置EditText的密码为可见的
            mTvPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //设置密码为隐藏的
            mTvPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        mTvPwd.setSelection(passWord.length());
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.layout_ssid:
                jumpToWifiList();
                break;

            case R.id.layout_pwd:

                break;
            case R.id.btn_confirm:
                boolean b = checkInfos();
                if (b) {
                    setWifiMsg(mSSID, mTvPwd.getText().toString().trim());
                }
                break;

        }
    }

    private boolean checkInfos() {
        String ssid = mTvSsid.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(ssid)) {
            ToastUtils.showToastDefault("请选择您想连接的wifi热点");
            return false;
        }

        String passWord = mTvPwd.getText().toString().trim();
//        if (StringUtils.isNullOrEmpty(passWord)) {
//            ToastUtils.showToastDefault("请选择您想连接的wifi热点");
//            return false;
//        }
        if (passWord.length() > 0 && passWord.length() < 8) {
            ToastUtils.showToastDefault(getResources().getString(R.string.toast_err_tip));
            mTvPwd.setText("");
            return false;
        }
        return true;
    }

    /**
     * 跳转至wifi列表弹窗界面
     */
    private void jumpToWifiList() {
        if (mScanResults == null || mScanResults.size() == 0) {
            WifiSettingManager.getInstance().initWifiManager(this).startScan();
            Toast.makeText(this, "正在获取附近wifi信息", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, WifiDialogActivity.class);
        intent.putParcelableArrayListExtra("WIFI_RESULT", mScanResults);
        startActivityForResult(intent, 122);
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
                    //设置成功
                    mHandler.obtainMessage(1, "设置成功").sendToTarget();
                }
                BaseDialogHelper.dismissLoadingDialog();

            } catch (UnknownHostException e) {
                BaseDialogHelper.dismissLoadingDialog();
                e.printStackTrace();
                closeScoket();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BaseDialogHelper.dismissLoadingDialog();
                        showIndeterminateProgressDialog(true, getResources().getString(R.string.socket_err));
                    }
                });
                Log.e("llw", "网络异常,scoket连接失败");

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BaseDialogHelper.dismissLoadingDialog();
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
     * wifi状态改变广播
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            if (action == null) {
                BaseDialogHelper.dismissLoadingDialog();
                return;
            }
            switch (action) {
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    // wifi已成功扫描到可用wifi。
                    BaseDialogHelper.dismissLoadingDialog();
                    List<ScanResult> scanResults = mWifiSettingManager.getWifiList();
                    mScanResults.clear();
                    for (int i = 0; i < scanResults.size(); i++) {
                        String ssid = scanResults.get(i).SSID;
                        //Hypnus_AP
                        if (!StringUtils.isNullOrEmpty(ssid) && !ssid.equals("Hypnus_AP")) {
                            mScanResults.add(scanResults.get(i));
                        }
                    }
                    if (mScanResults.size() > 0) {
                        mTvSsid.setText(mScanResults.get(0).SSID);
                    }

                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
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

                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                        showIndeterminateProgressDialog(true, "连接已断开");
                    } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
//                        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//                        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                        showIndeterminateProgressDialog(true, "已连接到网络");
                        NetworkInfo.DetailedState state = info.getDetailedState();
                        if (state == NetworkInfo.DetailedState.CONNECTING) {
                            showIndeterminateProgressDialog(true, "连接中...");
//                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "连接中...");
                        } else if (state == NetworkInfo.DetailedState.AUTHENTICATING) {
                            showIndeterminateProgressDialog(true, "正在验证身份信息...");
//                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "正在验证身份信息...");
                        } else if (state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                            showIndeterminateProgressDialog(true, "正在获取IP地址..");
//                            ToastUtils.showToastDefault(WifiSettingTipActivity.this, "正在获取IP地址...");
                        } else if (state == NetworkInfo.DetailedState.CONNECTED) {
//                            showIndeterminateProgressDialog(true, "连接成功");
//                            IP = getIp();
                            LogOut.d("llw", "当前链接wifi的ip:" + IP);
                            mWifiSettingManager.startScan();
                        } else if (state == NetworkInfo.DetailedState.FAILED) {
                            showIndeterminateProgressDialog(true, "请检测您的手机是否成功连接Hypnus_AP热点");
                        }
                    }

                    break;
            }
        }
    };

    /**
     * 获取当前连接的ip
     *
     * @return
     */
    private String getIp() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        //检查Wifi状态
        if (!wm.isWifiEnabled())
            wm.setWifiEnabled(true);
        WifiInfo wi = wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        return intToIp(ipAdd);
    }

    /**
     * 将地址转换成ip
     *
     * @param i
     * @return
     */
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
            mSSID = data.getStringExtra("SSID");
            mTvSsid.setText(mSSID);
        }
    }

    /**
     * 设置呼吸机要接连的wifi ssid 和 pwd
     *
     * @param ssid ssid
     * @param pwd  密码
     */
    private void setWifiMsg(String ssid, String pwd) {
        BaseDialogHelper.showLoadingDialog(this, true, "正在为您的设备配置wifi信息");
        HashMap<String, String> map = new HashMap<>();
        map.put("ssid", ssid);
        map.put("pwd", pwd);
        String msg = mGson.toJson(map);
        Log.d("llw", msg);
        if (isWifiEnable(this)) {
            connectSocket(msg);
        } else {
            BaseDialogHelper.dismissLoadingDialog();
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

    /**
     * 检查wifi权限
     */
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
                    WifiSettingManager.getInstance().initWifiManager(WifiSettingHistoryActivity.this).startScan();
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

    //连接socket时，不能直接在UI主线程中，不然会包：android.os.NetworkOnMainThreadException错误
    private void initSocket() {

        try {
            mSocket = new Socket(IP, PORT);
            mOutputStream = mSocket.getOutputStream();
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
            BaseDialogHelper.dismissLoadingDialog();
        } finally {
            mOutputStream = null;
        }

        try {
            if (mSocket != null) {
                mSocket.close();
            }

        } catch (IOException e1) {
            e1.printStackTrace();
            BaseDialogHelper.dismissLoadingDialog();

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
                if (splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }

    private void showIndeterminateProgressDialog(boolean horizontal, String content) {
        ToastUtils.showToastDefault(this, content);
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
        return !(mWifi == null || !mWifi.isConnected());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
