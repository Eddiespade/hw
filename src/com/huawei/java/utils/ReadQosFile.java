package com.huawei.java.utils;

import com.huawei.java.main.Main;
import com.huawei.java.pojo.Custom;
import com.huawei.java.pojo.SatisfySite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadQosFile {

    private static final String QOS_CSV_PATH = "/data/qos.csv";

    public static void read() throws IOException {
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
                            if (qosList[i].equals("\n\r") || qosList[i].equals("\r\n") || qosList[i].equals("\r") || qosList[i].equals("\n"))
                                continue;
                            String customID = customList.get(i - 1);
                            int qos = Integer.parseInt(qosList[i]);
                            if (qos < Main.qosLimit) {
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
}
