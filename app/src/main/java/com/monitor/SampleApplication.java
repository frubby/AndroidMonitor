package com.monitor;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.monitor.bean.Data;
import com.monitor.bean.SwitchData;

import java.util.Iterator;
import java.util.List;

/**
 * 全局属性
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
//        if (currentData.address != data.address)
//            return;
         if (currentData.config.containsKey(data.address))
             currentData.name = currentData.config.get(data.address);
        else
             currentData.name = "地址:"+data.address+"未配置";

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
