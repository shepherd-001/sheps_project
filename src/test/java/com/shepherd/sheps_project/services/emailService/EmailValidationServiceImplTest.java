package com.shepherd.sheps_project.services.emailService;

import com.shepherd.sheps_project.exceptions.EmailValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
class EmailValidationServiceImplTest {
    @Autowired
    private EmailValidationService emailValidationService;
    private static final Random RANDOM = new Random();
//
//    @Test
//    void testValidateEmail() {
//        String email = "mytest123@gmail.com";
//        EmailValidationResponse validateEmailResponse = emailValidationService.validateEmail(email);
//        assertThat(validateEmailResponse.getAddress()).isEqualTo(email);
//        assertThat(validateEmailResponse.getStatus()).isEqualTo("valid");
//        assertThat(validateEmailResponse.getSubStatus()).isNotNull();
//        assertThat(validateEmailResponse.getProcessedAt()).isNotBlank();
//    }
//
//    @Test
//    void testThatEmailIsDisposable() {
//        String[] emailDomain = {"10minutemail.com", "guerrillamail.com", "mailinator.com",
//                "temp-mail.org", "throwawaymail.com", "fakemailgenerator.com", "mohmal.com"};
//
//        int randomIndex = RANDOM.nextInt(emailDomain.length);
//        String email = String.format("testing123@%s", emailDomain[randomIndex]);
//        assertThat(emailValidationService.isDisposableEmail(email)).isTrue();
//    }
//
//    @Test
//    void testThatEmailIsNotDisposable() {
//        String[] emailDomain = {"protonmail.com", "outlook.com", "gmail.com",
//                "icloud.com", "outlook.com"};
//
//        int randomIndex = RANDOM.nextInt(emailDomain.length);
//        String email = String.format("testing123@%s", emailDomain[randomIndex]);
//        assertThat(emailValidationService.isDisposableEmail(email)).isFalse();
//    }

    @Test
    void testBlankEmail(){
        String email = " ";
         EmailValidationException exception = assertThrows(
                EmailValidationException.class,
                ()-> emailValidationService.isValidEmail(email),
                 "Expecting email validation exception to be thrown, but it didn't"
        );
         assertThat(exception.getMessage()).isEqualTo("Please enter a valid email address");
    }
}
