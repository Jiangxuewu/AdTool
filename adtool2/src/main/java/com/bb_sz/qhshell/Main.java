package com.bb_sz.qhshell;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/15.
 */

public class Main {
    public static void main(String[] args) {

        String dexPath = "D:\\Android\\github\\AdTool\\adtool2\\out\\qh_shell\\classes.dex";

        File dexFile = new File(dexPath);
        if (!dexFile.exists()) {
            return;
        }

        try {
            byte[] dexBytes = readFileBytes(dexFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 以二进制读出文件内容
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static byte[] readFileBytes(File file) throws IOException {
        byte[] arrayOfByte = new byte[1024];
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        while (true) {
            int i = fis.read(arrayOfByte);
            if (i != -1) {
                localByteArrayOutputStream.write(arrayOfByte, 0, i);
            } else {
                return localByteArrayOutputStream.toByteArray();
            }
        }
    }
}
