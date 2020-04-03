package com.bb_sz.shell;


import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Administrator on 2016/9/23.
 */

public class FileTools {
    public static boolean deleteFile(String file) {
        if (null == file) return false;
        return new File(file).delete();
    }

    public static boolean deleteDir(String file) {
        boolean result = deleteDir(new File(file));
        return result;
    }


    public static boolean deleteDir(File dir) {
        if (null == dir || !dir.exists()) return true;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // delete empty dir
        return dir.delete();
    }

    public static boolean deleteDirExceptFile(File dir, File[] excepts) {
        if (null == dir || !dir.exists()) return true;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirExceptFile(new File(dir, children[i]), excepts);
                if (!success) {
                    return false;
                }
            }
        }

        if (null != excepts) {
            for (File item : excepts) {
                if (dir.getAbsolutePath().equals(item.getAbsolutePath())) {
                    return true;
                }
            }
        }
        // delete empty dir
        return dir.delete();
    }

    public static void copyFile(String src, String dst) {
        copyFile(new File(src), new File(dst));
    }

    public static void copyFile(File src, File dst) {
        if (null == src || null == dst) return;
        if (!src.isFile()) return;
        if (!src.exists()) return;
        try {
            if (!dst.exists()) {
                if (!dst.getParentFile().exists()) {
                    File tmp;
                    while (!dst.getParentFile().mkdir()) {
                        tmp = dst.getParentFile().getParentFile();
                        while (!tmp.mkdir()) {
                            tmp = tmp.getParentFile();
                        }
                    }
                }
                boolean res = dst.createNewFile();
                if (!res) return;
            }
            InputStream inStream = new FileInputStream(src);
            FileOutputStream fs = new FileOutputStream(dst, false);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, length);
            }
            inStream.close();
            fs.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void copyDir(File from, File to) {
//        if (debug) System.out.print("copy Dir" + "\n");
        if (null == from || null == to) {
            return;
        }
//        if (debug)
//            System.out.print("copy Dir, from:" + from.getAbsolutePath() + ", to:" + to.getAbsolutePath() + "\n");
        File[] file = from.listFiles();

        if (null == file) {
            return;
        }
        for (File f : file) {
            if (f.isFile()) {
                File newFile = createFile(to.getAbsoluteFile() + File.separator + f.getName());
//                if (debug)
//                    System.out.print("copy File, from:" + f.getAbsolutePath() + ", to:" + newFile.getAbsolutePath() + "\n");
                copy(f, newFile);
            } else {
                File t = new File(to.getAbsolutePath() + File.separator + f.getName());
                copyDir(f, t);
            }
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

    public static boolean copy(File oldFile, File newFile) {
        try {
            if (oldFile.exists()) {
                FileInputStream inStream = new FileInputStream(oldFile);
                FileOutputStream outStream = new FileOutputStream(newFile);
                byte[] data = new byte[1024];
                int count = -1;
                while ((count = inStream.read(data, 0, 1024)) != -1)
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

    public static void saveStringToFile(String line, File file, boolean append){
//        try {

//            FileOutputStream outStream = new FileOutputStream(file);
//            FileOutputStream outStream = new FileOutputStream(new FileOutputStream(file, append), 123);
//            Writer wr = new OutputStreamWriter(outStream);
//            BufferedWriter writer = new BufferedWriter(wr);
//            writer.write(line + "\n\r");

//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
    }
}
