package com.bb_sz.hprof;

import com.bb_sz.shell.FileTools;
import com.bb_sz.tool.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jiangxuewu on 2020/4/3.
 */
public class HprofResult {

    private static final String TAG = "HprofResult";

    private static HashMap<String, List<String>> leakResult;
    private static String leakedCls;

    public static void main(List<String> args) {
        if (null == args || args.isEmpty()) {
            Log.e(TAG, "list param null");
            return;
        }

        int size = args.size();
        String[] ps = new String[size];
        for (int i = 0; i < size; i++) {
            ps[i] = args.get(i);
        }
        main(ps);

    }

    public static void main(String[] args) {
        if (null == args || args.length < 1) {
            Log.e(TAG, "param null");
            return;
        }
        String fileDir = args[0];

        Log.e(TAG, "file dis is " + fileDir);

        File file = new File(fileDir);

        if (!file.exists() || !file.canRead()) {
            Log.e(TAG, "file can read " + fileDir);
            return;
        }

        if (null != leakResult) {
            leakResult.clear();
            leakResult = null;
        }
        leakResult = new HashMap<>();
        leakedCls = "";

        parseFile(file);


        saveResult(file);


        if (null != leakResult) {
            leakResult.clear();
            leakResult = null;
        }

    }

    private static void saveResult(File file) {
        if (null == leakResult || leakResult.isEmpty()) {
            return;
        }

        file.setWritable(true, false);

        File result = new File(file, "result.txt");

        if (!result.exists()) {
            try {
                result.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        result.setWritable(true, false);


        Set<Map.Entry<String, List<String>>> set = leakResult.entrySet();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date time = cal.getTime();
        String date = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(time);



        FileOutputStream outStream = null;
        OutputStreamWriter writer = null;
        try {
            outStream = new FileOutputStream(result, false);
            writer = new OutputStreamWriter(outStream);

            String line = "result:========" + date + "=====\n";
            writer.write(line + "\r\n\r\n\r\n");

            int i = 0;

            for (Map.Entry<String, List<String>> item : set) {
                i++;

                writer.write( "--------------------------------------------------------------------");


                List<String> value = item.getValue();


                String key = item.getKey();

                writer.write("\r\n\r\n\r\n " + i + ",Type->(count:" + (null == value ? 0 : value.size()) + ")" + key + "\r\n" + " in Activity:");


                if (null != value) {
                    for (String lineStr : value) {

                        writer.write("\t\t" + lineStr + "\r\n");

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static void parseFile(File file) {
        if (null == file) {
            return;
        }

        if (file.isDirectory()) {
            File[] lists = file.listFiles();
            if (null != lists) {
                for (File item : lists) {
                    parseFile(item);
                }
            }
        } else {
            try {
                parseFileLine(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void parseFileLine(File file) throws IOException {
        InputStream inStream = null;
        try {

            inStream = new FileInputStream(file);
            InputStreamReader inputReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inputReader);
            String line;
            //分行读取
            while ((line = buffReader.readLine()) != null) {
                parseLine(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                inStream.close();
            }
        }
    }

    private static void parseLine(String line) {
        if (null == line) {
            return;
        }
//        Log.i(TAG, "line ------- " + line);

        if (line.contains("dominate_element")) {// key
            int startIndex = line.indexOf("dominate_element\">");
            int endInde = line.indexOf("</span>");

//            Log.i(TAG, "line ------- " + line);
//            Log.i(TAG, "startIndex ------- " + startIndex);
//            Log.i(TAG, "endInde ------- " + endInde);

            if (!(startIndex > 0 && endInde > 0 && endInde > startIndex)) {
                return;
            }
            String key = line.substring(startIndex + "dominate_element\">".length() + 1, endInde);
//            String key = line;
//            Log.i(TAG, "key ------- " + key);


            List<String> value;
            if (leakResult.containsKey(key)) {
                value = leakResult.get(key);
            } else {
                value = new ArrayList<>();
            }

            if (null == leakedCls) {
                leakedCls = "errorNull";
            }
            value.add(new String(leakedCls));

            leakResult.put(key, value);

            leakedCls = null;

        } else if (line.contains("leaked ==")) {// leaked Activity
            int startIndex = line.indexOf("leaked ==");
            leakedCls = line.substring(startIndex + "leaked ==".length() + 1);

//            Log.i(TAG, "leakedCls ---- " + leakedCls);
        }
    }

}
