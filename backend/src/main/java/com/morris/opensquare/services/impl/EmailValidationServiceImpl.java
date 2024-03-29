package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.validations.DisposableEmailDomain;
import com.morris.opensquare.repositories.DisposableEmailDomainRepository;
import com.morris.opensquare.services.EmailValidationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidationServiceImpl implements EmailValidationService {
    private final DisposableEmailDomainRepository disposableEmailDomainRepository;

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    public EmailValidationServiceImpl(DisposableEmailDomainRepository disposableEmailDomainRepository) {
        this.disposableEmailDomainRepository = disposableEmailDomainRepository;
    }

    @Override
    public DisposableEmailDomain addDisposableEmailDomain(String domain) {
        DisposableEmailDomain disposableEmailDomain = new DisposableEmailDomain.Builder()
                .id(new ObjectId())
                .domainName(domain.trim())
                .build();
        return disposableEmailDomainRepository.insert(disposableEmailDomain);
    }

    @Override
    public DisposableEmailDomain findDisposableEmailDomain(String emailAddress) {
        if (isValidEmail(emailAddress)) {
            String domain = getEmailDomain(emailAddress);
            return getDisposableEmailDomain(domain);
        }
        return null;
    }

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
