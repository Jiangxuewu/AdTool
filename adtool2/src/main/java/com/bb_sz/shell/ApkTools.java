package com.bb_sz.shell;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.bb_sz.tool.TManager.debug;

/**
 * Created by Administrator on 2016/9/23.
 */

public class ApkTools {


    private static final String M_SHELL_APP_NAME = "com.bb_sz.ndk.App";
    private static String mReallyApplicationName;

    public static int init(String apk) {
        return 0;
    }

    public static void unapk(String apkPath, String outPath) {
        String res = CMDTools.exec("java -jar " + IOTools.getPath() + File.separator
                + "bin\\apktool.jar d " + apkPath + " -o " + outPath);
        if (debug) System.out.print("unapk, res:" + res + "\n");
    }

    public static void unapkRS(String apkPath, String outPath) {
        String res = CMDTools.exec("java -jar " + IOTools.getPath() + File.separator
                + "bin\\apktool.jar d " + apkPath + " -r -s -o " + outPath);
        if (debug) System.out.print("unapkRS, res:" + res + "\n");
    }

    public static String getUnapkOutFile(String apkPath) {
        if (null != apkPath && apkPath.endsWith(".apk"))
            return apkPath.replace(".apk", "");
        return null;
    }

    public static void apk(String file, String apk) {
        if (debug) System.err.print("start create apk\n");
        String res = CMDTools.exec("java -jar " + IOTools.getPath() + File.separator + "bin\\apktool.jar b " + file + " -o " + apk);
        if (debug) System.out.print("apk, res:" + res + "\n");
    }

    public static void apkRS(String file, String apk) {
        if (debug) System.err.print("start create apk\n");
        String res = CMDTools.exec("java -jar " + IOTools.getPath() + File.separator + "bin\\apktool.jar b " + file + " -r -s -o " + apk);
        if (debug) System.out.print("apkRS, res:" + res + "\n");
    }

    public static void signApk(String apk, String signedApk, String keystore, String alise) {
        if (debug) System.err.print("start sign apk\n");
        int i = 60;
        String runPath = IOTools.getPath();
        String cmd = "java -jar " +
                "\"" + runPath + File.separator + "bin\\signapk.jar\" " +
                "\"" + runPath + File.separator + "bin\\testkey.x509.pem\" " +
                "\"" + runPath + File.separator + "bin\\testkey.pk8\" " +
                "" + apk + " " +
                "" + signedApk + "";
        String res = CMDTools.exec(cmd);
        if (debug) System.out.print("signApk, res:" + res + "\n");
    }

    public static void updateManifestForShell(String outPath) {
        String manifestFilePath = outPath + File.separator + "AndroidManifest.xml";
        File manifestFile = new File(manifestFilePath);
        if (!manifestFile.exists()) return;
        readAndUpdate(manifestFile);

    }

