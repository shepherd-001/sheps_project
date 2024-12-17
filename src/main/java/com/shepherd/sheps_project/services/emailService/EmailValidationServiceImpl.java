package com.shepherd.sheps_project.services.emailService;

import com.shepherd.sheps_project.exceptions.EmailValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailValidationServiceImpl implements EmailValidationService{
    @Value("${zero_bounce_api_key}")
    private String apiKey;
    @Value("${zero_bounce_url}")
    private String validationUrl;
    private final RestTemplate restTemplate;


    private static final Set<String> DISPOSABLE_EMAIL_DOMAINS = Set.of(
            "10minutemail.com", "guerrillamail.com", "mailinator.com",
            "temp-mail.org", "throwawaymail.com", "fakemailgenerator.com",
            "mohmal.com", "getnada.com", "yopmail.com", "disposablemail.com",
            "maildrop.cc", "tempmailo.com", "pookmail.com", "emailondeck.com",
            "trashmail.com", "mailcatch.com", "tempmail.net", "fakeinbox.com",
            "jetable.org", "spamgourmet.com", "mytemp.email", "spambox.us",
            "tempmailaddress.com", "mailcatcher.com", "tempinbox.com",
            "tempmail.us", "dumpmail.de", "spamthis.co", "getairmail.com",
            "dispostable.com", "throwawaymail.net", "boun.cr", "on0.com",
            "dropmail.me", "safepostmail.com", "mailnesia.com",
            "randommail.org", "guerrillamail.net", "fakeemailgenerator.com"
    );

    @Override
    public Map<String, Object> validateEmail(String email) {
        try{
            log.info("\n\nInitiating email validation\n");
            checkEmailNotBlank(email);
            String url = buildValidationUrl(email);
            log.info("\n\nValidating email the email address\n");
            return restTemplate.getForObject(url, Map.class);
        }catch (HttpClientErrorException | HttpServerErrorException ex){
            log.error("\n\nError during email validation: {}\n", ex.getMessage());
            throw new EmailValidationException(ex.getMessage());
        }
    }

    private void checkEmailNotBlank(String email) {
        if(email.isBlank())
            throw new EmailValidationException("Please enter a valid email address");
    }

    private String buildValidationUrl(String email) {
        return UriComponentsBuilder
                .fromUri(URI.create(String.format("%s", validationUrl)))
                .queryParam("api_key", apiKey)
                .queryParam("email", email)
                .toUriString();
    }

    @Override
    public boolean isDisposableEmail(String email){
        checkEmailNotBlank(email);
        String domain = extractDomainFromEmail(email);
        return DISPOSABLE_EMAIL_DOMAINS.contains(domain);
    }

    private String extractDomainFromEmail(String email) {
        return email.substring(email.indexOf('@') + 1);
    }
}