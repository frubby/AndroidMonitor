package com.frw.monitor;

import android.app.Application;
import android.util.Log;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.frw.monitor.bean.Data;
import com.frw.monitor.bean.SwitchData;

import java.util.Iterator;
import java.util.List;

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


        currentData.sdata.clear();

        Iterator<SwitchData> iterator = data.sdata.iterator();
        while (iterator.hasNext()) {
            SwitchData currentSwitchData = iterator.next();
            SwitchData showData = new SwitchData();

            showData.address = currentSwitchData.address;
            if (currentData.config.containsKey(showData.address))
                showData.name = currentData.config.get(showData.address);
            else
                showData.name = "未配置";

            showData.num = currentSwitchData.num;
            showData.Ia = currentSwitchData.Ia;
            showData.Ib = currentSwitchData.Ib;
            showData.Ic = currentSwitchData.Ic;
            showData.load = currentSwitchData.load;
            showData.loadType = currentSwitchData.loadType;
            showData.switchState = currentSwitchData.switchState;
            currentData.sdata.add(showData);
        }
    }

    /**
     * find match switch data
     *
     * @param find
     * @param list
     * @return
     */
    public static SwitchData findSwitchData(SwitchData find, List<SwitchData> list) {
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
