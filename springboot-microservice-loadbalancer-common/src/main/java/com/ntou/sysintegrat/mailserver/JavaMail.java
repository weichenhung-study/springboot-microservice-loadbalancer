package com.ntou.sysintegrat.mailserver;

import lombok.extern.log4j.Log4j2;

import jakarta.mail.Authenticator;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public class JavaMail {
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            log.info("ExecutorService 已關閉");
        }));
    }

    public void sendMail(MailVO vo) {
        executor.submit(() -> {
            final String userName = "weiiweiidev@gmail.com";
            final String password = "gevaewtbwiwhedea";

            Properties prop = getProperties();
            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(userName));
                message.setRecipient(RecipientType.TO, new InternetAddress(vo.getEmailAddr()));
                message.setSubject(vo.getSubject());
                message.setContent(vo.getContent(), "text/html;charset=UTF-8");

                Transport.send(message);
                log.info("寄信成功：{}", vo.getEmailAddr());

            } catch (MessagingException e) {
                log.error("寄信失敗", e);
            } catch (Exception e) {
                log.error("非同步寄信失敗", e);
            }
        });
    }

    private static Properties getProperties() {
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.host", "smtp.gmail.com");
        prop.setProperty("mail.smtp.port", "465");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.port", "465");
        prop.setProperty("mail.debug", "true");
        return prop;
    }
}
