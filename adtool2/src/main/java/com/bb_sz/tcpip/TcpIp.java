package com.bb_sz.tcpip;

import com.bb_sz.shell.CMDTools;
import com.bb_sz.tool.Log;

/**
 * Created by Jiangxuewu on 2018/11/20.
 */
public class TcpIp {

    public static void main(int post) {

//        int post = 3042;
        String cmd = "adb tcpip " + post;
        CMDTools.exec(cmd);

        cmd = "ping localhost -n 3 > nul";
        CMDTools.exec(cmd);

        cmd = "adb shell ifconfig wlan0 | findstr \"inet addr:\"";
        String value = CMDTools.exec(cmd);

        String ip = getIp(value);

        cmd = "adb connect " + ip + ":" + post;
        value = CMDTools.exec(cmd);

        Log.i("TcpIp", "value = " + value);
    }

    private static String getIp(String value) {
        //          inet addr:10.200.10.72  Bcast:10.200.11.255  Mask:255.255.254.0
        //          inet6 addr: fe80::d67d:fcff:fe07:1383/64 Scope: Link
        if (null == value) {
            return null;
        }
        int index = value.indexOf("Bcast");

        if (index > 0) {
            value = value.substring(0, index);
            value = value.trim();
            if (value.contains(":")) {
                return value.split(":")[1];
            }

        }
        return value;
    }
}
