package com.frw.monitor.bean;

public class Data {
    public long address;
    public String name;

    public float Ia;
    public float Ib;
    public float Ic;
    public float imbalance;//三相不平衡度

    public int num;//有效开关个数
    public SwitchData[] sdata = new SwitchData[32];


    public Data() {
        for (int i = 0; i < 32; i++) {
            sdata[i] = new SwitchData();
        }
    }
}

