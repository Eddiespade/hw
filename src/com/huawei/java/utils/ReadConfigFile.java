package com.huawei.java.utils;

import com.huawei.java.main.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadConfigFile {

    private static final String CONFIG_INI_PATH = "/data/config.ini";

    public static void read() throws IOException {
        // 读取config.ini
        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_INI_PATH))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("qos_constraint")) {
                    Main.qosLimit = Integer.parseInt(line.split("=")[1]);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
