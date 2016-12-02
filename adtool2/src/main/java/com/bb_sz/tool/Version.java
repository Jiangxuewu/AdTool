package com.bb_sz.tool;

/**
 * Created by Administrator on 2016/12/2.
 */

public class Version {
    //be same of the file MANIFEST.MF
    private static final String VersionCode = "11";
    private static final String VersionName = "1.1.0";

    public static String getVersion() {
        return "Version:" + VersionName;
    }

    public static String getHelp() {

        return "help->" + "http://www.xw927.com";
    }

}
