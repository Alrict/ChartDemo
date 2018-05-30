package com.ihypnus.ihypnuscare.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ihypnus.ihypnuscare.activity.WifiSettingTipActivity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Package com.ihypnus.ihypnuscare.thread
 * @author: llw
 * @Description: 线程监听
 * @date: 2018/5/30 14:46
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ListenerThread extends Thread {

    private ServerSocket serverSocket = null;
    private Handler handler;
    private int port;
    private Socket socket;

    public ListenerThread(int port, Handler handler) {
        setName("ListenerThread");
        this.port = port;
        this.handler = handler;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                Log.w("AAA","阻塞");
                //阻塞，等待设备连接
                socket = serverSocket.accept();
                Message message = Message.obtain();
                message.what = WifiSettingTipActivity.DEVICE_CONNECTING;
                handler.sendMessage(message);
            } catch (IOException e) {
                Log.w("AAA","error:"+e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
