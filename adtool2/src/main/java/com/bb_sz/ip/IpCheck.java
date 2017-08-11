package com.bb_sz.ip;

/**
 * Created by Administrator on 2017/7/11.
 */

public class IpCheck {
    private static final String HTTP_LOCAL_HOST = "/sdcard/TM";

    public static void main(String[] args){

        String url = "http://10.150.15.143/170714/3c6e924cb4b0446bd5a548464d151688/com.think.game.qh_302.apk";

        System.out.print(getLocalPath(url));

    }

    private static String getLocalPath(String url) {
        url = url.substring("http://".length());
        String[] fileStr = url.split("/");
        StringBuilder result = new StringBuilder(HTTP_LOCAL_HOST);
        int length = fileStr.length;
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                result.append("/").append(fileStr[i]);
            }
        }
        return result.toString();
    }
}
