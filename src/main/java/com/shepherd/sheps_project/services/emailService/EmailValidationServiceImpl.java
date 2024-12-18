package com.shepherd.sheps_project.services.emailService;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shepherd.sheps_project.exceptions.EmailValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailValidationServiceImpl implements EmailValidationService{
    @Builder
    @Getter
    private static class EmailValidationResponse{
        private String address;
        private String status;
        @JsonProperty("sub_status")
        private String subStatus;
        @JsonProperty("free_email")
        private boolean freeEmail;
        @JsonProperty("did_you_mean")
        private String didYouMean;
        private String account;
        private String domain;
        @JsonProperty("domain_age_days")
        private String domainAgeDays;
        @JsonProperty("active_in_days")
        private String activeInDays;
        @JsonProperty("smtp_provider")
        private String smtpProvider;
        @JsonProperty("mx_record")
        private String mxRecord;
        @JsonProperty("mx_found")
        private boolean mxFound;
        private String firstname;
        private String lastname;
        private String gender;
        private String country;
        private String region;
        private String city;
        private String zipcode;
        @JsonProperty("processed_at")
        private String processedAt;
    }

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
    public boolean isValidEmail(String email) {
        try{
            log.info("\n\nInitiating email validation\n");
            checkEmailNotBlank(email);
            isDisposableEmail(email);
            String url = buildValidationUrl(email);
            log.info("\n\nValidating email the email address\n");
            EmailValidationResponse emailValidationResponse = restTemplate.getForObject(url, EmailValidationResponse.class);
            return isEmailValidAndAllowed(Objects.requireNonNull(emailValidationResponse));
        }catch (RestClientException ex){
            log.error("\n\nError during email validation: {}\n", ex.getMessage());
            throw new EmailValidationException(ex.getMessage());
        }
    }

    private static boolean isEmailValidAndAllowed(EmailValidationResponse validationResponse) {
        String status = validationResponse.getStatus() != null ? validationResponse.getStatus().toLowerCase() : "";
        String subStatus = validationResponse.getSubStatus() != null ? validationResponse.getSubStatus().toLowerCase() : "";
        int domainAge = validationResponse.getDomainAgeDays() != null ? Integer.parseInt(validationResponse.getDomainAgeDays()) : 0;

        return switch (status) {
            case "valid" -> {
                log.info("\n\nValid email-> sub status: '{}' and domain age: '{}'\n", subStatus, domainAge);
                yield domainAge > 30;
            }
            case "do_not_mail" -> handleDoNoMailSubStatus(subStatus, domainAge);
            default -> throw new EmailValidationException("Error validating email address." +
                    " Try again with a valid email address");
        };
    }

    private static boolean handleDoNoMailSubStatus(String subStatus, int domainAge) {
        return switch (subStatus) {
            case "role_based", "role_based_catch_all" -> {
                log.info("\n\nValid email -> status: 'do_not_mail' sub status: '{}', domain age: '{}'\n",subStatus, domainAge);
                yield domainAge > 30;
            }
            case "disposable" -> {
                log.warn("\n\nThe email address entered is disposable\n");
                throw new EmailValidationException("Disposable email address is not allowed");
            }
            default -> {
                log.warn("\n\nUnacceptable email address with sub status: {}\n", subStatus);
                throw new EmailValidationException("Unacceptable email address. Try again with a valid email address");
            }
        };
    }

    private static void checkEmailNotBlank(String email) {
        if(email.isBlank())
            throw new EmailValidationException("Please enter a valid email address");
    }

    private static void isDisposableEmail(String email){
        String domain = extractDomainFromEmail(email.trim());
        if(DISPOSABLE_EMAIL_DOMAINS.contains(domain)){
            log.warn("\n\nDisposable email address\n");
            throw new EmailValidationException("Disposable email address is not allowed");
        }
    }

    private static String extractDomainFromEmail(String email) {
        return email.substring(email.indexOf('@') + 1);
    }

    private String buildValidationUrl(String email) {
        return UriComponentsBuilder.fromUriString(validationUrl)
                .queryParam("api_key", apiKey)
                .queryParam("email", email)
                .toUriString();
    }
}
