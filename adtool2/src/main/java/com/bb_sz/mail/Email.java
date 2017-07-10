package com.bb_sz.mail;

import com.bb_sz.umhelper.UMHelper;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Administrator on 2017/7/6.
 */

public class Email {

    //    public static String myEmailAccount = "jiangxuewu@iadmore.com";
//    public static String myEmailPassword = "Jxw0927";
    public static String myEmailAccount = "um@bb-sz.com";
    public static String myEmailPassword = "Jiang0927";
//    public static String myEmailAccount = "304261930@qq.com";
//    public static String myEmailPassword = "cgdoiknobeyfbjfb";
//    public static String myEmailPassword = "cwbdwrilbtelbjie";

    public static String myEmailSMTPHost = "smtp.bb-sz.com";

    public static String receiveMailAccount = UMHelper.debug ? "jiangxuewu@iadmore.com" : "qiyin.luo@jfst.com.cn";
    public static String receiveMailAccount2 = "jiangxuewu@iadmore.com";
    public static String receiveMailAccount3 = UMHelper.debug ? "jiangxuewu@iadmore.com" : "kf@jfst.com.cn";

    public static void sendEmail(String title, String content) throws Exception {

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", myEmailSMTPHost);
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount, title, content);

        Transport transport = session.getTransport();

        transport.connect(myEmailAccount, myEmailPassword);

        transport.sendMessage(message, message.getAllRecipients());

        transport.close();
    }

    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String title, String content) throws Exception {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(sendMail, "UM", "UTF-8"));

        // 3. To:
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "LuoQiYing", "UTF-8"));

        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(receiveMailAccount3, "KF", "UTF-8"));

        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(receiveMailAccount2, "JiangXueWu", "UTF-8"));

        // 4. Subject:
        message.setSubject(title, "UTF-8");

        // 5. Content:
        message.setContent(content, "text/html;charset=UTF-8");

        // 6.
        message.setSentDate(new Date());

        // 7.
        message.saveChanges();

        return message;
    }
}
