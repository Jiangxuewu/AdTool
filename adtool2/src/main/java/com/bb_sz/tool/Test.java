package com.bb_sz.tool;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/10.
 */

public class Test {


    public static void main(String[] args) {
//        String str = "汉中路Lss";
//        str = string2Unicode(str);
//        Log.i("", str);

        test();
    }

    public static void test() {
        RunAppsInfo record = new RunAppsInfo();

//        record.m_type = rs.getInt("m_type";
        record.pkg = "pkg";
        record.name = string2Unicode("name汉字");

        record.input = string2Unicode("input汉字");
        record.launcher = "launcher";
        record.open = "open";
//        record.w_main = rs.getInt("w_main";
//        record.w_search = rs.getInt("w_search";
//        record.w_infos = rs.getInt("w_infos";
//        record.w_input = rs.getInt("w_input";
//        record.w_install = rs.getInt("w_install";
//        record.w_play = rs.getInt("w_play";
//        record.time_out = rs.getInt("time_out";


//        record.swipe = rs.getInt("swipe";
//        record.swipe_time_out = rs.getInt("swipe_time_out";
//        record.before_start_sleep = rs.getInt("before_start_sleep";

//        record.clear_app_sleep = rs.getInt("clear_app_sleep";
//        record.clear_sd_sleep = rs.getInt("clear_sd_sleep";

//        record.launch_market_sleep = rs.getInt("launch_market_sleep";


//        record.run_blank = rs.getInt("run_blank";
//        record.install_ready_time = rs.getInt("install_ready_time";
//        record.open_ready_time = rs.getInt("open_ready_time";
        record.qh360type = "qh360type";
        record.qh360typeindex = "qh360typeindex";
        record.qh360typekey = string2Unicode("qh360typekey汉字");
//        record.down_times = rs.getInt("down_times";
        record.local_name = "local_name";
        String res = getContent(record);

        Log.i("", "res = " + res);
    }

    static class RunAppsInfo {
        protected int m_type;
        protected String pkg;
        protected String name;

        protected String input;
        protected String launcher;
        protected String open;
        protected int w_main;
        protected int w_search;
        protected int w_infos;
        protected int w_input;


        protected int w_install;
        protected int w_play;
        protected int time_out;
        protected int swipe;
        protected int swipe_time_out;

        protected int before_start_sleep;
        protected int clear_app_sleep;
        protected int clear_sd_sleep;
        protected int launch_market_sleep;

        protected int run_blank;

        protected int install_ready_time;
        protected int open_ready_time;
        protected String qh360type;
        protected String qh360typeindex;
        protected String qh360typekey;
        protected int down_times;
        protected String local_name;
        protected String url;

    }

    private static String getContent(RunAppsInfo info) {
        JSONObject json = new JSONObject();
        json.put("m_type", info.m_type);

        json.put("pkg", info.pkg);
        json.put("name", info.name);
        json.put("launcher", info.launcher);
        json.put("input", info.input);
        json.put("open", info.open);
        json.put("w_main", info.w_main);
        json.put("w_search", info.w_search);
        json.put("w_infos", info.w_infos);
        json.put("w_input", info.w_input);
        json.put("w_install", info.w_install);
        json.put("w_play", info.w_play);
        json.put("time_out", info.time_out);
        json.put("swipe", info.swipe);
        json.put("swipe_time_out", info.swipe_time_out);
        json.put("before_start_sleep", info.before_start_sleep);
        json.put("clear_app_sleep", info.clear_app_sleep);
        json.put("clear_sd_sleep", info.clear_sd_sleep);
        json.put("launch_market_sleep", info.launch_market_sleep);

        json.put("run_blank", info.run_blank);
        json.put("install_ready_time", info.install_ready_time);
        json.put("open_ready_time", info.open_ready_time);

        json.put("qh360type", info.qh360type);

        json.put("qh360typeindex", info.qh360typeindex);
        json.put("qh360typekey", info.qh360typekey);
        json.put("down_times", info.down_times);
        json.put("local_name", info.local_name);


        System.out.println(json.toString());
        String content = json.toString();
        System.out.println(content);
        return content;

    }

    public static String string2Unicode(String source) {
        if (source == null || source.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        char[] source_char = source.toCharArray();
        String unicode = null;
        for (int i = 0; i < source_char.length; i++) {
            unicode = Integer.toHexString(source_char[i]);
            if (unicode.length() <= 2) {
                unicode = "00" + unicode;
            }
            sb.append("\\u" + unicode);
        }
        return sb.toString();
    }

}
