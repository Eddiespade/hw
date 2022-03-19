package com.huawei.java.main;

import com.huawei.java.pojo.Custom;
import com.huawei.java.pojo.Demand;
import com.huawei.java.pojo.SatisfySite;
import com.huawei.java.pojo.Site;
import com.huawei.java.utils.OutputFile;
import com.huawei.java.utils.ReadFile;

import java.io.IOException;
import java.util.*;

public class Main {

    // siteMap 存储边缘节点的ID，与边缘节点
    public static Map<String, Site> siteMap = new HashMap<>();
    // customMap  存储客户节点的ID，与客户节点
    public static Map<String, Custom> customMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // 检查输出文件是否存在，若存在则删除；
        OutputFile.deleteOutputFile();
        // 读取文件
        ReadFile.readConfig();
        ReadFile.readSiteBandWidth();
        ReadFile.readQos();
        // 读取当前需求，每次只读取一行
        // curDemand 主要存储两个信息：当前时刻 及 每个客户所需要的带宽
        Demand curDemand = null;
        while ((curDemand = ReadFile.readDemand()) != null) {
            // 对边缘节点的带宽重置
            init();
            // // 对当前的需求进行分配
            String[] curAssignment = assignment(curDemand);
            // // 写入到输出文件中
            OutputFile.writeOutput(curAssignment);
        }
        OutputFile.close();

    }

    public static String[] assignment(Demand curDemand) {
        // 创建分配方案
        // customDemands <customID, needBandWidth>;
        Map<String, Integer> customDemands = curDemand.getCustomDemands();
        PriorityQueue<String> custom = new PriorityQueue<>((o, p) ->
                // 按可分配节点的个数从小到大分配
                customMap.get(o).getSiteCount() - customMap.get(p).getSiteCount()
        );
        custom.addAll(customDemands.keySet());
        String[] ans = new String[customDemands.size()];

        // 对每一个客户进行分配
        int index = 0;
        while (!custom.isEmpty()) {
            String curCustomID = custom.poll();
            // 用于存储当前客户的方案
            StringBuffer curCustomAssignment = new StringBuffer();
            curCustomAssignment.append(curCustomID).append(":");
            // 获取当前客户的实例
            Custom curCustom = customMap.get(curCustomID);
            // 获取当前满足当前客户qos的边缘节点
            PriorityQueue<SatisfySite> satisfySite = curCustom.getSatisfySite();
            // 获取满足的个数
            int siteCount = curCustom.getSiteCount();
            // 获取当前客户的需求
            int needBandWidth = customDemands.get(curCustomID);
            // 平均分配的话，每个边缘节点需要的带宽
            int average = (needBandWidth + 1) / siteCount;
            // 开始分配
            while (needBandWidth > 0 && satisfySite.peek() != null) {
                SatisfySite siteCur = satisfySite.poll();
                // 获取满足的边缘节点的ID 和 qos
                String curSiteID = siteCur.getSiteID();
                int qos = siteCur.getQos();
                // 获取剩余带宽
                int residualBandWidth = siteMap.get(curSiteID).getResidualBandWidth();
                if (residualBandWidth == 0) break;
                // 如果当前节点的剩余带宽大于平均值，且需要的带宽大于平均值
                if (residualBandWidth > average && needBandWidth >= average) {
                    // 更新方案
                    updateString(curCustomAssignment, curSiteID, average);
                    needBandWidth -= average;
                    // 对已分配的节点的流量更新；
                    updateSite(curSiteID, qos, residualBandWidth - average, satisfySite);

                } else if (residualBandWidth > needBandWidth && needBandWidth < average) {
                    // 更新方案
                    updateString(curCustomAssignment, curSiteID, needBandWidth);
                    needBandWidth = 0;
                    // 对已分配的节点的流量更新；
                    updateSite(curSiteID, qos, residualBandWidth - needBandWidth, satisfySite);


                } else if (residualBandWidth < average && needBandWidth >= average) {
                    // 更新方案
                    updateString(curCustomAssignment, curSiteID, residualBandWidth);
                    needBandWidth -= residualBandWidth;
                    // 对已分配的节点的流量更新；
                    updateSite(curSiteID, qos, 0, satisfySite);

                } else if (residualBandWidth < needBandWidth && needBandWidth < average) {
                    // 更新方案
                    updateString(curCustomAssignment, curSiteID, residualBandWidth);
                    needBandWidth -= residualBandWidth;
                    // 对已分配的节点的流量更新；
                    updateSite(curSiteID, qos, 0, satisfySite);
                }
            }
            ans[index] = curCustomAssignment.toString();
            index++;
        }
        return ans;
    }

    public static void updateSite(String name, int qos, int value, PriorityQueue<SatisfySite> s) {
        // 更新边缘节点，还需要放回优先级队列中
        siteMap.get(name).setResidualBandWidth(value);
        s.offer(new SatisfySite(name, qos));
    }

    public static void updateString(StringBuffer sb, String curSiteID, int value) {
        // 更新当前分配方案
        List<Integer> ids = searchAllIndex(sb.toString(), curSiteID);
        if (ids == null) {
            sb.append("<").append(curSiteID).append(",").append(value).append(">").append(",");
            return;
        }
        // 找到了对应字符
        int index = 0;
        for (int id : ids) {
            if (id == 0) continue;
            if (sb.charAt(id - 1) == '<') {
                index = id;
                break;
            }
        }
        if (index == 0) {
            sb.append("<").append(curSiteID).append(",").append(value).append(">").append(",");
        } else {
            // 如果之前分配过。则找到之前分配的值，在此基础上添加当前值
            int begin = index, end = index;
            for (int i = begin; i < sb.length(); i++) {
                if (sb.charAt(i) == ',') {
                    begin = i + 1;
                }
                if (sb.charAt(i) == '>') {
                    end = i;
                    break;
                }
            }
            String lastAssign = sb.toString().substring(begin, end);
            sb.delete(begin, end);
            sb.insert(begin, value + Integer.parseInt(lastAssign));
        }
    }

    public static List<Integer> searchAllIndex(String s1, String s2) {
        // 第一个出现的索引位置
        int a = s1.indexOf(s2);
        if (a == -1) return null;
        List<Integer> ans = new ArrayList<>();
        while (a != -1) {
            // 从这个索引往后开始第一个出现的位置
            ans.add(a);
            a = s1.indexOf(s2, a + 1);
        }
        return ans;
    }

    public static void init() {
        // 将边缘节点的剩余带宽全部置为其总带宽
        for (String s : siteMap.keySet()) {
            Site curSite = siteMap.get(s);
            curSite.setResidualBandWidth(curSite.getTotalBandWidth());
            siteMap.put(s, curSite);
        }
    }

}
