package com.frw.monitor.common;

/**
 * Created by fruwei on 16/11/8.
 */

public enum EnumLoadType {

    AXIANG(1, "A相"),
    BXIANG(2, "B相"),
    BISUO(3, "闭锁");

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
