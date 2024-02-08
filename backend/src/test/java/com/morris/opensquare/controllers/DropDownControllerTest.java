package com.morris.opensquare.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.morris.opensquare.TestHelper;
import com.morris.opensquare.models.DropDownOptions;
import com.morris.opensquare.services.DropDownService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DropDownControllerTest {

    @Autowired
    private MockMvc mockModelViewController;

    @MockBean
    HttpServletRequest httpServletRequest;

    @MockBean
    DropDownService dropDownService;

    private static final String ORGANIZATIONS_REQUEST = "/opensquare/api/dropdowns/organizations";
    private static final String PROFESSIONS_REQUEST = "/opensquare/api/dropdowns/professions";
    private static final String AGE_RANGES_REQUEST = "/opensquare/api/dropdowns/age_ranges";

    private static final String ORGANIZATIONS = "organizations";
    private static final String PROFESSIONS = "professions";
    private static final String AGE_RANGES = "ages";

    private static final String ORG_1 = "Graphika";
    private static final String ORG_2 = "RAND Organization";
    private static final String ORG_3 = "Amazon Web Services";
    private static final String PROFESSION_1 = "Software Engineer";
    private static final String PROFESSION_2 = "Data Scientist";
    private static final String PROFESSION_3 = "Intelligence Analyst";
    private static final String AGE_RANGE_1 = "18-25";
    private static final String AGE_RANGE_2 = "26-35";
    private static final String AGE_RANGE_3 = "36-45";
    private static DropDownOptions dropDownOptions;
    private static String organizationsAsString;
    private static String professionsAsString;
    private static String ageRangesAsString;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        dropDownOptions = getDropDownOptions();

        organizationsAsString = asJSONString(dropDownOptions.getOrganizations());
        professionsAsString = asJSONString(dropDownOptions.getProfessions());
        ageRangesAsString = asJSONString(dropDownOptions.getAges());
    }

    @Test
    void getOrganizationsFromDataStore() throws Exception {
        when(dropDownService.getOrganizations()).thenReturn(dropDownOptions.getOrganizations());
        this.mockModelViewController.perform(get(ORGANIZATIONS_REQUEST))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(organizationsAsString));
    }

    @Test
    void getOrganizationFromSession() throws Exception {
        this.mockModelViewController
                .perform(get(ORGANIZATIONS_REQUEST).sessionAttr(ORGANIZATIONS, dropDownOptions.getOrganizations()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(organizationsAsString));
    }

    @Test
    void getProfessionsFromDataStore() throws Exception {
        when(dropDownService.getProfessions()).thenReturn(dropDownOptions.getProfessions());
        this.mockModelViewController.perform(get(PROFESSIONS_REQUEST))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(professionsAsString));
    }

    @Test
    void getProfessionsFromSession() throws Exception {
        this.mockModelViewController
                .perform(get(PROFESSIONS_REQUEST).sessionAttr(PROFESSIONS, dropDownOptions.getProfessions()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(professionsAsString));
    }

    @Test
    void getAgeRangesFromDataStore() throws Exception {
        when(dropDownService.getAgeRanges()).thenReturn(dropDownOptions.getAges());
        this.mockModelViewController.perform(get(AGE_RANGES_REQUEST))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(ageRangesAsString));
    }

    @Test
    void getAgeRangesFromSession() throws Exception {
        this.mockModelViewController
                .perform(get(AGE_RANGES_REQUEST).sessionAttr(AGE_RANGES, dropDownOptions.getAges()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(ageRangesAsString));
    }

    private DropDownOptions getDropDownOptions() {
        return new DropDownOptions.Builder()
                .ages(getAgeRanges())
                .professions(getProfessions())
                .organizations(getOrganizations())
                .build();
    }

    private List<String> getOrganizations() {
        List<String> organizationsList = new ArrayList<>();
        organizationsList.add(ORG_1);
        organizationsList.add(ORG_2);
        organizationsList.add(ORG_3);
        return organizationsList;
    }

    private List<String> getProfessions() {
        List<String> professionsList = new ArrayList<>();
        professionsList.add(PROFESSION_1);
        professionsList.add(PROFESSION_2);
        professionsList.add(PROFESSION_3);
        return professionsList;
    }

    private List<String> getAgeRanges() {
        List<String> ageRangesList = new ArrayList<>();
        ageRangesList.add(AGE_RANGE_1);
        ageRangesList.add(AGE_RANGE_2);
        ageRangesList.add(AGE_RANGE_3);
        return ageRangesList;
    }

    private String asJSONString(Object object) throws JsonProcessingException {
        if (object != null) {
            return TestHelper.writeValueAsString(object);
        }
        return null;
    }
}
