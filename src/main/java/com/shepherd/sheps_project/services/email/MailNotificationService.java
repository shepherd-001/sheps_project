package com.shepherd.sheps_project.services.email;

import com.shepherd.sheps_project.data.models.User;

public interface MailNotificationService {
    void sendVerificationMail(User user, String token);
    void sendResetPasswordMail(User user, String token);
}
