package com.frw.monitor;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.frw.monitor.bean.Data;

/**
 * Created by fruwei on 16/11/8.
 */

public class SampleApplication extends Application {


    private static Data currentData;


    public synchronized static void initData(Data data) {
        currentData = data;
    }

    public synchronized static Data getData() {
        return currentData;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
