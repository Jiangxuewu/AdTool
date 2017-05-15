package com.bb_sz.mulchannelapk;

import com.bb_sz.shell.CMDTools;
import com.bb_sz.shell.FileTools;
import com.bb_sz.shell.Shell;
import com.bb_sz.tool.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;

import javax.swing.plaf.TextUI;

import static com.bb_sz.tool.TManager.debug;

/**
 * Created by Administrator on 2016/12/5.
 * <p>Mul channel create apk</p>
 */

public class MulCSignApk {
    private static final String SPACE = " ";
    private static String FilePath = "D:\\DengZong\\TuiGuang\\Templates\\HJZJ";
    private static final String TAG = MulCSignApk.class.getSimpleName();
    private static String apkPrefix = "";
    private static String templates = null;
    private static String apkToolPath = null;
    private static String cidKey = null;
    private static String YMCidKey = null;
    private static String QBCidKey = null;
    private static String letuCodePath = null;
    private static HashMap<String, String> settings;
    private static Set<Map.Entry<String, String>> entrySet;

    private static String jpaySmailPath = "D:\\DengZong\\JPaySDK\\out";
    private static String jpayJarPath = null;
    private static String bbszPath = null;

    private static boolean notJpay = false;
    private static boolean storeApp = false;
    private static HashMap<String,String> ymCIDMap = new HashMap<>();
    private static HashMap<String,String> QBCIDMap = new HashMap<>();

    public static void __run(String path) {
        storeApp = true;
        _run(path);
    }
    public static void _run(String path) {
        notJpay = true;
        main(new String[]{path});
    }

    public static void run(String path) {
        main(new String[]{path});
    }

    public static void main(String[] args) {
        if (null != args && args.length > 0) {
            FilePath = args[0];
            if (args.length > 1) {
                jpayJarPath = args[1];
            }
        }
        if (null == FilePath || FilePath.length() < 1) return;
        settings = getSettings(FilePath);
        if (null == settings || settings.size() < 1) return;
        if (null == templates || templates.length() < 1) return;
        if (null == apkToolPath || apkToolPath.length() < 1) return;
//
        List<String> channels = getChannels(FilePath);
        if (null == channels || channels.size() < 1) return;

        Log.i(TAG, "channels size is " + channels.size());
        Log.i(TAG, "jpayJarPath = " + jpayJarPath);
        Log.i(TAG, "bbszPath = " + bbszPath);
        if (!notJpay && null != jpayJarPath && jpayJarPath.length() > 0) {
            //unzip assets
            String parentPath = new File(jpayJarPath).getParent();

//            FileTools.deleteDir(new File(parentPath + File.separator + "assets"));
            FileTools.deleteDir(parentPath + File.separator + "JPay.dex");
            FileTools.deleteDir(jpaySmailPath);

            CMDTools.exec(parentPath + File.separator + "jpay2assets.bat");//need to undo

            //jar to dex
            CMDTools.exec("java -jar D:\\Android\\sdk\\build-tools\\24.0.2\\lib\\dx.jar --dex --output="
                    + parentPath + File.separator + "JPay.dex"
                    + SPACE
                    + jpayJarPath);

            //dex to smali
            CMDTools.exec("java -jar D:\\Tools\\Odex2dex\\baksmali-2.1.3.jar"
                    + SPACE
                    + parentPath + File.separator + "JPay.dex"
                    + SPACE
                    + "-o"
                    + SPACE
                    + jpaySmailPath
                    + "\\smali");


            FileTools.copyDir(new File(parentPath + File.separator + "assets"), new File(jpaySmailPath + File.separator + "assets"));
            FileTools.deleteDir(parentPath + File.separator + "JPay.dex");
        } else if (!notJpay) {
            Log.e(TAG, "jpayJarPath is null.");
            return;
        }

        if (null != bbszPath && bbszPath.length() > 0) {

        } else {
            Log.e(TAG, "bbszPath is null.");
            return;
        }


        int i = 1;
        String outFile;
        long start;
        String apkOutFile;
        for (String cid : channels) {//copy
            start = System.currentTimeMillis();
            outFile = FilePath + File.separator + "channel_srcs" + File.separator + apkPrefix + cid;
            copyAndReplasy(cid, outFile);
            Log.i(TAG, "copy over " + i++ + ",:" + (System.currentTimeMillis() - start));
        }

        i = 1;
        for (String cid : channels) {//create apk
            start = System.currentTimeMillis();
            outFile = FilePath + File.separator + "channel_srcs" + File.separator + apkPrefix + cid;
            apkOutFile = FilePath + File.separator + "channel_apks" + File.separator + "shell_" + cid + File.separator + apkPrefix + cid + ".apk";
            FileTools.deleteDir(FilePath + File.separator + "channel_apks" + File.separator + "shell_" + cid);
            FileTools.createFile(FilePath + File.separator + "channel_apks" + File.separator + "shell_" + cid + File.separator + "1.txt");//为了创建目录
            signApk(outFile, apkOutFile);
            FileTools.deleteFile(FilePath + File.separator + "channel_apks" + File.separator + "shell_" + cid + File.separator + "1.txt");
            Log.i(TAG, "sign over " + i++ + ",:" + (System.currentTimeMillis() - start));
        }

        //add our ad sdk
//        if (1 == 11){//_C4349
//            return;
//        }

        if (storeApp){

            return;
        }

        i = 1;
        String shellApkOutFile;
        for (String cid : channels) {//add shell
            start = System.currentTimeMillis();
            apkOutFile = FilePath + File.separator + "channel_apks" + File.separator + "shell_" + cid + File.separator + apkPrefix + cid + ".apk";
            shellApkOutFile = FilePath + File.separator + "channel_shelled" + File.separator + apkPrefix + cid + "_shelled.apk";
            FileTools.deleteFile(shellApkOutFile);
            FileTools.createFile(FilePath + File.separator + "channel_shelled" + File.separator + "1.txt");//为了创建目录
            Shell.run(apkOutFile, shellApkOutFile);
            FileTools.deleteFile(FilePath + File.separator + "channel_shelled" + File.separator + "1.txt");
            Log.i(TAG, "shell over " + i++ + ",:" + (System.currentTimeMillis() - start));
        }

//        i = 1;
//        String signCMD;
//        for (String cid : channels) {//signed
//            start = System.currentTimeMillis();
//            shellApkOutFile = FilePath + File.separator + "channel_shelled" + File.separator + apkPrefix + cid + "_shelled.apk";
//            //jarsigner -verbose -keystore mydemo.keystore -signedjar
//            signCMD = "jarsigner -verbose -keystore " + keyStoreFile + " -signedjar " + signedApk + "" + shellApkOutFile + align
//
//            CMDTools.exec(signCMD);
//
//            Log.i(TAG, "signed over " + i++ + ",:" + (System.currentTimeMillis() - start));
//        }

    }


