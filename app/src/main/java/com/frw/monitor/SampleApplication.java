package com.frw.monitor;

import android.app.Application;
import android.util.Log;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.frw.monitor.bean.Data;
import com.frw.monitor.bean.SwitchData;

/**
 * Created by fruwei on 16/11/8.
 */

public class SampleApplication extends Application {

    public static final String TAG = "SampleApplication";

    private static Data currentData;


    public synchronized static void initData(Data data) {
        currentData = data;
    }

    public synchronized static void refreshData(Data data) {
        if (currentData == null)
            return;
        if (currentData.address != data.address)
            return;
        currentData.Ia = data.Ia;
        currentData.Ib = data.Ib;
        currentData.Ic = data.Ic;
        currentData.imbalance = data.imbalance;

        for (int i = 0; i < currentData.num; i++) {
            SwitchData newSwitch = findSwitchData(currentData.sdata[i], data.sdata);
            if (newSwitch == null) {
                Log.w(TAG, " id " + currentData.sdata[i].address + "no new val");
                continue;
            }
            newSwitch.name = currentData.sdata[i].name;
            currentData.sdata[i] = newSwitch;

        }
        currentData = data;
    }

    /**
     * find match switch data
     *
     * @param find
     * @param list
     * @return
     */
    public static SwitchData findSwitchData(SwitchData find, SwitchData[] list) {
        for (SwitchData switchData : list) {
            if (find.address == switchData.address) {
                return switchData;
            }
        }
        return null;
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
