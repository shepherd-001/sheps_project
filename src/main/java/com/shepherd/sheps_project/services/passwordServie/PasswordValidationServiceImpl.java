package com.shepherd.sheps_project.services.passwordServie;

import com.shepherd.sheps_project.exceptions.PasswordValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;


@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordValidationServiceImpl implements PasswordValidationService{
    private final RestTemplate restTemplate;
    @Value("${have_i_been_pawned_url}")
    private String haveIBeenPawnedUrl;


    @Override
    public boolean isPasswordBreached(String password) {
        checkPasswordNotBlank(password);
        String sha1Hash = DigestUtils.sha1Hex(password).toUpperCase();
        String prefix = sha1Hash.substring(0, 5);
        String suffix = sha1Hash.substring(5);

        String apiUrl = String.format("%s/%s", haveIBeenPawnedUrl, prefix);
        try{
            log.info("\n\nChecking password breach\n");
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
                return response.getBody().contains(suffix);
            }
        }catch (HttpClientErrorException ex){
            log.error("\n\nError occurred during password breach check: {}\n", ex.getMessage());
            HttpStatus statusCode = HttpStatus.valueOf(ex.getStatusCode().value());
            return switch (statusCode) {
                case BAD_REQUEST -> throw new PasswordValidationException("The password format is invalid", BAD_REQUEST.value());
                case UNAUTHORIZED -> throw new PasswordValidationException("API key is missing or invalid", UNAUTHORIZED.value());
                case FORBIDDEN -> throw new PasswordValidationException("User-Agent header is missing in the request", FORBIDDEN.value());
                case NOT_FOUND -> false; //Password not breached
                case TOO_MANY_REQUESTS -> throw new PasswordValidationException("Rate limit exceeded. Please try again later", TOO_MANY_REQUESTS.value());
                case SERVICE_UNAVAILABLE -> throw new PasswordValidationException("Password validation service is temporarily unavailable. Please try again later", SERVICE_UNAVAILABLE.value());
                default -> throw new PasswordValidationException(String.format("Client error exception when validating password. Code %s", statusCode.value()), statusCode.value());
            };
        }
        return false;
    }

    private static void checkPasswordNotBlank(String password) {
        if(password.isBlank())
            throw new PasswordValidationException("Password is required to proceed validation", 400);
    }
}
