package com.bb_sz.shell;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016/9/23.
 */

public class ZipUtils {

    public static void main(String[] args) {
//        zip("D:\\zip����", "D:\\���Խ��.zip");
        unZip("D:\\DengZong\\JPaySDK\\2017-01-12\\JPay", "D:\\DengZong\\JPaySDK\\2017-01-12\\JPay.jar");
    }

    /**
     * ���ܣ��� sourceDir Ŀ¼�µ������ļ����� zip ��ʽ��ѹ��������Ϊָ�� zip �ļ�
     *
     * @param sourceDir
     * @param zipFile
     */
    public static void zip(String sourceDir, String zipFile) {
        OutputStream os;
        try {
            os = new FileOutputStream(zipFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            ZipOutputStream zos = new ZipOutputStream(bos);
            File file = new File(sourceDir);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {//ֱ��ѹ�������ļ�ʱ��ȡ��Ŀ¼
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
            zos.closeEntry();
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ���ܣ�ִ���ļ�ѹ����zip�ļ�
     *
     * @param source
     * @param basePath ��ѹ���ļ���Ŀ¼
     * @param zos
     */
    private static void zipFile(File source, String basePath,
                                ZipOutputStream zos) {
        File[] files = new File[0];
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }
        String pathName;//�����·��(����ڴ�ѹ���ĸ�Ŀ¼)
        byte[] buf = new byte[1024];
        int length = 0;
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    pathName = file.getPath().substring(basePath.length() + 1)
                            + "/";
                    zos.putNextEntry(new ZipEntry(pathName));
                    zipFile(file, basePath, zos);
                } else {
                    pathName = file.getPath().substring(basePath.length() + 1);
                    InputStream is = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    zos.putNextEntry(new ZipEntry(pathName));
                    while ((length = bis.read(buf)) > 0) {
                        zos.write(buf, 0, length);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ܣ���ѹ zip �ļ���ֻ�ܽ�ѹ zip �ļ�
     *
     * @param zipfile
     * @param destDir
     */
    public static void unZip(String zipfile, String destDir) {
        destDir = destDir.endsWith("\\") ? destDir : destDir + "\\";
        byte b[] = new byte[1024];
        int length;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(new File(zipfile));
            Enumeration enumeration = zipFile.getEntries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                File loadFile = new File(destDir + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    loadFile.mkdirs();
                } else {
                    if (!loadFile.getParentFile().exists()) {
                        loadFile.getParentFile().mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream(loadFile);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    while ((length = inputStream.read(b)) > 0)
                        outputStream.write(b, 0, length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
