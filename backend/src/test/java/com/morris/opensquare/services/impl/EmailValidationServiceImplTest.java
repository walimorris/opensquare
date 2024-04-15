package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.validations.DisposableEmailDomain;
import com.morris.opensquare.repositories.DisposableEmailDomainRepository;
import com.morris.opensquare.services.EmailValidationService;
import org.apache.commons.validator.routines.EmailValidator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"coverage"})
class EmailValidationServiceImplTest {

    @Autowired
    EmailValidationService emailValidationService;

    @MockBean
    DisposableEmailDomainRepository disposableEmailDomainRepository;

    private static final String BLAZE_IT_EMAIL_DOMAIN = "420blaze.it";
    private static final String BLAZE_IT_DISPOSABLE_EMAIL_VALID = "2HI@420blaze.it";
    private static final String BLAZE_IT_DISPOSABLE_EMAIL_INVALID = "2HI@#420blaze.it";

    @Test
    void findDisposableEmailDomainValid() {
        DisposableEmailDomain validDisposableEmailDomain = new DisposableEmailDomain.Builder()
                .id(new ObjectId())
                .domainName(BLAZE_IT_EMAIL_DOMAIN)
                .build();
        when(disposableEmailDomainRepository.findByDomainName(BLAZE_IT_EMAIL_DOMAIN))
                .thenReturn(validDisposableEmailDomain);
        DisposableEmailDomain disposableEmailDomain = emailValidationService.findDisposableEmailDomain(BLAZE_IT_DISPOSABLE_EMAIL_VALID);
        Assertions.assertEquals(disposableEmailDomain.getDomainName(), BLAZE_IT_EMAIL_DOMAIN);
    }

    @Test
    void getEmailDomainValid() {
        boolean isValidEmailAddress = isValidEmail(BLAZE_IT_DISPOSABLE_EMAIL_VALID);
        String domain;
        if (isValidEmailAddress) {
            domain = BLAZE_IT_DISPOSABLE_EMAIL_VALID.split("@")[1];
        } else {
            domain = null;
        }
        assertAll(
                () -> assertTrue(isValidEmailAddress),
                () -> assertEquals(domain, BLAZE_IT_EMAIL_DOMAIN)
        );
    }

    @Test
    void getEmailDomainInvalid() {
        boolean isValidEmailAddress = isValidEmail(BLAZE_IT_DISPOSABLE_EMAIL_INVALID);
        String domain;
        if (isValidEmailAddress) {
            domain = BLAZE_IT_DISPOSABLE_EMAIL_INVALID.split("@")[1];
        } else {
            domain = null;
        }
        assertAll(
                () -> assertFalse(isValidEmailAddress),
                () -> assertNull(domain)
        );
    }

    private boolean isValidEmail(String emailAddress) {
        return EmailValidator.getInstance().isValid(emailAddress);
    }
}
