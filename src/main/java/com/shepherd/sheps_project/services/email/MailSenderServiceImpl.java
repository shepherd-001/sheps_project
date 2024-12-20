package com.shepherd.sheps_project.services.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import com.shepherd.sheps_project.exceptions.MailSenderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender mailSender;
    @Value("${mail_from_email}")
    private String email;
    @Value("${mail_from_name}")
    private String name;

    @Override
    @Async
    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            log.info("Initiating send email notification to {}", to);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(new InternetAddress(email, name));
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

            log.info("Email notification sent to {}", to);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            log.error("Unable to send email to '{}'.  Error:: {}", to, ex.getMessage());
            throw new MailSenderException(ex.getMessage());
        }
    }



//    @Value("${mailersend_api_key}")
//    private String apiKey;
//
//    @Override
//    public void sendEmail(String to, String subject, String htmlContent) {
//        Email email = new Email();
//        email.setFrom("Sheps App", "shepsappproduct@gmail.com");
//        email.setSubject(subject);
//        email.setHtml(htmlContent);
//        email.addRecipient("", to);
//
//        MailerSend mailerSend = new MailerSend();
//        mailerSend.setToken(apiKey);
//
//        try{
//            MailerSendResponse response =
//                    mailerSend.emails().send(email);
//            log.info("\n\nResponse Id: '{}',  Response status code: '{}',  Response to string: '{}' \n\n",
//                    response.messageId, response.responseStatusCode, response.toString());
//        } catch (MailerSendException ex) {
//            throw new MailSenderException(ex.getMessage());
//        }
//    }
}

