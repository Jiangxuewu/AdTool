package com.bb_sz.umhelper;

import com.bb_sz.mail.Email;
import com.bb_sz.tool.Log;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.mail.MessagingException;


/**
 * Created by Administrator on 2017/7/6.
 */

public class UMHelper {

    private static final String ENCODING = "utf-8";
    public static String cookie = "um_lang=zh; pgv_pvi=529788928; cn_f7169cbd4377cl2a77d3_dplus=%7B%22distinct_id%22%3A%20%22156964f68d3777-0bee8f2a236d0f-404c0628-1fa400-156964f68d48ea%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201478248735%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201478248735%7D; cn_3e15a5f04a2742ae1e32_dplus=%7B%22distinct_id%22%3A%20%2215905ad30e925c-0208b50ba9f5de-6a191178-1fa400-15905ad30ea165%22%2C%22%E6%98%AF%E5%90%A6%E7%99%BB%E5%BD%95%22%3A%20%22%E5%B7%B2%E7%99%BB%E5%BD%95%22%2C%22%E7%94%A8%E6%88%B7%E5%90%8D%2B%E6%98%B5%E7%A7%B0%22%3A%20%22304261930%40qq.com%2B304261930%40qq.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201481858959%2C%22initial_view_time%22%3A%20%221481858943%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fi.umeng.com%2Fuser%2Fproducts%3Fspm%3D0.0.0.0.U9SKGo%22%2C%22initial_referrer_domain%22%3A%20%22i.umeng.com%22%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201481858959%7D; cn_1255795631_dplus=%7B%22distinct_id%22%3A%20%22159ac052e5d98e-01cded676d1d7b-6a191178-1fa400-159ac052e5e620%22%7D; cn_session_id_e41519ea7a5b8g5d6b18=1484649741; cn_e41519ea7a5b8g5d6b18_dplus=%7B%22distinct_id%22%3A%20%22159ac05384bab-0ecba95ea38e2a-6a191178-1fa400-159ac05384c61%22%2C%22%24initial_time%22%3A%201484620941%2C%22%24initial_referrer%22%3A%20%22https%3A%2F%2Fadplus.umeng.com%2F%3Fclose%3D1%26show_protocol%3D1%26key%3D97eedbabfd2bb89e50307cd0277dbd86_1484649734_12525%22%2C%22%24initial_referring_domain%22%3A%20%22adplus.umeng.com%22%7D; l=Ajc335qv4AuAduy02WKaEbGJRyGANQte; _ga=GA1.2.310020993.1475132570; cn_1259827933_dplus=%7B%22distinct_id%22%3A%20%221596c5c035b6c2-098c9767d713a2-6a191178-1fa400-1596c5c035c916%22%2C%22initial_view_time%22%3A%20%221494468706%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.umeng.com%2F%22%2C%22initial_referrer_domain%22%3A%20%22www.umeng.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201497496401%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201497496401%7D; cn_ab16623e58941o0088a4_dplus=%7B%22distinct_id%22%3A%20%22159ac041c1ea45-0f60aa71339269-6a191178-1fa400-159ac041c1fca%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201498128353%2C%22utm_source%22%3A%20%22zzbanner12%22%2C%22initial_view_time%22%3A%20%221484648279%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.umeng.com%2F%22%2C%22initial_referrer_domain%22%3A%20%22www.umeng.com%22%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201498128353%7D; cn_1260769985_dplus=%7B%22distinct_id%22%3A%20%2215901a0288e72-0eda2150c4b98f-6a191178-1fa400-15901a0288fa66%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201498128353%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201498128353%7D; umlid_5938b4561c5dd077360011ea=20170706; __utmt=1; CNZZDATA1258498910=22763969-1471396564-%7C1499304157; CNZZDATA1253424029=193568835-1475135190-http%253A%252F%252Fmobile.umeng.com%252F%7C1499304043; umlid_5490f600fd98c52678000244=20170706; __utma=151771813.431653530.1471400505.1499218906.1499308322.377; __utmb=151771813.12.10.1499308322; __utmz=151771813.1471400505.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); CNZZDATA1259864772=172728588-1471396554-http%253A%252F%252Fmobile.umeng.com%252F%7C1499306767; cn_1259864772_dplus=%7B%22distinct_id%22%3A%20%22156964e09a99b-01aeb52aa7e63f-404c0628-1fa400-156964e09aa804%22%2C%22initial_view_time%22%3A%20%221471396554%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fmobile.umeng.com%2Fapps%2Feec0005e66e85e76d8ac3b75%2Fappkey%22%2C%22initial_referrer_domain%22%3A%20%22mobile.umeng.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%2C%22sp%22%3A%20%7B%22%E6%98%AF%E5%90%A6%E7%99%BB%E5%BD%95%22%3A%20true%2C%22USER%22%3A%20%22304261930%40qq.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201499308657%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201499308657%7D%7D; cn_1258498910_dplus=%7B%22distinct_id%22%3A%20%221596c5c035b6c2-098c9767d713a2-6a191178-1fa400-1596c5c035c916%22%2C%22initial_view_time%22%3A%20%221483581671%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.bb-sz.com%2F%22%2C%22initial_referrer_domain%22%3A%20%22www.bb-sz.com%22%2C%22sp%22%3A%20%7B%22%24recent_outside_referrer%22%3A%20%22www.bb-sz.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201499308673%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201499308673%7D%7D; cna=Y1a3DzkWARgCAXeJIHBSUA7U; umplusappid=umcenter; __ufrom=http://www.umeng.com/; cn_a61627694930aa9c80cf_dplus=%7B%22distinct_id%22%3A%20%22156964a9ea7402-09002262c00f71-404c0628-1fa400-156964a9ea863b%22%2C%22initial_view_time%22%3A%20%221471399450%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DIE3uJwCXoZLu8mjVBAxgAJQUu7QO0LjlNAfk8ysWkJK%26wd%3D%26eqid%3D8d840c93001011380000000657b3c959%22%2C%22initial_referrer_domain%22%3A%20%22www.baidu.com%22%2C%22%E7%94%A8%E6%88%B7%E5%90%8D%22%3A%20%22304261930%40qq.com%22%2C%22sp%22%3A%20%7B%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201499308658%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201499308658%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D%7D; UM_distinctid=156964a9ea7402-09002262c00f71-404c0628-1fa400-156964a9ea863b; umengplus_name=304261930%40qq.com; umplusuuid=882b83d165df8e17ee9f82bbe5d39015; isg=AkhIJ7QchTac1-dce42C5zVeGbZQZ6OP_jy9SAL5DkO23epHqwN0ik2fIYNW; ummo_ss=BAh7CUkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjoGRVRbCEkiCVVzZXIGOwBGWwZvOhNCU09OOjpPYmplY3RJZAY6CkBkYXRhWxFpWWkBkGkB9mkAaQH9aQGYaQHFaStpfWkAaQdpSUkiGXVQQXkzSE9LRTJ2RU10bnBhNmN3BjsAVEkiD3VtcGx1c3V1aWQGOwBGIiU4ODJiODNkMTY1ZGY4ZTE3ZWU5ZjgyYmJlNWQzOTAxNUkiEF9jc3JmX3Rva2VuBjsARkkiMUQ1QmE2SU5VeGVaZFcyQlZUeUpwUVoyZzRkQzltMDljWmljSFhQbHgvbkE9BjsARkkiD3Nlc3Npb25faWQGOwBUSSIlMTYzZGQwM2Y4Yjk0MDA3YzhkNDlkZGFjNjBmNzE0OTMGOwBG--b0ddb62030d947e7318347ac8302ded32dd7a5e3";

