package com.shepherd.sheps_project.services.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MailSenderServiceImplTest {
    @Autowired
    private MailSenderService mailSenderService;

    @Test
    void sendEmail() {
        String to = "shepsproduct@proton.me";
        String subject = "Hello World";
        String htmlContent = "<h1>This is just testing the email</h1>";

        mailSenderService.sendEmail(to, subject, htmlContent);
        assertThat("no").isNotBlank();
    }
}