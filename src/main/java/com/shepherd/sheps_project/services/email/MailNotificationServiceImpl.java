package com.shepherd.sheps_project.services.email;

import com.shepherd.sheps_project.data.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailNotificationServiceImpl implements MailNotificationService{
    private final MailSenderService mailSenderService;
    @Value("${base_url}")
    private String baseUrl;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendVerificationMail(User user, String token) {
        String verificationLink = "%s/api/v1/auth/verify?token=%s&email=%s".formatted(baseUrl, token, user.getEmail());
        Context context = new Context();
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("confirmationLink", verificationLink);
        String htmlContent = templateEngine.process("email-confirmation", context);
        log.info("::::: Verification mail ready to be sent to {} :::::", user.getEmail());
        CompletableFuture.runAsync(() -> mailSenderService
                .sendEmail(user.getEmail(), "Confirm Your Email Address", htmlContent));

    }

    @Override
    public void sendResetPasswordMail(User user, String token) {
        String verificationLink = "%s/api/v1/auth/reset-password?token=%s&email=%s".formatted(baseUrl, token, user.getEmail());
        Context context = new Context();
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("resetPasswordLink", verificationLink);
        String htmlContent = templateEngine.process("reset-password", context);
        log.info("::::: Reset password mail ready to be sent to {} :::::", user.getEmail());
        CompletableFuture.runAsync(() -> mailSenderService
                .sendEmail(user.getEmail(), "Reset Your Password", htmlContent));
    }
}
