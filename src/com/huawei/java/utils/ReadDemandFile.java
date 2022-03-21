package com.huawei.java.utils;

import com.huawei.java.pojo.Demand;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadDemandFile {

    private static final String DEMAND_CSV_PATH = "/data/demand.csv";
    private static BufferedReader dr;
    private static String[] demandCustom;
    private static int needCount;

    public static int getNeedCount() {
        return needCount;
    }

    public static void preRead() throws IOException {
        // 先预读读取demand.csv，知道总的时刻有多少
        try (BufferedReader br = new BufferedReader(new FileReader(DEMAND_CSV_PATH))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("mtime") && !line.equals("")) {
                    needCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Demand read() throws IOException {
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
                    if (cur[i].equals("\n\r") || cur[i].equals("\r\n") || cur[i].equals("\r") || cur[i].equals("\n"))
                        continue;
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
}
