package com.monitor.net;

import android.content.Context;
import android.os.Bundle;
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
import com.monitor.tcpdata.Protocol;
import com.monitor.tcpdata.TcpLink;
import com.monitor.tcpdata.TcpLinkImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class TcpServer implements Runnable {
    public static final String TAG = "TcpServer";
    Context context;
    Handler handler;
    public volatile boolean flag = true;//线程运行标志

    //    public byte[] sendData = new byte[4096];
//    public byte[] receiveData = new byte[4096];
    private Data data;

    ServerSocket serverSocket;

    //0 refresh  1 append
    int state = 0;

    String ip;
    int port;

    public TcpServer(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
//        ip = PreferenceManager.getDefaultSharedPreferences(context).getString("text_ip", "");
        port = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("text_port", "1234"));
//
//        Log.i(TAG, "ip : " + ip + "  port : " + port);
        data = new Data();
    }


    SwitchData cmd = null;

    public synchronized void sendCmd(SwitchData cmd) {
        this.cmd = cmd;
        if (protocol == null || protocol.getLinkState() != true) {
            Toast.makeText(context, "link error", Toast.LENGTH_SHORT).show();
            return;
        }
        protocol.setSwitchData(cmd);
        protocol.dataSendProcess();
    }

    Protocol protocol;
    TcpLink tcpLink;

    public void clearCmd(){
        if(protocol!=null){
            protocol.clearSwitchData();
        }
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
                tcpLink = new TcpLinkImpl();
                tcpLink.tcpConnect(clientSocket);
                protocol = new Protocol(tcpLink);
                protocol.setLinkState(true);

            } catch (IOException e) {
                Toast.makeText(context, "serverSocket.accept error " + e.getMessage(), Toast.LENGTH_LONG).show();

                e.printStackTrace();
                continue;
            }
            System.out.println("Server: Receiving...");
            msg = handler.obtainMessage(ActivityNav.CONNECT);
            handler.sendMessage(msg);
            try {
                //                    clientSocket.setKeepAlive(true);
                while (flag && protocol.getLinkState()) {
                    try {
                        Thread.sleep(1000);//等待时间
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if (!flag)
                        break;


                    protocol.dataProcess();

                    data = protocol.getData();
                    System.out.println(JSON.toJSONString(data, false));


                    if (handler != null) {  //test case
                        msg = handler.obtainMessage(ActivityNav.DATA_REFRESH);
                        handler.sendMessage(msg);
                    }

                    if (!protocol.getLinkState()) {
                        tcpLink.disconnected();
                        protocol.setLinkStateNum(0);
                    }


                    if (data != null) {

                        SampleApplication.refreshData(data);

                        if (handler != null) {  //test case
                            msg = handler.obtainMessage(ActivityNav.DATA_REFRESH);
                            handler.sendMessage(msg);
                        }
                    } else {
                        Toast.makeText(context, "data parse null ", Toast.LENGTH_SHORT).show();
                    }
                    SwitchData sd = protocol.getSwitchData();
                    if (sd.address > 0) {
                        SampleApplication.refreshData(sd);

                        msg = handler.obtainMessage(ActivityNav.DATA_RESULT_OK);
                        Bundle bd = new Bundle();
                        bd.putSerializable("data", sd);
                        msg.setData(bd);
                        handler.sendMessage(msg);

                    } else if (sd.address < 0) {

                        msg = handler.obtainMessage(ActivityNav.DATA_RESULT_ERR);
                        handler.sendMessage(msg);
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

}
