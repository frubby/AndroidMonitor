package com.frw.monitor.task;

import android.content.Context;
import android.util.Log;

/**
 * Created by yoho on 2016/11/9.
 */

public class DataThread extends Thread {

    public volatile boolean isRun = false;
    Context context;

    public DataThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (isRun) {
            Log.i("test", "run: ");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
