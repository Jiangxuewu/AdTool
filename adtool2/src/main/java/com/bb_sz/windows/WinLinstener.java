package com.bb_sz.windows;

import com.bb_sz.shell.CMDTools;
import com.bb_sz.tool.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */

public class WinLinstener {

    public static void main(String[] args) {

    }

    public static void main2(ArrayList<String> options) {
        //get pwd name
        //rasdial.exe VPN ppxsk02 jxw3042
        //rasdial.exe /DISCONNECT
        if (null == options || options.size() != 5) {
            Log.i("", "param error.");
            return;
        }
        String connectName = options.get(0);
        String user = options.get(1);
        String pwd = options.get(2);
        int disconnectTime = Integer.valueOf(options.get(3));
        int connectTime = Integer.valueOf(options.get(4));
        int i = 0;
        for (i = 0;i > -1; i++) {
            CMDTools.exec("rasdial.exe /DISCONNECT");
            Log.i("", "disconnect");
            try {
                Thread.sleep(disconnectTime);
            } catch (InterruptedException ignored) {
            }
            CMDTools.exec("rasdial.exe " + connectName + " " + user + " " + pwd);
            Log.i("", "connectName = " + connectName + ", user = " + user + ", pwd = " + pwd + ", i = " + i);
            try {
                Thread.sleep(connectTime);
            } catch (InterruptedException ignored) {
            }
        }
//        CMDTools.exec("shutdown -r -f -t 10");
    }
}
