package com.shepherd.sheps_project.services.passwordServie;

import com.shepherd.sheps_project.exceptions.PasswordValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordValidationServiceImpl implements PasswordValidationService{
    private final RestTemplate restTemplate;
    private static final String PASSWORD_VALIDATION_URL = "https://api.pwnedpasswords.com/range";


    @Override
    public boolean isPasswordBreached(String password) {
        checkPasswordNotBlank(password);
        String sha1Hash = DigestUtils.sha1Hex(password).toUpperCase();
        String prefix = sha1Hash.substring(0, 5);
        String suffix = sha1Hash.substring(5);

        String apiUrl = String.format("%s/%s", PASSWORD_VALIDATION_URL, prefix);
        try{
            log.info("\n\nChecking password breach\n");
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
                log.info("\n\nPawned password response: {}\n", response.getBody());
                return response.getBody().contains(suffix);
            }
        }catch (HttpClientErrorException ex){
            HttpStatusCode statusCode = ex.getStatusCode();
            return switch (statusCode.value()) {
                case 400 -> throw new PasswordValidationException("The password format is invalid");
                case 401 -> throw new PasswordValidationException("API key is missing or invalid");
                case 403 -> throw new PasswordValidationException("User-Agent header is missing in the request");
                case 404 -> false; //Password not breached
                case 429 -> throw new PasswordValidationException("Rate limit exceeded. Please try again later");
                case 503 -> throw new PasswordValidationException("Password validation service is temporarily unavailable. Please try again later");
                default -> throw new PasswordValidationException(String.format("Client error exception when validating password. Code %s", statusCode.value()));
            };
        }
        return false;
    }

    private static void checkPasswordNotBlank(String password) {
        if(password.isBlank())
            throw new PasswordValidationException("Password is required to proceed validation");
    }
}
