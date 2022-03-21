package com.huawei.java.utils;

import java.io.IOException;

public class LoadFiles {

    public static void load() throws IOException {
        // 检查输出文件是否存在，若存在则删除；
        WriteOutputFile.delete();
        // 读取文件
        ReadDemandFile.preRead();
        ReadConfigFile.read();
        ReadSiteBandWidthFile.read();
        ReadQosFile.read();
    }
}
