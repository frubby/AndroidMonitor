package com.frw.monitor.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.frw.monitor.ActivityNav;
import com.frw.monitor.SampleApplication;
import com.frw.monitor.bean.Data;

public class DataThread extends Thread {
    public static final String TAG = "DataThread";
    Context context;
    Handler handler;


    String ip;
    int port;

    public DataThread(Context context, Handler handler) {
        this.handler = handler;
        this.context = context;
        ip = PreferenceManager.getDefaultSharedPreferences(context).getString("text_ip", "");
        port = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("text_port", "1234"));

        Log.i(TAG, "ip : " + ip + "  port : " + port);
    }


    public Data data = new Data();//规约提供的数据
    TcpLink link = new TcpLinkImpl();//换成你实际实现的类

    int refreshTime = 1000;//换成实际刷新时间
    public volatile boolean flag = true;//线程运行标志


    public void stopThread() {
        flag = false;
        link.disconnected();
    }

    public void run() {//单开一线程来跑
        Protocol protocol = new Protocol(link);

        while (flag) {
            if (!protocol.getLinkState()) {
                try {
                    while (flag && !link.tcpConnect(ip, port)) {
                        if (handler != null) {  //test case
                            Message msg = handler.obtainMessage(ActivityNav.DISCONNECT);
                            handler.sendMessage(msg);
                        }
                        Thread.sleep(5000);
                        Log.w(TAG, "reconnect...");

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (handler != null) {  //test case
                Message msg = handler.obtainMessage(ActivityNav.CONNECT);
                handler.sendMessage(msg);
            }
            if (!flag)
                break;

            protocol.dataProcess();
            data = protocol.getData();
            System.out.println(JSON.toJSONString(data, false));

            SampleApplication.refreshData(data);

            if (handler != null) {  //test case
                Message msg = handler.obtainMessage(ActivityNav.DATA_REFRESH);
                handler.sendMessage(msg);
            }

            if (!protocol.getLinkState()) {
                link.disconnected();
                protocol.setLinkStateNum(0);
            }

            try {
                Thread.sleep(refreshTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) {
        DataThread run = new DataThread(null, null);
        run.start();
    }

}
