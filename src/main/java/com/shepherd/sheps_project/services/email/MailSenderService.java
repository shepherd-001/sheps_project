package com.shepherd.sheps_project.services.email;

public interface MailSenderService {
    void sendEmail(String to, String subject, String htmlContent);
}
