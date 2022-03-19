package com.huawei.java.pojo;

public class SatisfySite {

    private String siteID;
    private int qos;

    public SatisfySite(String siteID, int qos) {
        this.siteID = siteID;
        this.qos = qos;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    @Override
    public String toString() {
        return "SatisfySite{" +
                "site=" + siteID +
                ", qos=" + qos +
                '}';
    }
}
