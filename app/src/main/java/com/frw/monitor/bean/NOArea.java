package com.frw.monitor.bean;

import java.util.List;

/**
 * Created by fruwei on 2016/11/9.
 */

public class NOArea {

    private double ia;
    private double ib;
    private double ic;
    private double loadDegree;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getIa() {
        return ia;
    }

    public void setIa(double ia) {
        this.ia = ia;
    }

    public double getIb() {
        return ib;
    }

    public void setIb(double ib) {
        this.ib = ib;
    }

    public double getIc() {
        return ic;
    }

    public void setIc(double ic) {
        this.ic = ic;
    }

    public double getLoadDegree() {
        return loadDegree;
    }

    public void setLoadDegree(double loadDegree) {
        this.loadDegree = loadDegree;
    }


    public List<NoDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<NoDevice> devices) {
        this.devices = devices;
    }

    private List<NoDevice> devices;

}
