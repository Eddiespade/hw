package com.huawei.java.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFile {

    private static final String OUTPUT_TXT_PATH = "/output/solution.txt";
    private static BufferedWriter bw;

    public static void deleteOutputFile() throws IOException {
        // 如果输出文件存在则删除，保证输出文件最新
        File outputFile = new File(OUTPUT_TXT_PATH);
        if (outputFile.exists() && !outputFile.delete()) {
            throw new IOException("输出文件删除失败");
        }
    }
    public static void writeOutput(String[] assignment) throws IOException {
        if (bw == null) bw = new BufferedWriter(new FileWriter(OUTPUT_TXT_PATH));

        for (String arr : assignment) {
            if (arr.charAt(arr.length() - 1) == ',') {
                bw.write(arr.substring(0, arr.length() - 1) + "\n");
            } else {
                bw.write(arr + "\n");
            }
        }
    }
    public static void close() throws IOException {
        bw.close();
    }

}
