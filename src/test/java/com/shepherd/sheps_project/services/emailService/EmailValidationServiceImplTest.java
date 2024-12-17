package com.shepherd.sheps_project.services.emailService;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;


@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
class EmailValidationServiceImplTest {
    private final EmailValidationService emailValidationService;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateEmail() {
        String email = "mytest123@gmail.com";
//        Map<String, Object> validateEmailResponse = emailValidationService.validateEmail(email);
//        assertThat(validateEmailResponse.get("status")).isEqualTo("valid");
    }

    @Test
    void isDisposableEmail() {
    }
}