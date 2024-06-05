package com.morris.opensquare.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.morris.opensquare.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.morris.opensquare.TestHelper.asJSONString;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"coverage"})
class DropDownOptionsTest {
    private static final String ORG_1 = "Apple";
    private static final String ORG_2 = "MongoDB";
    private static final String ORG_3 = "Microsoft";
    private static final String PROFESSION_1 = "Data Analyst";
    private static final String PROFESSION_2 = "Software Analyst";
    private static final String PROFESSION_3 = "Social Media Analyst";
    private static final String AGE_RANGE_1 = "18-20";
    private static final String AGE_RANGE_2 = "21-30";
    private static final String AGE_RANGE_3 = "31-40";
    private static final String AGE_RANGE_4 = "41-50";
    private static DropDownOptions dropDownOptions;
    private static String organizationsAsString;
    private static String professionsAsString;
    private static String ageRangesAsString;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        dropDownOptions = TestHelper.getDropDownOptions();

        organizationsAsString = asJSONString(dropDownOptions.getOrganizations());
        professionsAsString = asJSONString(dropDownOptions.getProfessions());
        ageRangesAsString = asJSONString(dropDownOptions.getAges());
    }

    @Test
    void getAges() {
        assertAll(
                () -> assertEquals(ageRangesAsString, asJSONString(dropDownOptions.getAges())),
                () -> assertEquals(4, dropDownOptions.getAges().size())
        );
    }

    @Test
    void setAges() throws JsonProcessingException {
        dropDownOptions.setAges(List.of(AGE_RANGE_1, AGE_RANGE_2, AGE_RANGE_3, AGE_RANGE_4));
        assertNotEquals(ageRangesAsString, asJSONString(dropDownOptions.getAges()));

    }

    @Test
    void getOrganizations() {
        assertAll(
                () -> assertEquals(organizationsAsString, asJSONString(dropDownOptions.getOrganizations())),
                () -> assertEquals(3, dropDownOptions.getOrganizations().size())
        );
    }

    @Test
    void setOrganizations() throws JsonProcessingException {
        dropDownOptions.setOrganizations(List.of(ORG_1, ORG_2, ORG_3));
        assertNotEquals(organizationsAsString, asJSONString(dropDownOptions.getOrganizations()));
    }

    @Test
    void getProfessions() {
        assertAll(
                () -> assertEquals(professionsAsString, asJSONString(dropDownOptions.getProfessions())),
                () -> assertEquals(3, dropDownOptions.getOrganizations().size())
        );
    }

    @Test
    void setProfessions() throws JsonProcessingException {
        dropDownOptions.setProfessions(List.of(PROFESSION_1, PROFESSION_2, PROFESSION_3));
        assertNotEquals(professionsAsString, asJSONString(dropDownOptions.getProfessions()));
    }

    @Test
    void builderPatternValid() {
        DropDownOptions dropDownOptionsBuilder = DropDownOptions.builder()
                .ages(List.of(AGE_RANGE_1, AGE_RANGE_2, AGE_RANGE_3, AGE_RANGE_4))
                .professions(List.of(PROFESSION_1, PROFESSION_2, PROFESSION_3))
                .organizations(List.of(ORG_1, ORG_2, ORG_3))
                .build();

        assertAll(
                () -> assertNotNull(dropDownOptionsBuilder),
                () -> assertEquals(4, dropDownOptionsBuilder.getAges().size()),
                () -> assertEquals(3, dropDownOptionsBuilder.getOrganizations().size()),
                () -> assertEquals(3, dropDownOptionsBuilder.getProfessions().size()),
                () -> assertTrue(dropDownOptionsBuilder.getAges().contains(AGE_RANGE_1)),
                () -> assertTrue(dropDownOptionsBuilder.getOrganizations().contains(ORG_1)),
                () -> assertTrue(dropDownOptionsBuilder.getProfessions().contains(PROFESSION_1))
        );
    }
}