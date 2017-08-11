package com.bb_sz.helper360;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/13.
 */

public class QiHoo {
    private static final String ENCODING = "utf-8";

    public static void main(String[] args) {

        String key = "%E5%BF%8D%E8%80%85";//=%E5%BF%8D%E8%80%85
        String url = "http://zhushou.360.cn/search/index/?kw";
        String res = null;
        res = sendGetReq(url + "=" + key);
        if (null != res) {

        }
    }

    public static String sendGetReq(String urlStr) {

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
//            httpURLConnection.setRequestProperty("Cookie", cookie);
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
