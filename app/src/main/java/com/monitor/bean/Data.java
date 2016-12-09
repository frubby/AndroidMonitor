package com.monitor.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    public long address;
    public String name;

    public float Ia;
    public float Ib;
    public float Ic;
    public float imbalance;//三相不平衡度

    public int num;//有效开关个数
    public List<SwitchData> sdata=new ArrayList<SwitchData>();

    public Map<Long, String> config = new HashMap<Long, String>();

    public Data() {
    }
}

