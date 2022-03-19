package com.huawei.java.pojo;

public class Site {

    // 边缘节点的编号（ID）
    private String siteName;
    // 边缘节点的总带宽
    private int totalBandWidth;
    // 边缘节点的分配之后剩余带宽
    private int residualBandWidth;

    public Site(String siteName, int totalBandWidth, int residualBandWidth) {
        this.siteName = siteName;
        this.totalBandWidth = totalBandWidth;
        this.residualBandWidth = residualBandWidth;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getTotalBandWidth() {
        return totalBandWidth;
    }

    public void setTotalBandWidth(int totalBandWidth) {
        this.totalBandWidth = totalBandWidth;
    }

    public int getResidualBandWidth() {
        return residualBandWidth;
    }

    public void setResidualBandWidth(int residualBandWidth) {
        this.residualBandWidth = residualBandWidth;
    }

    @Override
    public String toString() {
        return "Site{" +
                "siteName='" + siteName + '\'' +
                ", totalBandWidth=" + totalBandWidth +
                ", residualBandWidth=" + residualBandWidth +
                '}';
    }
}
