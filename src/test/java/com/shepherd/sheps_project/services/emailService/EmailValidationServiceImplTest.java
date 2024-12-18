package com.shepherd.sheps_project.services.emailService;

import com.shepherd.sheps_project.exceptions.EmailValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
class EmailValidationServiceImplTest {
    @Autowired
    private EmailValidationService emailValidationService;


    @Test
    void testValidEmailStatusInLowerCase(){
        String email = "mytest123@gmail.com";
        boolean isValid = emailValidationService.isValidEmail(email);
        assertThat(isValid).isTrue();
    }

    @Test
    void testValidEmailStatusInUpperCase(){
        String email = "MYTEST123@GMAIL.COM";
        boolean isValid = emailValidationService.isValidEmail(email);
        assertThat(isValid).isTrue();
    }

    @Test
    void testDoNotMailWithAcceptableSubStatus(){
        String email = "info@mytech.com";
        boolean isValid = emailValidationService.isValidEmail(email);
        assertThat(isValid).isTrue();
    }

    @Test
    void testDoNotMailWithUnacceptableSubStatus(){
        String email = "mentos@information.com";
         EmailValidationException exception = assertThrows(
                EmailValidationException.class,
                ()-> emailValidationService.isValidEmail(email),
                 "Expecting email validation exception to be thrown, but it didn't"
        );
         assertThat(exception.getMessage()).isEqualTo("Error validating email address. Try again with a valid email address");
    }

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

    @Test
    void testDisposableEmail(){
        String email = "mytest@mailinator.com";
         EmailValidationException exception = assertThrows(
                EmailValidationException.class,
                ()-> emailValidationService.isValidEmail(email),
                 "Expecting email validation exception to be thrown, but it didn't"
        );
         assertThat(exception.getMessage()).isEqualTo("Disposable email address is not allowed");
    }
}