    private static void copyAndReplasy(String cid, String outFile) {
        File tmp = new File(outFile);
        FileTools.deleteDir(tmp);
//        FileTools.createFile(outFile + File.separator + "1.txt");
        //1, copy app
        copyDir(new File(templates), tmp, cid);
        //2, copy jpay
        if (!notJpay) {
            copyDir(new File(jpaySmailPath), tmp, cid);
        }
        //3, copy bb_sz
        copyDir(new File(bbszPath), tmp, cid, true);
        //4, add permission
        //5, add androidManifest.xml
        updateManifest(outFile, cid);

        if (null != letuCodePath && letuCodePath.length() > 0) {
            String appcode = outFile + File.separator + "assets" + File.separator + "llappcode.dat";
            if (debug) Log.i("SKY", "letuCodePath = " + letuCodePath);
            if (debug) Log.i("SKY", "appcode = " + appcode);
            FileTools.deleteFile(appcode);
            FileTools.copyFile(letuCodePath, appcode);
        }

//        FileTools.deleteFile(outFile + File.separator + "1.txt");
    }

    public static void updateManifest(String outFile, String cid) {
        File file = new File(outFile + File.separator + "AndroidManifest.xml");
        File to = new File(outFile + File.separator + "AndroidManifest-Copy.xml");
        if (!file.exists() || file.isDirectory()) return;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            OutputStreamWriter outWriter = new OutputStreamWriter(new FileOutputStream(to));
            List<String> permission = new ArrayList<String>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("<uses-permission android:name=\"")) {// is permission
                    if (!permission.contains(line.trim())) {
                        permission.add(line.trim());
                        outWriter.write(line + "\n");
                    } else {
                        continue;
                    }
                } else if (line.contains("</application>") && !line.contains("-->")) {// next add cus permission
                    outWriter.write(getManifest(cid) + "\n");
                    outWriter.write(update(line, cid) + "\n");
                    outWriter.write(getCusPermissions(permission) + "\n");
                    continue;
                } else {
                    outWriter.write(line + "\n");
                }
            }
            outWriter.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileTools.deleteFile(file.getAbsolutePath());
        FileTools.copyFile(to, file);
        FileTools.deleteFile(to.getAbsolutePath());
    }

    private static String getManifest(String cid) {
        StringBuffer sb = new StringBuffer();
        File file = new File(bbszPath + File.separator + "AndroidManifest.xml");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            boolean isStart = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<application")) {// is start
                    isStart = true;
                } else if (line.contains("</application>")) {// is end.
                    isStart = false;
                }
                if (isStart && !line.contains("<application")) {
                    sb.append(update(line, cid)).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private static String getCusPermissions(List<String> permission) {
        StringBuffer sb = new StringBuffer();
        File file = new File(bbszPath + File.separator + "AndroidManifest.xml");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<uses-permission android:name=\"")) {// is permission
                    if (null == permission) {
                        permission = new ArrayList<String>();
                    }
                    if (!permission.contains(line.trim())) {
                        permission.add(line.trim());
                        sb.append(line).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private static List<String> getChannels(String filePath) {
        File settings = new File(filePath + File.separator + "channels.txt");
        if (!settings.exists()) {
            return null;
        }

        List<String> map = new ArrayList<String>();
        try {
            FileReader fr = new FileReader(settings);
            BufferedReader bf = new BufferedReader(fr);
            String str = null;
            String cidTmp;
            String ymcidTmp;
            String yycidTmp;
            while ((str = bf.readLine()) != null) {
                if (!str.startsWith("#")) {
                    cidTmp = (str.contains("=") ? str.split("=")[0] : str);
                    ymcidTmp = (str.contains("=") ? str.split("=")[1] : str);
                    yycidTmp = (str.contains("=") && str.split("=").length == 3 ? str.split("=")[2] : str);
                    map.add(cidTmp);
                    ymCIDMap.put(cidTmp, ymcidTmp);
                    QBCIDMap.put(cidTmp, yycidTmp);
                    if (debug) Log.i(TAG, "cid=" + cidTmp + ", ymcidTmp = " + ymcidTmp + ", yycidTmp = " + yycidTmp);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;

    }

    private static HashMap<String, String> getSettings(String filePath) {
        File settings = new File(filePath + File.separator + "settings.txt");
        if (!settings.exists()) {
            return null;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            FileReader fr = new FileReader(settings);
            BufferedReader bf = new BufferedReader(fr);
            String str = null;
            String key = null;
            String value = null;
            while ((str = bf.readLine()) != null) {
                if (str.startsWith("#")) {//filter notes
                    continue;
                }
                if (null == apkToolPath && str.startsWith("apkToolPath=")) {// get apt tool path
                    apkToolPath = str.replace("apkToolPath=", "");
                    if (debug) Log.i(TAG, "apkToolPath = " + apkToolPath);
                } else if (null == jpayJarPath && str.startsWith("jpayPath=")) {// get jpay path
                    jpayJarPath = str.replace("jpayPath=", "");
                    if (debug) Log.i(TAG, "jpayJarPath = " + jpayJarPath);
                } else if (null == bbszPath && str.startsWith("bbszPath=")) {// get bbsz path
                    bbszPath = str.replace("bbszPath=", "");
                } else if (null == templates && str.startsWith("templates=")) {// get templates name
                    String tvalue = str.replace("templates=", "");
                    apkPrefix = (tvalue.contains("_") ? tvalue.split("_")[0] : "") + "_";
                    templates = FilePath + File.separator + tvalue;
                    if (debug) Log.i(TAG, "templates = " + templates);
                } else if (null == cidKey && str.startsWith("CID=")) {//get cid name  letuCodePath
                    cidKey = str.replace("CID=", "");
                    if (debug) Log.i(TAG, "cidKey = " + cidKey);
                }  else if (null == YMCidKey && str.startsWith("YMCID=")) {//get YMCidKey name  letuCodePath
                    YMCidKey = str.replace("YMCID=", "");
                    if (debug) Log.i(TAG, "YMCidKey = " + YMCidKey);
                }  else if (null == QBCidKey && str.startsWith("QBCID=")) {//get QBCidKey name
                    QBCidKey = str.replace("QBCID=", "");
                    if (debug) Log.i(TAG, "YYCidKey = " + QBCidKey);
                } else if (null == letuCodePath && str.startsWith("letuCodePath=")) {//letuCodePath
                    letuCodePath = str.replace("letuCodePath=", "");
                    if (debug) Log.i(TAG, "letuCodePath = " + letuCodePath);
                } else if (str.contains("=")) {//get need replace key and it's values
                    key = str.split("=")[0];
                    value = str.split("=")[1];
                    map.put(key, value);
                    if (debug) Log.i(TAG, key + "=" + value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static void signApk(String src, String apk) {
        String cmd = "java.exe -jar \"" + apkToolPath + "\" b -f \"" + src + "\" -o \"" + apk + "\"";
        CMDTools.exec(cmd);
    }

    private static void copyDir(File from, File to, String cid) {
        copyDir(from, to, cid, false);
    }

    private static void copyDir(File from, File to, String cid, boolean isCheckLibExist) {
        if (null == from || null == to) {
            return;
        }
        File[] file = from.listFiles();

        if (null == file) {
            return;
        }
        for (File f : file) {
            if (f.isFile()) {
                if (f.getAbsolutePath().equals(bbszPath + File.separator + "AndroidManifest.xml")) {//bbsz's AndroidManifest.xml not copy. will do it in next step.
                    continue;
                }
                File newFile = FileTools.createFile(to.getAbsoluteFile() + File.separator + f.getName());
                if (isBinaryFile(newFile)) {
                    FileTools.copy(f, newFile);
                } else {
                    boolean hasUpdate = copy(f, newFile, cid);
                    if (!checkCopyOk(f, newFile) || !hasUpdate) {
                        FileTools.copy(f, newFile);
                    }
                }
            } else {
                if (!"original".equals(f.getName())) {
                    if (isCheckLibExist && "lib".equals(f.getParentFile().getName())) {
                        File t = new File(to.getAbsolutePath() + File.separator + f.getName());
                        if (t.exists()) {
                            copyDir(f, t, cid, isCheckLibExist);
                        }
                    } else {
                        File t = new File(to.getAbsolutePath() + File.separator + f.getName());
                        copyDir(f, t, cid, isCheckLibExist);
                    }
                }
            }
        }
    }

    private static boolean checkCopyOk(File f, File newFile) {
        boolean result = newFile.length() - f.length() > 100;
        if (result) {
            Log.i(TAG, "checkCopyOk file name:" + newFile.getName());
        }

        return !result;
    }

    private static boolean isBinaryFile(File newFile) {


        String fileName = newFile.getName();
        if (fileName.endsWith(".xml")
//                || fileName.endsWith(".txt")
                || fileName.endsWith(".yml")
                || fileName.endsWith(".smali")) {
//            Log.i(TAG, "fileName = " + fileName);
//            String fileType = FileType.getFileType(newFile.getAbsolutePath());

            return false;
        } else if (fileName.equals("jpay_uid.txt")) {
            return false;
        }


//        String fileType = FileType.getFileType(newFile.getAbsolutePath());
//        if (null == fileType) {
//            Log.i(TAG, "null, type is " + newFile.getAbsolutePath());
//
//            return true;
//        }
//        if (fileType.equals(".txt")
//                || fileType.equals(".xml")
//                || fileType.equals(".yml")
//                || fileType.equals(".smali")) {
//            Log.i(TAG, "fileType = " + fileType);
//            return false;
//        }
        return true;
    }

    private static boolean copy(File oldFile, File newFile, String cid) {
        try {
            if (oldFile.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(oldFile), "UTF-8"));
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");
                String line = "";
                String tmp = null;
                String updated = null;
                boolean hasUpdate = false;
                while ((line = br.readLine()) != null) {
                    if (null != tmp) {//&& tmp.length() > 0  tmp may be is bank line. so not need tem.length() > 0
                        updated = update(tmp, cid);
                        if (!hasUpdate) {
                            hasUpdate = !tmp.equals(updated);
                        }
                        osw.write(updated + "\n");
                    }
                    tmp = line;
                }
                //last line not need \n
                if (null != tmp) {
                    updated = update(tmp, cid);
                    if (!hasUpdate) {
                        hasUpdate = !tmp.equals(updated);
                    }
                    osw.write(updated);
                }
                osw.close();
                br.close();
                return hasUpdate;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String update(String line, String cid) {
        if (line.contains(cidKey)) {
            line = line.replace(cidKey, cid);
        }

        if (null != YMCidKey){
            line = line.replace(YMCidKey, ymCIDMap.get(cid));
        }

        if (null != QBCidKey){
            line = line.replace(QBCidKey, QBCIDMap.get(cid));
        }

        if (null == entrySet){
            if (notJpay){
                settings.put("Lcom/jpay/sdk/IChargeResult;", "Lcom/bb_sz/pay/IChargeResult;");
            }
            entrySet = settings.entrySet();
        }

        for (Map.Entry<String, String> entry : entrySet) {
            if (line.contains(entry.getKey())) {
                line = line.replace(entry.getKey(), entry.getValue());
            }
        }
        return line;
    }

}
