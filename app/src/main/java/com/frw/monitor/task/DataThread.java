package com.frw.monitor.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import com.frw.monitor.ActivityNav;
import com.frw.monitor.SampleApplication;
import com.frw.monitor.bean.Area;
import com.frw.monitor.common.DataMock;

import org.json.JSONObject;

/**
 * Created by yoho on 2016/11/9.
 */

public class DataThread extends Thread {

    public volatile boolean isRun = true;
    Context context;
    Handler handler;

    public DataThread(Context context, Handler handler) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
        while (isRun) {
            Log.i("test", "run: ");

            DataMock.area = DataMock.generateData();
            Message msg = handler.obtainMessage(ActivityNav.DATA_REFRESH);
            handler.sendMessage(msg);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
