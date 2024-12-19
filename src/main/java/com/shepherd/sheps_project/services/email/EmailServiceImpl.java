package com.shepherd.sheps_project.services.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{
//    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String htmlContent) {

    }
}
