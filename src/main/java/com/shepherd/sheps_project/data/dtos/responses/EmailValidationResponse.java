package com.shepherd.sheps_project.data.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailValidationResponse {
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