    public static HashMap<String, Integer> resultMap;//cid all date
    public static HashMap<String, Integer> appResultMap;//app all install
    public static TreeMap<String, HashMap<String, Integer>> appCidDetailMap;// app cid install
    static StringBuffer sb = null;
    public static boolean debug = true;

    public static void main(String[] args) {
        main("11");
    }

    public static void main(String args) {
        debug = null != args && args.length() > 0;
        Log.i("main", "start, debug is " + debug);
        if (null == cookie || cookie.length() <= 0) return;
        boolean run = true;
        sb = null;
        sb = new StringBuffer();
        while (run) {
            sb = null;
            sb = new StringBuffer();
            resultMap = null;
            appResultMap = null;
            appCidDetailMap = null;
            if (isRightTime() || debug) {
                getAppsDetails(1, 300, "install_yesterday", "desc");
                sendResult();
//                sendResult2();
                sendResult3();
                int i = 60;
                if (sb.toString().length() > 10) {
                    Log.i("SB", sb.toString());
                    sendEmail();
                } else {
                    i = 1;
                }
                try {
                    Thread.sleep(1000 * 60 * i);
                } catch (InterruptedException ignored) {
                }
            }
            try {
                Thread.sleep(1000 * 60 * 30);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static boolean isRightTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Log.i("hour", "" + hour);
        return hour == 8 || debug;
    }

    private static void sendResult() {
        if (null == resultMap || resultMap.size() == 0) return;

        resultMap = sort(resultMap);

        Set<Map.Entry<String, Integer>> entry = resultMap.entrySet();
        sb.append("======================\u5404\u4e2a\u6e20\u9053\u7684\u7528\u6237\u603b\u6570 start=====================</br>");
        for (Map.Entry<String, Integer> item : entry) {
            sb.append(item.getKey()).append(",").append(item.getValue()).append("</br>");
        }
        sb.append("======================\u5404\u4e2a\u6e20\u9053\u7684\u7528\u6237\u603b\u6570 end=====================</br>");
    }

    private static HashMap<String, Integer> sort(HashMap<String, Integer> map) {

        Set<Map.Entry<String, Integer>> mapEntries = map.entrySet();

        List<Map.Entry<String, Integer>> aList = new LinkedList<Map.Entry<String, Integer>>(mapEntries);

        Collections.sort(aList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });
        HashMap<String, Integer> map2 = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : aList) {
            map2.put(entry.getKey(), entry.getValue());
        }

        return map2;
    }

