package com.morris.opensquare.services.impl;

import org.bson.types.ObjectId;
import com.morris.opensquare.models.validations.DisposableEmailDomain;
import com.morris.opensquare.repositories.DisposableEmailDomainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailValidationService.class);

    private final DisposableEmailDomainRepository disposableEmailDomainRepository;

    @Autowired
    public EmailValidationService(DisposableEmailDomainRepository disposableEmailDomainRepository) {
        this.disposableEmailDomainRepository = disposableEmailDomainRepository;
    }

    public DisposableEmailDomain addDisposableEmailDomain(String domain) {
        DisposableEmailDomain disposableEmailDomain = new DisposableEmailDomain.Builder()
                .id(new ObjectId())
                .domainName(domain)
                .build();
        return disposableEmailDomainRepository.save(disposableEmailDomain);
    }

    public DisposableEmailDomain findDisposableEmailDomain(String emailAddress) {
        if (isValidEmail(emailAddress)) {
            String domain = getEmailDomain(emailAddress);
            return getDisposableEmailDomain(domain);
        }
        return null;
    }

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private boolean isValidEmail(String emailAddress) {
        if (Pattern.compile(EMAIL_REGEX).matcher(emailAddress).matches()) {
            return true;
        }
        throw new IllegalArgumentException("Invalid Email Address: " + emailAddress);
    }

    private String getEmailDomain(String emailAddress) {
        if (isValidEmail(emailAddress)) {
            return emailAddress.split("@")[1];
        }
        return null;
    }

    private DisposableEmailDomain getDisposableEmailDomain(String domain) {
        return disposableEmailDomainRepository.findByDomainName(domain);
    }
}
