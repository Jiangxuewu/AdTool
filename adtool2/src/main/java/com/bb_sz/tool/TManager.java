package com.bb_sz.tool;

import com.bb_sz.adtool.AdTool;
import com.bb_sz.mulchannelapk.MulCSignApk;
import com.bb_sz.shell.Shell;
import com.bb_sz.tycp.TyCaiPiao;
import com.bb_sz.tycp.TyHelper;
import com.bb_sz.umhelper.UMHelper;
import com.bb_sz.windows.WinLinstener;

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

    public static final boolean debug = false;

    private static final String TAG = TManager.class.getSimpleName();


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
        if (debug) Log.i(TAG, "main");
        if (null == args) return;
        //��������
        initOptions(args);
        //�õ� ���е����������Ӧ�Ĳ���ֵ,�������optionsMap��
        run();
    }

    private static void run() {
        if (debug) Log.i(TAG, "run");
        Set<Map.Entry<String, ArrayList<String>>> set = optionsMap.entrySet();
        Iterator<Map.Entry<String, ArrayList<String>>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = it.next();
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            //������������
            //һ��һ��ֻ��һ������
            run(key, value);
        }
    }

    private static void run(String line, ArrayList<String> options) {
        if (debug) Log.i(TAG, "run line = " + line);
        if (null == line) return;
        if ("-v".equals(line) || "-version".equals(line)) {
            System.out.print(Version.getVersion() + " \n");
        } else if ("-h".equals(line) || "-help".equals(line)) {
            System.out.print(Version.getHelp() + " \n");
        } else if ("-a".equals(line) || "-ad".equals(line)) {// add sd sdk
            runAd(options);
        } else if ("-s".equals(line) || "-shell".equals(line)) {// shell for apk
            if (null != options && options.size() > 0)
                Shell.run(options.get(0));
        } else if ("-d".equals(line) || "-c".equals(line)) {// ���������
            if (null != options && options.size() > 0)
                MulCSignApk.run(options.get(0));
        } else if ("-f".equals(line) || "-t".equals(line)) {
            TyCaiPiao.main(null);
        } else if ("-g".equals(line) || "-b".equals(line)) {
            TyHelper.main(null);
        } else if ("-dd".equals(line) || "-cc".equals(line)) {// ��������� ����jpay
            if (null != options && options.size() > 0)
                MulCSignApk._run(options.get(0));
        } else if ("-ddd".equals(line) || "-ccc".equals(line)) {// �̵�app���
            if (null != options && options.size() > 0)
                MulCSignApk.__run(options.get(0));
        } else if ("-v".equals(line) || "-vpn".equals(line)) {// vpn
                WinLinstener.main2(options);
        } else if ("-u".equals(line) || "-um".equals(line)) {// vpn
            UMHelper.main(options.get(0));
        }
    }

    private static void runAd(ArrayList<String> options) {
        String[] args = new String[options.size()];
        options.toArray(args);
        AdTool.main(args);
    }

    private static void initOptions(String[] args) {
        if (debug) Log.i(TAG, "initOptions" );
        int count = args.length;
        for (int i = 0; i < count; i++) {
            // ��������ѷ���"-"��ͷ�� Ŀǰ��-s -a -v -h ��������
            // -s �ӿ�����
            // -a �ӹ��sdk����
            // -v �鿴�汾������
            // -h ��������
            if (args[i].startsWith("-")) {
                //�鿴�����Ĳ���
                //���ҰѲ������ �������Ӧ������
                if (i + 1 < count)
                    for (int j = i + 1; j < count; j++) {
                        if (!args[j].startsWith("-")) {//��������"-"��ͷ
                            addOptions(args[i], args[j]);
                        } else {//�����"-"��ͷ��������һ����� ѭ������
                            addOptions(args[i], "");
                            break;
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
