package com.monitor.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.monitor.ActivityNav;
import com.monitor.SampleApplication;
import com.monitor.bean.Data;
import com.monitor.bean.SwitchData;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class TcpServer implements Runnable {
    public static final String TAG = "TcpServer";
    Context context;
    Handler handler;
    public volatile boolean flag = true;//线程运行标志

    public byte[] sendData = new byte[4096];
    public byte[] receiveData = new byte[4096];
    private Data data;

    ServerSocket serverSocket;

    //0 refresh  1 append
    int state = 0;

    String ip;
    int port;

    public TcpServer(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        ip = PreferenceManager.getDefaultSharedPreferences(context).getString("text_ip", "");
        port = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("text_port", "1234"));

        Log.i(TAG, "ip : " + ip + "  port : " + port);
        data = new Data();
    }


    @Override
    public void run() {
        Looper.prepare();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "ServerSocket error " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Message msg;
        while (flag) {
            msg = handler.obtainMessage(ActivityNav.DISCONNECT);
            handler.sendMessage(msg);
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                Toast.makeText(context, "serverSocket.accept error " + e.getMessage(), Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
            System.out.println("Server: Receiving...");

            msg = handler.obtainMessage(ActivityNav.CONNECT);
            handler.sendMessage(msg);
            try {
                //                    clientSocket.setKeepAlive(true);
                InputStream in = clientSocket.getInputStream();
                while (flag && clientSocket.isConnected()) {
                    try {
                        Thread.sleep(200);//等待时间
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("服务器端接受请求");
                    Log.i(TAG, "read input...");

                    int recvNum = 0;
                    byte recvByte[] = new byte[4096];

                    recvNum = in.read(recvByte);
//                    for (int k = 0; k < recvNum; k++) {
//                        System.out.print(" " + Integer.toHexString(recvByte[k] & 0xFF));
//                    }
                    if (recvNum > 0) {
                        System.arraycopy(recvByte, 0, receiveData, 0, recvNum);
                        Log.i(TAG, "\n num  " + recvNum);


                        dataReceiveProcess(recvNum);
                        Log.i(TAG, JSON.toJSONString(data, false));
                    }


                    if (recvNum > 0 && data != null) {


                        SampleApplication.refreshData(data);

                        if (handler != null) {  //test case
                            msg = handler.obtainMessage(ActivityNav.DATA_REFRESH);
                            handler.sendMessage(msg);
                        }
                    } else {
                        Toast.makeText(context, "data parse null ", Toast.LENGTH_SHORT).show();
                    }

                }


            } catch (Exception e) {
                Log.i(TAG, "Server: Error" + e.getMessage());
                e.printStackTrace();
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e1) {
                        e.printStackTrace();
                        Toast.makeText(context, "Server: Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Server: Error" + e.getMessage());
                    }
                }
            }

        }


    }

    public void start() {
        new Thread(this).start();

    }


    public void stop() {
        flag = false;
        if (serverSocket == null) {
            return;
        }
        if (serverSocket.isClosed()) {
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dataReceiveProcess(int readNum) {
        int sum = readNum;

        int ptr = 0;
        int frame_len = 0;
        while (sum >= 12) {
            if (receiveData[ptr] == 0x68 && receiveData[ptr + 7] == 0x68 && receiveData[ptr + 9] != 0) {
                frame_len = (int) (getLongData(ptr + 9, 1) + 12);
                if (frame_len > sum || calcSum(ptr, frame_len - 2) != receiveData[ptr + frame_len - 2])
                    break;

                dataPro(ptr);

                ptr += frame_len;
                sum -= frame_len;

                continue;
            }
            ++ptr;
            --sum;
        }
    }

    private byte calcSum(int ptr, int len) {
        long sum = 0;
        for (int i = 0; i < len; ++i)
            sum += receiveData[ptr + i];

        return (byte) (sum & 0xFF);
    }

    private void dataPro(int ptr) {
        int num = 0;


        int mark = receiveData[ptr + 4];



        ptr += 8;
        switch (receiveData[ptr]) {
            case (byte) 0x81:
                ptr += 1;
                num = (int) getLongData(ptr, 1);
                if (num % 26 != 0)
                    return;
                num /= 26;
                ptr += 1;

                data.num = num - 1;
                if (data.num > 32)
                    return;

                if(state==0) {   //第一次添加
                    data.sdata.clear();
                }
                int temp = 0;
                for (int i = 0; i < num; ++i) {
                    if(state==0) {   //第一次添加
                        if (i == 0) {
                            data.address = getLongData(ptr, 6);
                            ptr += 6;

                            data.Ia = (float) getLongData(ptr, 4) / 1000;
                            ptr += 4;
                            data.Ib = (float) getLongData(ptr, 4) / 1000;
                            ptr += 4;
                            data.Ic = (float) getLongData(ptr, 4) / 1000;
                            ptr += 4;
                            float max = data.Ia > data.Ib ? data.Ia : data.Ib;
                            max = max > data.Ic ? max : data.Ic;
                            float min = data.Ia < data.Ib ? data.Ia : data.Ib;
                            min = min < data.Ic ? min : data.Ic;
                            data.imbalance = (max - min) / max * 100;

                            ptr += 4;
                            ptr += 4;
                            continue;
                        }
                    }

                    SwitchData switchData = new SwitchData();
                    switchData.address = getLongData(ptr, 6);
                    ptr += 6;

                    switchData.Ia = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;
                    switchData.Ib = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;
                    switchData.Ic = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;

                    switchData.num = (int) getLongData(ptr, 4);
                    ptr += 4;

                    switchData.load = 0;
                    temp = (int) getLongData(ptr, 4);
                    ptr += 4;
                    switch (temp & 0x07) {
                        case 0x00:
                            switchData.switchState = "断开";
                            break;
                        case 0x01:
                            switchData.switchState = "A相";
                            switchData.load = switchData.Ia;
                            break;
                        case 0x02:
                            switchData.switchState = "B相";
                            switchData.load = switchData.Ib;
                            break;
                        case 0x04:
                            switchData.switchState = "C相";
                            switchData.load = switchData.Ic;
                            break;
                        default:
                            switchData.switchState = "无效";
                            break;
                    }
                    int l = (temp & 0xF0) >> 4;//等具体解释
                    switchData.loadType = Integer.toString(l);

                    data.sdata.add(switchData);
                }
                break;
            default:
                break;
        }


        if (mark == 1) {
            state = 1;
        } else {  //后续没了  state置为开始
            state = 0;
        }

    }

    private long getLongData(int ptr, int len) {
        if (len > 8)
            len = 8;

        long sum = 0, temp = 0;
        int n = -1;
        while (++n < len) {
            temp = receiveData[ptr + n] < 0 ? 256 + receiveData[ptr + n] : receiveData[ptr + n];
            sum |= temp << (8 * n);
        }

        return sum;
    }


    public Data getData() {
        return data;
    }


}
