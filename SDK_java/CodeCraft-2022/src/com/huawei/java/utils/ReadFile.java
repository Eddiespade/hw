package com.huawei.java.utils;

import com.huawei.java.main.Main;
import com.huawei.java.pojo.Custom;
import com.huawei.java.pojo.Demand;
import com.huawei.java.pojo.SatisfySite;
import com.huawei.java.pojo.Site;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile {

    private static final String SITE_BANDWIDTH_CSV_PATH = "/data/site_bandwidth.csv";
    private static final String QOS_CSV_PATH = "/data/qos.csv";
    private static final String CONFIG_INI_PATH = "/data/config.ini";
    private static final String DEMAND_CSV_PATH = "/data/demand.csv";
    private static int qosLimit;

    private static BufferedReader dr;
    private static String[] demandCustom;

    public static void readConfig() throws IOException {
        // 读取config.ini
        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_INI_PATH))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("qos_constraint")) {
                    qosLimit = Integer.parseInt(line.split("=")[1]);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readSiteBandWidth() throws IOException {
        // 读取site_bandwidth.csv
        try (BufferedReader br = new BufferedReader(new FileReader(SITE_BANDWIDTH_CSV_PATH))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                // 去除两端的空字符
                line = line.trim();
                if (!line.equals("") && !line.startsWith("site_name")) {
                    // data[0] 代表 边缘节点的ID， data[1] 代表 边缘节点的带宽
                    String[] data = line.split(",");
                    // 新建当前边缘节点， 初始时，剩余的带宽和总共带宽一致
                    Site cur = new Site(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[1]));
                    // 添加进边缘节点的表中
                    Main.siteMap.put(data[0], cur);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readQos() throws IOException {
        // 读取qos.csv 可以获取客户的ID 及 到各节点的qos值
        try (BufferedReader br = new BufferedReader(new FileReader(QOS_CSV_PATH))) {
            // 为了在存储满足qos的时候知道当前的客户节点ID
            List<String> customList = new ArrayList<>();
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.equals("")) {
                    // 根据第一行创建所有的用户节点
                    if (line.startsWith("site_name")) {
                        String[] customID = line.split(",");
                        for (int i = 1; i < customID.length; i++) {
                            customList.add(customID[i]);
                            Custom cur = new Custom(customID[i]);
                            Main.customMap.put(customID[i], cur);
                        }
                    }
                    // 为客户节点添加到满足qos要求的边缘节点
                    else {
                        // qos[0] 为边缘节点ID， qos[i]为边缘节点到第i个客户节点的qos
                        String[] qosList = line.split(",");
                        for (int i = 1; i < qosList.length; i++) {
                            if (qosList[i].equals("\n\r") || qosList[i].equals("\r\n") || qosList[i].equals("\r") || qosList[i].equals("\n")) continue;
                            String customID = customList.get(i - 1);
                            int qos = Integer.parseInt(qosList[i]);
                            if (qos < qosLimit) {
                                SatisfySite site = new SatisfySite(qosList[0], qos);
                                Main.customMap.get(customID).addSatisfySite(site);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Demand readDemand() throws IOException {
        if (dr == null) {
            dr = new BufferedReader(new FileReader(DEMAND_CSV_PATH));
        }
        Demand demand = null;
        String line = dr.readLine();
        // 如果当前行不为空，则直接返回
        if (line != null) {
            line = line.trim();
            if (line.startsWith("mtime")) {
                demandCustom = line.split(",");
                line = dr.readLine();
                line = line.trim();
            }
            if (!line.equals("")) {
                String[] cur = line.split(",");
                Map<String, Integer> customDemand = new HashMap<>();
                for (int i = 1; i < cur.length; i++) {
                    if (cur[i].equals("\n\r") || cur[i].equals("\r\n") || cur[i].equals("\r") || cur[i].equals("\n")) continue;
                    customDemand.put(demandCustom[i], Integer.parseInt(cur[i]));
                }
                demand = new Demand(cur[0], customDemand);
            }
        }
        // 如果为空则关闭流
        else {
            if (dr != null) {
                dr.close();
                dr = null;
            }
        }
        return demand;
    }

    public static void main(String[] args) throws IOException {
        readConfig();
        readSiteBandWidth();
        readQos();
        System.out.println(1);
    }

}
