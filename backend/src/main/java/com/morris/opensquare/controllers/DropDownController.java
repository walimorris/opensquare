package com.morris.opensquare.controllers;

import com.morris.opensquare.services.DropDownService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/opensquare/api/dropdowns")
public class DropDownController {
    private final DropDownService dropDownService;
    private static final String AGES = "ages";
    private static final String PROFESSIONS = "professions";
    private static final String ORGANIZATIONS = "organizations";

    @Autowired
    public DropDownController(DropDownService dropDownService) {
        this.dropDownService = dropDownService;
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<String>> getOrganizations(HttpServletRequest request) {
        return dropDownOptionsFromAttribute(ORGANIZATIONS, request);
    }

    @GetMapping("/professions")
    public ResponseEntity<List<String>> getProfessions(HttpServletRequest request) {
        return dropDownOptionsFromAttribute(PROFESSIONS, request);
    }

    @GetMapping("/age_ranges")
    public ResponseEntity<List<String>> getAgeRanges(HttpServletRequest request) {
        return dropDownOptionsFromAttribute(AGES, request);
    }

    /**
     * Get drop-down options from given attribute. There are different attributes
     * and each attribute contains a list of options based on the category.
     *
     * @param attribute the drop-down attribute (i.e., professions, organizations)
     * @param request {@link HttpServletRequest}
     *
     * @return {@link ResponseEntity} list of drop-down options
     */
    ResponseEntity<List<String>> dropDownOptionsFromAttribute(String attribute, HttpServletRequest request) {
        List<String> sessionAttributes = getDropDownOptionsFromSession(attribute, request);

        if (sessionAttributes != null) {
            return attributeResponseEntity(sessionAttributes);
        }

        switch (attribute) {
            case AGES:
                List<String> ageRanges = dropDownService.getAgeRanges();
                request.getSession().setAttribute(AGES, ageRanges);
                return attributeResponseEntity(ageRanges);

            case ORGANIZATIONS:
                List<String> organizations = dropDownService.getOrganizations();
                request.getSession().setAttribute(ORGANIZATIONS, organizations);
                return attributeResponseEntity(organizations);

            default:
                List<String> professions = dropDownService.getProfessions();
                request.getSession().setAttribute(PROFESSIONS, professions);
                return attributeResponseEntity(professions);
        }
    }

    /**
     * Get attribute drop-down options from current session.
     *
     * @param attribute the drop-down attribute (i.e., professions, organizations)
     * @param request {@link HttpServletRequest}
     *
     * @return {@link List<String>} drop-down options
     */
    @SuppressWarnings("unchecked")
    private List<String> getDropDownOptionsFromSession(String attribute, HttpServletRequest request) {
        HttpSession session = request.getSession();

        switch(attribute) {
            case AGES -> { return (List<String>) session.getAttribute(AGES); }
            case ORGANIZATIONS -> { return (List<String>) session.getAttribute(ORGANIZATIONS); }
            default -> { return (List<String>) session.getAttribute(PROFESSIONS); }
        }
    }

    /**
     * Get attribute response entity. A list of drop-down options.
     *
     * @param dropdownOptions {@link List<String>}
     * @return {@link ResponseEntity}
     */
    private ResponseEntity<List<String>> attributeResponseEntity(List<String> dropdownOptions) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dropdownOptions);
    }
}
