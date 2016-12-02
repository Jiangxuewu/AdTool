package com.bb_sz.tool;

/**
 * Created by Administrator on 2016/12/2.
 */

public class Log {

    public static void i(String tag, String msg){
        System.out.print(tag + " " + msg + "\n");
    }

    public static void e(String tag, String msg){
        System.err.print(tag + " " + msg + "\n");
    }
}