    private static void sendResult2() {
        if (null == appResultMap || appResultMap.size() == 0) return;

        appResultMap = sort(appResultMap);

        Set<Map.Entry<String, Integer>> entry = appResultMap.entrySet();
        sb.append("======================\u5404\u4e2a\u6e38\u620f\u7684\u7528\u6237\u603b\u6570 start=====================</br>");
        for (Map.Entry<String, Integer> item : entry) {
            sb.append(item.getKey()).append(",").append(item.getValue()).append("</br>");
        }
        sb.append("======================\u5404\u4e2a\u6e38\u620f\u7684\u7528\u6237\u603b\u6570 end=====================</br>");
    }

    private static void sendResult3() {
        if (null == appCidDetailMap || appCidDetailMap.size() == 0) return;

        Set<Map.Entry<String, HashMap<String, Integer>>> entry = appCidDetailMap.entrySet();
        sb.append("======================\u5404\u4e2a\u6e38\u620f\u5404\u81ea\u6e20\u9053\u7684\u7528\u6237\u603b\u6570 start============</br>");
        for (Map.Entry<String, HashMap<String, Integer>> item : entry) {
            sb.append(item.getKey()).append(":</br>");
            HashMap<String, Integer> tmp = sort(item.getValue());
            Set<Map.Entry<String, Integer>> tmpEn = tmp.entrySet();
            for (Map.Entry<String, Integer> tmpItem : tmpEn) {
                if (tmpItem.getValue() > 0) {
                    sb.append("------------").append(tmpItem.getKey()).append(",").append(tmpItem.getValue()).append("</br>");
                }
            }
        }
        sb.append("======================\u5404\u4e2a\u6e38\u620f\u5404\u81ea\u6e20\u9053\u7684\u7528\u6237\u603b\u6570 end============</br>");
    }

