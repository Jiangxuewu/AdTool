package com.bb_sz.shell;


import com.bb_sz.tool.Log;

import java.io.File;

import static com.bb_sz.tool.TManager.debug;

/**
 * Created by Administrator on 2016/9/23.
 */

public class Shell {

    private static final String TAG = Shell.class.getSimpleName();

    public static void run(String apk) {
        if (null == apk || "".equals(apk)){
            Log.e(TAG, "param error, apk path is invalid");
            return;
        }
        main(apk);
    }

    private static void main(String curApkPath) {

        //1, ��ѹԭʼapk�ļ��� ɾ�����г�classes.dex֮��������ļ������´��������class_0.apk.
        ApkTools.splitDexFromApk(curApkPath);
        //2, ׼��class_0_shell.dex
        //3, �ϲ�class_0.apk��class_0_shell.dex -> class.dex
        MergeDex.merge(curApkPath);
        //4, ������ԭʼapk�� �滻���е�smail��androidManifest.xml�����lib
        String outPath = ApkTools.getUnapkOutFile(curApkPath);
        ApkTools.unapk(curApkPath, outPath);
        if (debug) System.err.print("0 outPath:" + outPath + "->" + System.currentTimeMillis() + "\n");
        File smail = new File(outPath + File.separator + "smali");
        while (!smail.exists()) {
            if (debug) System.err.print("not smali and waiting:" + smail.getAbsolutePath() + "->" + System.currentTimeMillis() + "\n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (debug) System.err.print("1 smali:" + smail.getAbsolutePath() + "->" + System.currentTimeMillis() + "\n");
        while (smail.exists()) {
            if (debug) System.err.print("exists smali and del:" + smail.getAbsolutePath() + "\n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FileTools.deleteDir(smail);
        }
        ApkTools.addShellLib(outPath);
        ApkTools.updateManifestForShell(outPath);
        //5��������Ĳ�����ļ���shell.apk
        String shellApkPath = ApkTools.getReApkFileName(curApkPath);
        if (debug) System.err.print("start apk, shellApkPath:" + shellApkPath + "->" + System.currentTimeMillis() + "\n");
        ApkTools.apk(outPath, shellApkPath);
        if (debug) System.err.print("start check, shellApkPath:" + shellApkPath + "->" + System.currentTimeMillis() + "\n");
        while (!new File(shellApkPath).canRead()) {
            if (debug) System.err.print("not can read, shellApkPath:" + shellApkPath + "\n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //6, ��ѹshell.apk�ļ��������е�class.dex�滻�ɵ������еĺ�����ѹ����shell_unsigned.apk���ɡ�
        String lastDexPath = ApkTools.getDexFileName(curApkPath);
        if (debug) System.err.print("lastDexPath:" + lastDexPath + "\n");
        ApkTools.replaceDex(lastDexPath, shellApkPath);
        ApkTools.clean(curApkPath);
    }

}
