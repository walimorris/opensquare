package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.validations.DisposableEmailDomain;
import com.morris.opensquare.repositories.DisposableEmailDomainRepository;
import com.morris.opensquare.services.EmailValidationService;
import com.morris.opensquare.services.loggers.LoggerService;
import org.apache.commons.validator.routines.EmailValidator;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailValidationServiceImpl implements EmailValidationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailValidationServiceImpl.class);
    private final DisposableEmailDomainRepository disposableEmailDomainRepository;
    private final LoggerService loggerService;

    @Autowired
    public EmailValidationServiceImpl(DisposableEmailDomainRepository disposableEmailDomainRepository,
                                      LoggerService loggerService) {

        this.disposableEmailDomainRepository = disposableEmailDomainRepository;
        this.loggerService = loggerService;
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
        loggerService.saveLog(
                getClass().getName(),
                "Invalid Email Search attempt on address: " + emailAddress,
                Optional.of(LOGGER)
        );
        return null;
    }

    @Override
    public String getEmailDomain(String emailAddress) {
        if (isValidEmail(emailAddress)) {
            return emailAddress.split("@")[1];
        }
        return null;
    }

    private boolean isValidEmail(String emailAddress) {
        return EmailValidator.getInstance().isValid(emailAddress);
    }

    private DisposableEmailDomain getDisposableEmailDomain(String domain) {
        return disposableEmailDomainRepository.findByDomainName(domain);
    }
}
