package com.morris.opensquare.controllers;

import com.morris.opensquare.services.DropDownService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/opensquare/api/dropdowns")
public class DropDownController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DropDownController.class);

    private final DropDownService dropDownService;

    @Autowired
    public DropDownController(DropDownService dropDownService) {
        this.dropDownService = dropDownService;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/organizations")
    public ResponseEntity<List<String>> getOrganizations(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<String> sessionOrganizations = (List<String>) session.getAttribute("organizations");
        if (sessionOrganizations != null) {
            LOGGER.info("REUSING Organizations FROM SESSION");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sessionOrganizations);
        }
        LOGGER.info("CALLING ORGANIZATIONS DROPDOWN REQUEST");
        List<String> organizations = dropDownService.getOrganizations();
        request.getSession().setAttribute("organizations", organizations);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(organizations);
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/professions")
    public ResponseEntity<List<String>> getProfessions(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<String> sessionProfessions = (List<String>) session.getAttribute("professions");
        if (sessionProfessions != null) {
            LOGGER.info("REUSING Professions FROM SESSION");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sessionProfessions);
        }
        LOGGER.info("CALLING Professions DROPDOWN REQUEST");
        List<String> professions = dropDownService.getProfessions();
        request.getSession().setAttribute("professions", professions);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(professions);
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/age_ranges")
    public ResponseEntity<List<String>> getAgeRanges(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<String> sessionAgeRanges = (List<String>) session.getAttribute("ages");
        if (sessionAgeRanges != null) {
            LOGGER.info("REUSING AGE RANGES FROM SESSION");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sessionAgeRanges);
        }
        LOGGER.info("CALLING AGE RANGES DROPDOWN REQUEST");
        List<String> ageRanges = dropDownService.getAgeRanges();
        request.getSession().setAttribute("ages", ageRanges);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ageRanges);
    }
}
