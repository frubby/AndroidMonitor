package com.frw.monitor.common;

import com.frw.monitor.bean.Data;
import com.frw.monitor.bean.SwitchData;

import java.util.Random;

/**
 * Created by fruwei on 2016/11/9.
 */

public class DataMock {


    //
//    public static Area generateStruct() {
//        area = new Area();
//        Random random = new Random();
//        int n = random.nextInt(100);
//        List<Device> list = new ArrayList<Device>();
//        for (int i = 0; i < n; i++) {
//            Device device = new Device();
//            device.setId(612300 + i);
//            device.setName("测试开关-" + 612300 + i);
//            list.add(device);
//        }
//        area.setDevices(list);
//        area.setName("测试台区-" + random.nextInt(10));
//        return area;
//    }
//
    public static void generateData(Data area) {
        Random random = new Random();
        area.Ia = (random.nextInt(100) / 50.0f);
        area.Ib = (random.nextInt(100) / 50.0f);
        area.Ic = (random.nextInt(100) / 50.0f);
        area.imbalance = (float) (random.nextInt(360));
        SwitchData switchData[] = area.sdata;
        for (int i = 0; i < area.num; i++) {
            SwitchData device = area.sdata[i];
            device.Ia = (random.nextInt(100) / 50.0f);
            device.Ib = (random.nextInt(100) / 50.0f);
            device.Ic = (random.nextInt(100) / 50.0f);


            device.load = (random.nextInt(100) / 50.0f);
            device.num = (random.nextInt(20));
            device.switchState = (EnumStats.values()[random.nextInt(3)].getText());

            device.loadType = (EnumLoadType.values()[random.nextInt(3)].getText());
        }
    }

}
