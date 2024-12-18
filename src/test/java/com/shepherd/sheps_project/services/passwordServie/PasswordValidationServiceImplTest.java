package com.shepherd.sheps_project.services.passwordServie;

import com.shepherd.sheps_project.exceptions.PasswordValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
class PasswordValidationServiceImplTest {
    @Autowired
    private PasswordValidationService passwordValidationService;

    @Test
    void shouldReturnTrueWhenPasswordIsBreached() {
        String password = "Password123$";
        boolean isPasswordBreached = passwordValidationService.isPasswordBreached(password);
        assertThat(isPasswordBreached).isTrue();
    }

    @Test
    void shouldReturnFalseWhenPasswordIsNotBreached() {
        String password = "TupicmStron123$%";
        boolean isPasswordBreached = passwordValidationService.isPasswordBreached(password);
        assertThat(isPasswordBreached).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsBlank(){
        String password = " ";
        PasswordValidationException exception = assertThrows(
                PasswordValidationException.class,
                ()-> passwordValidationService.isPasswordBreached(password),
                "Expecting password validation exception to be thrown, but it didn't"
        );
        assertThat(exception.getMessage()).isEqualTo("Password is required to proceed validation");
    }
}