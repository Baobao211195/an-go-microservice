package com.an.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

@Component
public class MailUtils {

    @Value("${smtp.server:smtp.gmail.com}")
    private String smtpServer;

    @Value("${smtp.port:587}")
    private String smtpPort;

    @Value("${warning.mail.account:ancustomercareservice@gmail.com}")
    private String warnMail;

    @Value("${warning.mail.password:ango@2019}")
    private String warnPass;


    public void sendmail(String subject, String content, String attachmentDir, String toMail) throws Exception {
        try {
            if (StringUtils.isEmpty(toMail) || StringUtils.isEmpty(content)){
                return;
            }
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpServer);
            props.put("mail.smtp.port", smtpPort);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(warnMail, warnPass);
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(warnMail, false));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
            msg.setSubject(subject);
            msg.setContent(content, "text/html");
            msg.setSentDate(new Date());

            if (!StringUtils.isEmpty(attachmentDir)) { // set attachment if had
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(content, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                MimeBodyPart attachPart = new MimeBodyPart();

                attachPart.attachFile(attachmentDir);
                multipart.addBodyPart(attachPart);
                msg.setContent(multipart);
            }
            Transport.send(msg);
        } catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public String sendOtp (String email){
        String otp = RandomUtils.generateOtp(Const.OTP.LENGTH);
        if (!StringUtils.isEmpty(otp)){
            try {
                sendmail("v/v Ango OTP", "Ma OTP cua ban la " + otp, null, email);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return otp;
        }
        return null;
    }

}
