package com.monitor.common;

/**
 * 负载类型枚举
 */

public enum EnumLoadType {

    LEVEL_1(1, "一级负荷"),
    LEVEL_2(2, "二级负荷"),
    LEVEL_IMP(3, "重要负荷");

    private EnumLoadType(int value, String text) {
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
