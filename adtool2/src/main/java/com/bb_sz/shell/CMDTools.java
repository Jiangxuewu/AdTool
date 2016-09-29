package com.bb_sz.shell;

import static com.bb_sz.tool.TManager.debug;

/**
 * Created by Administrator on 2016/9/23.
 */

public class CMDTools {

    public static String exec(String cmd) {
        if (debug) System.out.print("exec:" + cmd + "\n");
        cmd = "cmd.exe /c " + cmd;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec(cmd);
            String res = IOTools.inputStreamTOString(p.getInputStream());
            if (debug) System.out.print(res);
            return res;
        } catch (Exception e) {
            System.err.println("Error!" + "\n");
        } finally {

        }
        return null;
    }
}
