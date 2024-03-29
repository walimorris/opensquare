package com.morris.opensquare.controllers;

import jakarta.servlet.http.HttpServletRequest;
import com.morris.opensquare.models.validations.DisposableEmailDomain;
import com.morris.opensquare.services.impl.EmailValidationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/opensquare/api/verify")
public class EmailValidationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailValidationController.class);

    private final EmailValidationServiceImpl emailValidationService;

    @Autowired
    public EmailValidationController(EmailValidationServiceImpl emailValidationService) {
        this.emailValidationService = emailValidationService;
    }

    @GetMapping("/disposable")
    public ResponseEntity<DisposableEmailDomain> getDisposableEmail(@RequestParam String emailAddress, HttpServletRequest request) {
        LOGGER.info("Verify Email: " + emailAddress);
        DisposableEmailDomain possibleDisposableEmailDomain = emailValidationService.findDisposableEmailDomain(emailAddress);
        if (possibleDisposableEmailDomain != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(possibleDisposableEmailDomain);
        }
        return ResponseEntity.ofNullable(null);
    }

    @PostMapping("/add")
    public ResponseEntity<DisposableEmailDomain> addDisposableEmailDomain(@RequestParam String domain, HttpServletRequest request) {
        LOGGER.info("Email Domain: " + domain);

        // todo: sanitize
        DisposableEmailDomain disposableEmailDomainResult = emailValidationService.addDisposableEmailDomain(domain);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(disposableEmailDomainResult);

    }
}
