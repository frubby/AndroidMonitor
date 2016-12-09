package com.monitor.common;

/**
 * 状态枚举
 */
public enum EnumStats {
    UNKNOW(-1, "无效"),
    NOCONN(0, "断开"),
    AXIANG(1, "A相"),
    BXIANG(2, "B相"),
    CXIANG(4, "C相");

    private EnumStats(int value, String text) {
        this.val = val;
        this.text = text;

    }

    public int val;
    public String text;

    public int getVal() {
        return val;
    }

    public String getText() {
        return text;
    }
}
