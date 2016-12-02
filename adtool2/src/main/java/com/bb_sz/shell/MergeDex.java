package com.bb_sz.shell;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.zip.Adler32;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Administrator on 2016/9/23.
 */

public class MergeDex {

    public static String shellDexFileStr;
    public static String class_0_apk_str;
    public static String result_class_str;
    public static byte[] key = {0, 0, 0, 0};

    public static void merge(String srcApk) {
        class_0_apk_str = ApkTools.getClass_0FileName(srcApk);
        result_class_str = ApkTools.getDexFileName(srcApk);
        shellDexFileStr = IOTools.getPath() + "\\ShellSrc\\classes_0_shell.dex";
        main();
    }

    private static void main() {
        try {
            int len = new Random().nextInt(Integer.MAX_VALUE);
            key = intToByte(len);
            System.out.print("key:" + key[0] + ":" + key[1] + ":" + key[2] + ":" + key[3] + "\n");
            File payloadSrcFile = new File(class_0_apk_str);
            File unShellDexFile = new File(shellDexFileStr);
            byte[] payloadArray = encrpt(readFileBytes(payloadSrcFile));
            byte[] unShellDexArray = readFileBytes(unShellDexFile);
            int payloadLen = payloadArray.length;
            int unShellDexLen = unShellDexArray.length;
            int totalLen = payloadLen + unShellDexLen + 4 + key.length;
            byte[] newdex = new byte[totalLen];

            System.arraycopy(unShellDexArray, 0, newdex, 0, unShellDexLen);

            System.arraycopy(payloadArray, 0, newdex, unShellDexLen,
                    payloadLen);
            byte[] dexlen = intToByte(payloadLen);
            System.out.print("dexlen:" + dexlen[0] + ":" + dexlen[1] + ":" + dexlen[2] + ":" + dexlen[3] + "\n");
            System.arraycopy(intToByte(payloadLen), 0, newdex, totalLen - 4 - key.length, 4);

            System.arraycopy(key, 0, newdex, totalLen - key.length, key.length);

            fixFileSizeHeader(newdex);

            fixSHA1Header(newdex);

            fixCheckSumHeader(newdex);


            //������д��g:/classes.dex
            String str = result_class_str;
            File file = new File(str);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
            }

            FileOutputStream localFileOutputStream = new FileOutputStream(str);
            localFileOutputStream.write(newdex);
            localFileOutputStream.flush();
            localFileOutputStream.close();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * �޸�dexͷ��CheckSum У����
     *
     * @param dexBytes
     */
    private static void fixCheckSumHeader(byte[] dexBytes) {
        Adler32 adler = new Adler32();
        adler.update(dexBytes, 12, dexBytes.length - 12);//��12���ļ�ĩβ����У����
        long value = adler.getValue();
        int va = (int) value;
        byte[] newcs = intToByte(va);
        //��λ��ǰ����λ��ǰ������
        byte[] recs = new byte[4];
        for (int i = 0; i < 4; i++) {
            recs[i] = newcs[newcs.length - 1 - i];
        }
        System.arraycopy(recs, 0, dexBytes, 8, 4);//Ч���븳ֵ��8-11��
    }

    /**
     * �޸�dexͷ sha1ֵ
     *
     * @param dexBytes
     * @throws NoSuchAlgorithmException
     */
    private static void fixSHA1Header(byte[] dexBytes)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(dexBytes, 32, dexBytes.length - 32);
        byte[] newdt = md.digest();
        System.arraycopy(newdt, 0, dexBytes, 12, 20);

        String hexstr = "";
        for (int i = 0; i < newdt.length; i++) {
            hexstr += Integer.toString((newdt[i] & 0xff) + 0x100, 16)
                    .substring(1);
        }
    }

    /**
     * �޸�dexͷ file_sizeֵ
     *
     * @param dexBytes
     */
    private static void fixFileSizeHeader(byte[] dexBytes) {
        //���ļ�����
        byte[] newfs = intToByte(dexBytes.length);
        byte[] refs = new byte[4];
        //��λ��ǰ����λ��ǰ������
        for (int i = 0; i < 4; i++) {
            refs[i] = newfs[newfs.length - 1 - i];
        }
        System.arraycopy(refs, 0, dexBytes, 32, 4);//�޸ģ�32-35��
    }

    /**
     * int תbyte[]
     *
     * @param number
     * @return
     */
    private static byte[] intToByte(int number) {
        byte[] b = new byte[4];
        for (int i = 3; i >= 0; i--) {
            b[i] = (byte) (number % 256);
            number >>= 8;
        }
        return b;
    }

    /**
     * ����
     */
    private static byte[] encrpt(byte[] bytes) {
        int len = bytes.length;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            if (i > 7) {
                result[i] = bytes[len + 7 - i];
            } else {
                result[i] = bytes[i];
            }
        }
        return result;
    }

    private static short getS(int i) {
        return (short) Math.abs((Short.valueOf(String.valueOf(key[i % key.length])) % 4));
    }

    /**
     * ��ָ�����ֽ�������gzipѹ��
     *
     * @param src
     * @return ѹ�������ֽ�����
     */
    public static byte[] gzip(byte[] src) {
        if (null != src) {
            final ByteArrayOutputStream os = new ByteArrayOutputStream(
                    src.length);
            try {
                final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(
                        os);
                gzipOutputStream.write(src);
                gzipOutputStream.flush();
                gzipOutputStream.close();
                return os.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public final static byte[] MD5(byte[] btInput) {
        // char
        // hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // ���MD5ժҪ�㷨�� MessageDigest ����
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // ʹ��ָ�����ֽڸ���ժҪ
            mdInst.update(btInput);
            // �������
            byte[] md = mdInst.digest();
            return md;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * �Զ����ƶ����ļ�����
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static byte[] readFileBytes(File file) throws IOException {
        byte[] arrayOfByte = new byte[1024];
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        while (true) {
            int i = fis.read(arrayOfByte);
            if (i != -1) {
                localByteArrayOutputStream.write(arrayOfByte, 0, i);
            } else {
                return localByteArrayOutputStream.toByteArray();
            }
        }
    }
}
