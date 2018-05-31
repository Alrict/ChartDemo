package com.ihypnus.ihypnuscare.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ihypnus.ihypnuscare.activity.WifiSettingTipActivity;
import com.ihypnus.ihypnuscare.utils.LogOut;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Package com.ihypnus.ihypnuscare
 * @author: llw
 * @Description:
 * @date: 2018/5/30 14:36
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ConnectThread extends Thread {

    private final Socket socket;
    private Handler handler;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ConnectThread(Socket socket, Handler handler) {
        setName("ConnectThread");
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void run() {
        if (socket == null) {
            return;
        }
        handler.sendEmptyMessage(WifiSettingTipActivity.DEVICE_CONNECTED);
        try {
            //获取数据流
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                //读取数据
                bytes = inputStream.read(buffer);
                if (bytes > 0) {
                    final byte[] data = new byte[bytes];
                    System.arraycopy(buffer, 0, data, 0, bytes);

                    Message message = Message.obtain();
                    message.what = WifiSettingTipActivity.GET_MSG;
                    Bundle bundle = new Bundle();
                    bundle.putString("MSG", new String(data));
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     */
    public void sendData(String msg) {
        if (socket!=null){
            LogOut.d("llw","socket是否连接:"+socket.isConnected());
        }
        try {
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(msg);  // 写一个UTF-8的信息
            writer.write(msg.getBytes());
            writer.write("\r\n".getBytes());
            writer.flush();
            LogOut.d("llw","socket客户端发送消息:"+msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (outputStream != null) {
//            try {
//                outputStream.write(msg.getBytes());
//                Message message = Message.obtain();
//                message.what = WifiSettingTipActivity.SEND_MSG_SUCCSEE;
//                Bundle bundle = new Bundle();
//                bundle.putString("MSG", new String(msg));
//                message.setData(bundle);
//                handler.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//                Message message = Message.obtain();
//                message.what = WifiSettingTipActivity.SEND_MSG_ERROR;
//                Bundle bundle = new Bundle();
//                bundle.putString("MSG", new String(msg));
//                message.setData(bundle);
//                handler.sendMessage(message);
//            }
//        }
    }
}
