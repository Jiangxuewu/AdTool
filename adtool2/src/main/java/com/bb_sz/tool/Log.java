package com.bb_sz.tool;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/2.
 */

public class Log {

    public static void i(String tag, String msg) {
        System.out.print(new Date().toLocaleString() + ":" + tag + " " + msg + "\n");
    }

    public static void e(String tag, String msg) {
        System.err.print(new Date().toLocaleString() + ":" + tag + " " + msg + "\n");
    }
}
