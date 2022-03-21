package com.huawei.java.utils;

import com.huawei.java.main.Main;
import com.huawei.java.pojo.Site;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadSiteBandWidthFile {

    private static final String SITE_BANDWIDTH_CSV_PATH = "/data/site_bandwidth.csv";

    public static void read() throws IOException {
        int percentFive = (ReadDemandFile.getNeedCount() * 5) / 100;
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
                    Site cur = new Site(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[1]), percentFive);
                    // 添加进边缘节点的表中
                    Main.siteMap.put(data[0], cur);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