    private static void readAndUpdate(File manifestFile) {
        if (null == manifestFile) return;
        final String oldFilePath = manifestFile.getAbsolutePath();
        final String tmpFilePath = manifestFile.getAbsolutePath().replace("AndroidManifest.xml", "TAndroidManifest.xml");
        File oldManifest = new File(oldFilePath);
        if (!oldManifest.exists()) {
            return;
        }
        boolean renameRes = false;
        String inFile = oldFilePath;
        String outFile = tmpFilePath;
        if (oldManifest.renameTo(new File(tmpFilePath))) {
            inFile = tmpFilePath;
            outFile = oldFilePath;
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
                    mReallyApplicationName = getReallyApplicationName(line);
                    if (!isEmpty(mReallyApplicationName)) {
                        line = line.replace(mReallyApplicationName, M_SHELL_APP_NAME);
                        //add meta data
                        line = line + "\n" + getShellMetaData() + "\n";
                    } else {
                        line = line.substring(0, line.length() - 1) + " android:name=\"" + M_SHELL_APP_NAME + "\" >";
                    }
                } else if (!isAddPermission && line.contains("<manifest")) {
                    isAddPermission = true;
                    //add permission
                    line = line + "\n" + getShellPermission() + "\n";
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
                boolean res = FileTools.copy(new File(tmpFilePath), new File(oldFilePath));
                if (res) {
                    new File(tmpFilePath).deleteOnExit();
                }
            }
        }
    }

    private static String getShellPermission() {
        StringBuffer sb = new StringBuffer();
        sb.append("<uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\"/>").append("\n");
        sb.append("<uses-permission android:name=\"android.permission.READ_EXTERNAL_STORAGE\"/>").append("\n");
        return sb.toString();
    }

    private static String getShellMetaData() {
        return "<meta-data android:name=\"SZ_SHELL_APP\" android:value=\"" + mReallyApplicationName + "\" />";
    }

    private static String getReallyApplicationName(String line) {
        String key = "android:name=\"";
        String value = getNameValue(line, key);
        if (debug) System.out.print("inject name = " + value + "\n");
        return value;
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

    public static void addShellLib(String outPath) {
        if (null == outPath) return;
        File libs = new File(outPath + File.separator + "lib");
        if (libs.exists()) {
            if (!libs.isDirectory()) {
                libs.delete();
                libs.mkdir();
            }
            File[] soLibs = libs.listFiles();
            for (File item : soLibs) {
                if (item.isDirectory() && ("arm64-v8a".equals(item.getName())
                        || "armeabi".equals(item.getName())
                        || "armeabi-v7a".equals(item.getName())
                        || "mips".equals(item.getName())
                        || "mips64".equals(item.getName())
                        || "x86".equals(item.getName())
                        || "x86_64".equals(item.getName())
                )) {
                    FileTools.copyDir(getSrcLibFilePath(item.getName()), item);
                }
            }
        } else {
            libs.mkdir();
            FileTools.copyDir(new File(IOTools.getPath() + "\\ShellSrc\\lib"), libs);
        }
    }

    private static File getSrcLibFilePath(String fileName) {
        return new File(IOTools.getPath() + "\\ShellSrc\\lib\\" + fileName);
    }

    public static String getReApkFileName(String curApkPath) {
        if (null == curApkPath || !curApkPath.endsWith(".apk")) return null;

        return curApkPath.replace(".apk", "_shell.apk");
    }

    public static void replaceDex(String dexPath, String apkPath) {
        // 解压
        String outZipDir = apkPath.replace(".apk", "");
        unapkRS(apkPath, outZipDir);
        File outFile = new File(outZipDir);
        if (!outFile.exists() || !outFile.isDirectory()) return;
        String dex = outZipDir + File.separator + "classes.dex";
        FileTools.copyFile(dexPath, dex);

        String lastFile = apkPath.replace(".apk", "ed.apk");
        apkRS(outZipDir, lastFile);
    }

    public static void clean(String srcapk) {
        if (null == srcapk) return;
        File srcApkFile = new File(srcapk);
        File clearDir = srcApkFile.getParentFile();
        if (!clearDir.exists() || clearDir.isFile()) return;
        File[] files = clearDir.listFiles();
        if (null == files || files.length == 0) return;
        if (debug) System.err.print("clean............start\n");
        for (File item : files) {
            if (debug) System.err.print("clean............" + item.getAbsolutePath() + "\n");
            if (item.isDirectory()) {
                boolean delDirRes = FileTools.deleteDir(item);
                if (debug) System.err.print("clean............delDirRes:" + delDirRes + "\n");
                int i = 0;
                while (!(delDirRes = FileTools.deleteDir(item))) {
                    if (debug) System.err.print("clean............delDirRes:" + delDirRes + "\n");
                    if (i++ > 10) {
                        break;
                    }
                }
                if (debug) System.err.print("clean............delDirRes:" + delDirRes + "\n");
            } else if (!item.getAbsolutePath().equals(srcapk) && !item.getAbsolutePath().endsWith("ed.apk")) {
                boolean delFileRes = FileTools.deleteFile(item.getAbsolutePath());
                if (debug) System.err.print("clean............delFileRes:" + delFileRes + "\n");
                int i = 0;
                while (!(delFileRes = FileTools.deleteDir(item))) {
                    if (debug) System.err.print("clean............delFileRes:" + delFileRes + "\n");
                    if (i++ > 10) {
                        break;
                    }
                }
                if (debug) System.err.print("clean............delFileRes:" + delFileRes + "\n");
            }
        }
        if (debug) System.err.print("clean............end\n");
    }

    public static void splitDexFromApk(String apk) {
        if (null == apk) return;
        try {
            String tmpDstFileName = getTmpDexFileName(apk);
            splitDexFileFromApk(apk, tmpDstFileName);
            ZipUtils.zip(tmpDstFileName, getClass_0FileName(apk));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDexFileName(String src) {
        return getTmpDexFileName(src);
    }

    public static String getTmpDexFileName(String src) {
        return getTmpDexFileName(new File(src));
    }

    public static String getTmpDexFileName(File src) {
        if (null == src || src.isDirectory()) return null;
        String path = src.getAbsolutePath();
        String name = src.getName();

        return path.replace(name, "classes.dex");
    }

    /**
     * 从apk包里面获取dex文件内容（byte）
     *
     * @return
     * @throws IOException
     */
    private static void splitDexFileFromApk(String src, String dexFileName) throws IOException {
        FileOutputStream localFileOutputStream = new FileOutputStream(new File(dexFileName));
        ZipInputStream localZipInputStream = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(src)));
        while (true) {
            ZipEntry localZipEntry = localZipInputStream.getNextEntry();
            if (localZipEntry == null) {
                localZipInputStream.close();
                break;
            }
            if (localZipEntry.getName().equals("classes.dex")) {
                byte[] arrayOfByte = new byte[1024];
                while (true) {
                    int i = localZipInputStream.read(arrayOfByte);
                    if (i == -1)
                        break;
                    localFileOutputStream.write(arrayOfByte, 0, i);
                }
            }
            localZipInputStream.closeEntry();
        }
        localZipInputStream.close();
        localFileOutputStream.close();
        return;
    }

    public static String getClass_0FileName(String src) {
        return getClass_0FileName(new File(src));
    }

    public static String getClass_0FileName(File src) {
        if (null == src || src.isDirectory()) return null;
        String path = src.getAbsolutePath();
        String name = src.getName();

        return path.replace(name, "classes_0.apk");
    }


}
