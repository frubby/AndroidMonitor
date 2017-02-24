package com.monitor.bean;

import java.io.Serializable;

public class SwitchData implements Serializable{
	public long address=0;
	public String name;

	public float Ia;
	public float Ib;
	public float Ic;
	public float load ;//出线负荷
	public int num;//开关动作次数
	public String switchState;//开关状态
	public String loadType;//负荷类型
}
