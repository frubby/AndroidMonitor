package com.frw.monitor;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.frw.monitor.bean.Area;

/**
 * Created by fruwei on 16/11/8.
 */

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
