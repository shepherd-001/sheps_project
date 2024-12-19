package com.shepherd.sheps_project.services.email;

public interface EmailService {
    void sendEmail(String to, String subject, String htmlContent);
}
