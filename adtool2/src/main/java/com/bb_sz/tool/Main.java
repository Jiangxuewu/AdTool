package com.bb_sz.tool;

import com.bb_sz.shell.MergeDex;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/8/12.
 */
public class Main {

    public static void main(String[] args) {
        TManager.main(args);
    }

    private static void test(String[] args){
        String mobile = args[0];

        StringBuilder sb = new StringBuilder();

        sb.append("mobile").append("=").append(mobile);
        sb.append("mobileCountryCode").append("=").append("+86");
        sb.append("type").append("=").append("32");
        sb.append("382700b563f4");

        String sign = null;
        try {
            sign = MergeDex.md5(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert sign != null;

        StringBuilder param = new StringBuilder();

        param.append("mobile").append("=").append(mobile).append("&");
        param.append("mobileCountryCode").append("=").append("+86").append("&");
        param.append("type").append("=").append("32").append("&");
        param.append("sig").append("=").append(sign);

        try {
            URL url = new URL("https://id.kuaishou.com/pass/game/sms/requestMobileCode");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置参数
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", "" + param.toString().length());
            conn.setRequestProperty("Cookie", getCookie());

            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            conn.connect();

            //建立输入流，向指向的URL传入参数
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(URLEncoder.encode(param.toString()));
            dos.flush();
            dos.close();

            // 获得服务器响应的结果和状态码
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader is = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = is.readLine()) != null) {
                    response.append(line).append("\n");
                }
                Log.i("", "response:" + response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCookie() {

        return "did=da39a3ee5e6b4b0d3255bfef95601890afd80709; soft_did=ANDROID_31aa6a4bf948b157_S8PJRCQ4JFWGTGFM; _locale=en_US; _appVer=1.7.63; _channel=uc; client_key=3c2cd3f3";
    }

}
