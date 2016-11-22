package com.frw.monitor.bean;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public long address;
    public String name;

    public float Ia;
    public float Ib;
    public float Ic;
    public float imbalance;//三相不平衡度

    public int num;//有效开关个数
    public List<SwitchData> sdata = new ArrayList<SwitchData>(32);


    public Data() {
        for (int i = 0; i < 32; i++) {
            sdata.add(new SwitchData());
        }
    }
}

