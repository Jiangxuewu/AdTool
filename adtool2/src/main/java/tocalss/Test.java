package tocalss;

import com.bb_sz.shell.CMDTools;

import java.io.File;

/**
 * Created by Administrator on 2016/9/25.
 */

public class Test {

    public static void main(String[] args) {
        String file = "D:\\Android\\github\\AdTool\\adtool2\\src\\main\\java\\com\\bb_sz";
        File srcFile = new File(file);

        toClass(srcFile);
        moveClass(srcFile);
    }

    private static void moveClass(File srcFile) {

    }

    private static void toClass(File srcFile) {
        if (null == srcFile) return;
        if (srcFile.isFile() && srcFile.getAbsolutePath().endsWith(".java")){
            CMDTools.exec("javac " + srcFile.getAbsolutePath());
        } else if(srcFile.isDirectory()){
            File[] files = srcFile.listFiles();
            if (null != files){
                for (File item : files){
                    toClass(item);
                }
            }
        }
    }
}
