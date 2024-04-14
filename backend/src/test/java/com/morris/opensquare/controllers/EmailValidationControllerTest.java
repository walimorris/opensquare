package com.morris.opensquare.controllers;

import com.morris.opensquare.TestHelper;
import com.morris.opensquare.models.validations.DisposableEmailDomain;
import com.morris.opensquare.services.EmailValidationService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailValidationControllerTest {

    @MockBean
    private EmailValidationService emailValidationService;

    @Autowired
    MockMvc mockModelViewController;

    private static final String GET_DISPOSABLE_EMAIL_DOMAIN_REQUEST = "/opensquare/api/verify/disposable";
    private static final String EMAIL_ADDRESS_PARAM = "emailAddress";

    private static final String BLAZE_IT_EMAIL_DOMAIN = "420blaze.it";
    private static final String BLAZE_IT_DISPOSABLE_EMAIL_VALID = "2HI@420blaze.it";
    private static final String BLAZE_IT_DISPOSABLE_EMAIL_INVALID = "2HI@#420blaze.it";

    @Test
    void getValidDisposableEmail() throws Exception {
        DisposableEmailDomain disposableEmailDomain = new DisposableEmailDomain.Builder()
                .id(new ObjectId())
                .domainName(BLAZE_IT_EMAIL_DOMAIN)
                .build();
        String disposableEmailDomainAsString = TestHelper.writeValueAsString(disposableEmailDomain);

        when(emailValidationService.findDisposableEmailDomain(BLAZE_IT_DISPOSABLE_EMAIL_VALID))
                .thenReturn(disposableEmailDomain);

        this.mockModelViewController.perform(get(GET_DISPOSABLE_EMAIL_DOMAIN_REQUEST).param(EMAIL_ADDRESS_PARAM, BLAZE_IT_DISPOSABLE_EMAIL_VALID))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(disposableEmailDomainAsString));
    }

    @Test
    void getInvalidDisposableEmail() throws Exception {
        when(emailValidationService.findDisposableEmailDomain(BLAZE_IT_DISPOSABLE_EMAIL_INVALID))
                .thenReturn(null);

        this.mockModelViewController.perform(get(GET_DISPOSABLE_EMAIL_DOMAIN_REQUEST).param(EMAIL_ADDRESS_PARAM, BLAZE_IT_DISPOSABLE_EMAIL_INVALID))
                .andExpect(status().is4xxClientError());
    }
}
