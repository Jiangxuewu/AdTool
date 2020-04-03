package com.bb_sz.autotest;

import com.bb_sz.tool.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResHelper {

    static List<String> result = new ArrayList<>();

    public static void main(String[] args) {

        String filePath = "C:\\Users\\M7075J2M\\Desktop\\test_vskit_ids.txt";

        File file = new File(filePath);

        if (!file.exists() || !file.canRead()) {
            return;
        }

        InputStream inStream = null;
        try {

            inStream = new FileInputStream(file);
            InputStreamReader inputReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inputReader);
            String line;
            //分行读取
            while ((line = buffReader.readLine()) != null) {
                execEvent(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (String line : result) {
            System.out.println(line);
        }
    }

    private static void execEvent(String line) {

        if (line.contains("@+id/") && !line.contains("-->")) {
            line = line.substring(line.indexOf("@+id/") + "@+id/".length());
            line = line.replace("\"", "");
            line = "\"" + line + "\",";
            if (!result.contains(line)) {
                result.add(line);
            }
        }
    }
}
