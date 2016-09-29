package com.bb_sz.tool;

import com.bb_sz.adtool.AdTool;
import com.bb_sz.shell.Shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/23.
 * <p>1, add ad sdk</p>
 * <p>2, add shell for apk</p>
 */
public class TManager {

    public static final boolean debug = true;

    /***
     * -v
     * -h
     * -ad add ad sdk
     * -
     * -shell add shell for sdk
     *
     * @param args
     */

    public static void main(String[] args) {
        if (debug) System.out.print("[i] [TManager] [main] \n");
        if (null == args) return;
        initOptions(args);
        run();
    }

    private static void run() {
        if (debug) System.out.print("[i] [TManager] [run] \n");
        Set<Map.Entry<String, ArrayList<String>>> set = optionsMap.entrySet();
        Iterator<Map.Entry<String, ArrayList<String>>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = it.next();
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            run(key, value);
        }
    }

    private static void run(String line, ArrayList<String> options) {
        if (null == line) return;
        if ("-v".equals(line) || "-version".equals(line)) {

        } else if ("-h".equals(line) || "-help".equals(line)) {

        } else if ("-a".equals(line) || "-ad".equals(line)) {// add sd sdk
            runAd(options);
        } else if ("-s".equals(line) || "-shell".equals(line)) {// shell for apk
            if (null != options && options.size() > 0)
                Shell.run(options.get(0));
        }

    }

    private static void runAd(ArrayList<String> options) {
        String[] args = new String[options.size()];
        options.toArray(args);
        AdTool.main(args);
    }

    private static void initOptions(String[] args) {
        if (debug) System.out.print("[i] [TManager] [initOptions] \n");
        int count = args.length;
        for (int i = 0; i < count; i++) {
            if (args[i].startsWith("-")) {
                if (i + 1 < count)
                    for (int j = i + 1; j < count; j++) {
                        if (!args[j].startsWith("-1")) {
                            addOptions(args[i], args[j]);
                        }
                    }
                else
                    addOptions(args[i], "");
            }
        }
    }

    private static HashMap<String, ArrayList<String>> optionsMap = new HashMap<String, ArrayList<String>>();

    private static void addOptions(String line, String options) {
        ArrayList<String> list = null;
        if (optionsMap.containsKey(line)) {
            list = optionsMap.get(line);
            if (null != list)
                list.add(options);
        } else {
            list = new ArrayList<String>();
            list.add(options);
        }
        optionsMap.put(line, list);
    }
}
