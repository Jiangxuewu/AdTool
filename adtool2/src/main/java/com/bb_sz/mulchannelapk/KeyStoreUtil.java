package com.bb_sz.mulchannelapk;

/**
 * Created by Administrator on 2016/12/6.
 */

public class KeyStoreUtil {


    public static void main(String[] args) {
//        String signCMD = "jarsigner -verbose -keystore " + keyStoreFile + " -signedjar " + signedApk + "" + shellApkOutFile + align

//        String aliasName = "test.keystore";
//        String keystorePath = "D:\\DengZong\\2016-09-12_ttby\\zhifusdk\\Templates1\\channel_out\\test.keystore";
//
//        String signCMD = "keytool -genkey -alias " +
//                aliasName +
//                " -keyalg RSA -validity 100 -keystore " +
//                keystorePath;
//
//        CMDTools.exec(signCMD);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

//        try {
//            String filePath = "C:\\Users\\Administrator\\Desktop\\ty.txt";
//            File file = new File(filePath);
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                upload(line);
//            }
//        } catch (Exception e) {
//
//        }

        //{"Status":true,"Description":"","CurrentIssueNo":"1728259","Remain":35.217410199999996,"LastIssueNo":"1728258","LastIssueOpenNum":"57495","FengDan":0}
        String result = "{\"Status\":true,\"Description\":\"\",\"CurrentIssueNo\":\"1728259\",\"Remain\":35.217410199999996,\"LastIssueNo\":\"1728258\",\"LastIssueOpenNum\":\"null\",\"FengDan\":0}";
        int index = result.indexOf("\"LastIssueNo\":\"");
        System.out.print("index = " + index + " \n");
        if (index < 0) {
            return;
        }
        String No = result.substring(index + "\"LastIssueNo\":\"".length(), index + "\"LastIssueNo\":\"".length() + 7);

        index = result.indexOf("\"LastIssueOpenNum\":\"");
        System.out.print("index = " + index + " \n");
        if (index < 0) {
            return;
        }
        String OpenNum = result.substring(index + "\"LastIssueOpenNum\":\"".length(), index + "\"LastIssueOpenNum\":\"".length() + 5);

        System.out.print("No = " + No + " \n");
        System.out.print("OpenNum = " + OpenNum + " \n");
    }


}
