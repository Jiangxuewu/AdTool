package com.bb_sz.tool;

import com.bb_sz.mulchannelapk.MulCSignApk;
import com.bb_sz.shell.FileTools;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/12.
 */
public class Main {

    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        TManager.main(args);
//        String str = " \n ";
//        System.out.print("str = " + str);
//        str = str.replaceAll("[\\t\\n\\r ]", "");
//        System.out.print("str = " + str);
//
//        str = "  ";
//        System.out.print("str = " + str);
//        str = str.replaceAll("[\\t\\n\\r ]", "");
//        System.out.print("str = " + str);

//        String test = "asdfas\r\r\r489\r\r48";
//        test = test.replaceAll("\\r", "\n");
//        System.out.print("test = " + test);

//        String time = "2017-03-31 17:42:11";
//        Date date = null;
//        try {
//            date = sdf2.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Calendar doneDar = Calendar.getInstance();
//        doneDar.setTime(date);
//        int week = doneDar.get(Calendar.DAY_OF_WEEK) - 1;
//        int year = doneDar.get(Calendar.YEAR);
//        int month = doneDar.get(Calendar.MONTH) + 1;
//        int day = doneDar.get(Calendar.DAY_OF_MONTH);
////        doneDar.set(year, month, day, 0, 0, 0);
//        doneDar.set(Calendar.HOUR_OF_DAY, 0);
//        doneDar.set(Calendar.MINUTE, 0);
//        doneDar.set(Calendar.SECOND, 0);
//
//        long doneTime = doneDar.getTime().getTime();
//        System.out.print(" year = " + year + "\n");
//        System.out.print(" month = " + month + "\n");
//        System.out.print(" day = " + day + "\n");
//        System.out.print(" week = " + week + "\n");
//        System.out.print(" doneTime = " + doneTime + "\n");
//        System.out.print(" curTime = " + System.currentTimeMillis()+ "\n");
    }

    public static void test() {
        File file = new File("D:\\DengZong\\TuiGuang\\Templates");
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory() && f.getName().contains("_")) {
                deleteCusDir(f);
            }
        }
    }

    private static void deleteCusDir(File f) {
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory() && file.getName().contains("_Templates")) {
//                startDelOldCode(file);
            } else if (file.isFile() && file.getName().equals("settings.txt")) {
//                addBbszPath(file, "\nbbszPath=D:\\Android\\github\\MyShell\\jpaysdk\\out\\jpaysdk-debug");
            } else if (file.isDirectory() && file.getName().startsWith("channel_") && !file.getName().equals("channel_out")) {
                FileTools.deleteDir(file);
            }
        }
    }

    private static void addBbszPath(File file, String text) {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(text, 0, text.length());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startDelOldCode(File file) {
        File[] files = file.listFiles();
        for (File f : files) {
            if ("assets".equals(f.getName())) {
                FileTools.deleteDir(f.getAbsolutePath() + File.separator + "cs_res");
                FileTools.deleteDir(f.getAbsolutePath() + File.separator + "cus_service");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "jpay_config.xml");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "jpay_uid.txt");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "sz_game_center.png");
            } else if ("lib".equals(f.getName())) {
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "arm64-v8a" + File.separator + "libjpaysdk.so");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "armeabi" + File.separator + "libjpaysdk.so");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "armeabi-v7a" + File.separator + "libjpaysdk.so");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "mips" + File.separator + "libjpaysdk.so");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "mips64" + File.separator + "libjpaysdk.so");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "x86" + File.separator + "libjpaysdk.so");
                FileTools.deleteFile(f.getAbsolutePath() + File.separator + "x86_64" + File.separator + "libjpaysdk.so");
            } else if ("smali".equals(f.getName())) {
                FileTools.deleteDir(f.getAbsolutePath() + File.separator + "com" + File.separator + "bb_sz");
                FileTools.deleteDir(f.getAbsolutePath() + File.separator + "com" + File.separator + "umeng");
                FileTools.deleteDir(f.getAbsolutePath() + File.separator + "u");
            }
        }
    }
}
