package com.frw.monitor.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.frw.monitor.ActivityNav;
import com.frw.monitor.common.DataMock;
import com.frw.monitor.net.TcpClient;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by yoho on 2016/11/9.
 */

public class DataThread extends Thread {

    public volatile boolean isRun = true;
    Context context;
    Handler handler;
    TcpClient tcpClient;

    public DataThread(Context context, Handler handler) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
        while (isRun) {
            String ip = PreferenceManager.getDefaultSharedPreferences(context).getString("text_ip", "");

            Log.i("test", "run:  " + ip);


            DataMock.area = DataMock.generateData();
            Message msg = handler.obtainMessage(ActivityNav.DATA_REFRESH);
            handler.sendMessage(msg);

            tcpClient = new TcpClient();
            tcpClient.getData();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