    private static void sendEmail() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Date time = cal.getTime();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(time);
            Email.sendEmail(date + " Umeng Data", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAppsDetails(int page, int perPage, String sort, String order) {
        String url = "http://mobile.umeng.com/apps/get_apps_stats_details?page=" + page + "&per_page=" + perPage
                + "&type=all-apps-list&show_all=false&sort_metric=" + sort + "&order=" + order;
        Log.i("http", "url = " + url);
        String res = sendGetReq(url, cookie);
        Log.i("http", "res = " + res);
        if (null != res) {
            AppsDetailRes appsDetailRes = new Gson().fromJson(res, AppsDetailRes.class);
            if (null != appsDetailRes) {
                String result = appsDetailRes.getResult();
                int total = appsDetailRes.getTotal();//游戏总数
                List<AppsDetailRes.StatsBean> stats = appsDetailRes.getStats();
                if ("success".equals(result) && total > 0 && null != stats) {
                    for (AppsDetailRes.StatsBean item : stats) {
                        if ("yes".equals(item.getGame())) {
                            if (null == appResultMap) {
                                appResultMap = new HashMap<>();
                            }
                            if (!appResultMap.containsKey(item.getName()) && item.getInstall_yesterday() > 0) {
                                appResultMap.put(item.getName(), item.getInstall_yesterday());
                            }
                            if (item.getInstall_yesterday() > 0) {
                                getAppChannelDetails(item.getName(), item.getApp_id());
                            }
                        }
                    }
                }
            }
        }
    }

    public static void getAppChannelDetails(String appName, String app_id) {
        String url = "http://mobile.umeng.com/apps/" + app_id + "/game_reports/load_table_data?page=1&per_page=30&stats=channel_stats_details&daytime=yesterday";
        Log.i("http", "url = " + url);
        String res = sendGetReq(url, cookie);
        Log.i("http", "res = " + res);
        if (null != res) {
            AppChannelDetailRes appChannelDetailRes = new Gson().fromJson(res, AppChannelDetailRes.class);
            if (null != appChannelDetailRes) {
                String result = appChannelDetailRes.getResult();
                int total = appChannelDetailRes.getTotal();//渠道总数

                if (null == appCidDetailMap) {
                    appCidDetailMap = new TreeMap<>();
                }
                HashMap<String, Integer> tmpTree = new HashMap<>();
                List<AppChannelDetailRes.StatsBean> stats = appChannelDetailRes.getStats();
                if ("success".equals(result) && total > 0 && null != stats) {
                    for (AppChannelDetailRes.StatsBean item : stats) {
                        if (item.getInstallation() > 0) {
                            if (null == resultMap) {
                                resultMap = new HashMap<>();
                            }
                            if (resultMap.containsKey(item.getChannel_name())) {
                                resultMap.put(item.getChannel_name(), item.getInstallation() + resultMap.get(item.getChannel_name()));
                            } else {
                                resultMap.put(item.getChannel_name(), item.getInstallation());
                            }

                            Log.i("CID", "app " + appName + ", install " + item.getInstallation() + ", key " + item.getChannel_name() + ", value " + resultMap.get(item.getChannel_name()));

                            if (tmpTree.containsKey(item.getChannel_name())) {
                                tmpTree.put(item.getChannel_name(), item.getInstallation() + tmpTree.get(item.getChannel_name()));
                            } else {
                                tmpTree.put(item.getChannel_name(), item.getInstallation());
                            }
//                            Log.i(appName, "" + item.getChannel_name() + "->" + item.getInstallation());
                        }
                    }
                }
                if (!appCidDetailMap.containsKey(appName)) {
                    appCidDetailMap.put(appName, tmpTree);
                }
            }
        }
    }


    public static String sendGetReq(String urlStr, String cookie) {

        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // 设置请求体的类型
//            httpURLConnection.setRequestProperty("Connection", "keep-alive");
//            httpURLConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
//            httpURLConnection.setRequestProperty("X-CSRF-Token", "D5Ba6INUxeZdW2BVTyJpQZ2g4dC9m09cZicHXPlx/nA=");
//            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
//            httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
//            httpURLConnection.setRequestProperty("Referer", "http://mobile.umeng.com/apps");
//            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//            httpURLConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            httpURLConnection.setRequestProperty("Cookie", cookie);
//            httpURLConnection.setRequestProperty("", "");

            httpURLConnection.connect();

            // 获得服务器响应的结果和状态码
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                return null;
            }
            BufferedReader is = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), ENCODING));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = is.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
