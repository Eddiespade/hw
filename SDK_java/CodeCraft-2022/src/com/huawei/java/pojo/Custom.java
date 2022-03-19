package com.huawei.java.pojo;

import com.huawei.java.main.Main;

import java.util.PriorityQueue;

public class Custom {

    // 当前客户有哪些满足qos要求的边缘节点
    private PriorityQueue<SatisfySite> SatisfySite;
    private String customID;
    private int siteCount;

    public Custom(String customID) {
        this.siteCount = 0;
        this.SatisfySite = new PriorityQueue<>((o, p) ->
            // 按照剩余流量的大小排序；
                Main.siteMap.get(p.getSiteID()).getResidualBandWidth() - Main.siteMap.get(o.getSiteID()).getResidualBandWidth()
        );
        this.customID = customID;
    }

    public int getSiteCount() {
        return siteCount;
    }

    public void setSiteCount(int siteCount) {
        this.siteCount = siteCount;
    }

    public void addSatisfySite(SatisfySite satisfySite) {
        siteCount++;
        SatisfySite.add(satisfySite);
    }

    public PriorityQueue<com.huawei.java.pojo.SatisfySite> getSatisfySite() {
        return SatisfySite;
    }

    public void setSatisfySite(PriorityQueue<com.huawei.java.pojo.SatisfySite> satisfySite) {
        SatisfySite = satisfySite;
    }

    public String getCustomID() {
        return customID;
    }

    public void setCustomID(String customID) {
        this.customID = customID;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "sites=" + SatisfySite +
                ", customID='" + customID + '\'' +
                '}';
    }
}
