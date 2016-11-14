package com.frw.monitor.bean;

public class Data {
    public float Ia;
    public float Ib;
    public float Ic;
    public float imbalance;//三相不平衡度

    public SwitchData[] sdata = new SwitchData[8];
}
