package com.huawei.java.pojo;

import java.util.Map;

public class Demand {

    private String mtime;
    private Map<String, Integer> customDemands;

    public Demand(String mtime, Map<String, Integer> customDemands) {
        this.mtime = mtime;
        this.customDemands = customDemands;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public Map<String, Integer> getCustomDemands() {
        return customDemands;
    }

    public void setCustomDemands(Map<String, Integer> customDemands) {
        this.customDemands = customDemands;
    }

    @Override
    public String toString() {
        return "Demand{" +
                "mtime='" + mtime + '\'' +
                ", customDemands=" + customDemands +
                '}';
    }
}
