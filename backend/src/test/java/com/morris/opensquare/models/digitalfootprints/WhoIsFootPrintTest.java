package com.morris.opensquare.models.digitalfootprints;

import com.morris.opensquare.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"coverage"})
class WhoIsFootPrintTest {

    private static final String DOMAIN_NAME_1 = "google.com";
    private static final String REGISTRAR_URL_1 = "http://www.markmonitor.com";
    private static final String REGISTRAR_1 = "MarkMonitor, Inc.";
    private static final String REGISTRANT_ORGANIZATION_1 = "Google LLC";
    private static final String REGISTRANT_EMAIL_1 = "Select Request Email Form at https://domains.markmonitor.com/whois/google.com";
    private static final String REGISTRAR_ABUSE_CONTACT_EMAIL = "abusecomplaints@markmonitor.com";
    private static final String ADMIN_COUNTRY = "US";
    private static final String UPDATED_DATE = "2019-09-09T15:39:04+0000";
    private static final String TECH_STATE_PROVINCE = "CA";
    private static final String TECH_ORGANIZATION = "Google LLC";
    private static final String REGISTRAR_REGISTRATION_EXPIRATION_DATE = "2028-09-13T07:00:00+0000";
    private static final String REGISTRAR_WHOIS_SERVER = "whois.markmonitor.com";
    private static final String REGISTRANT_COUNTRY = "US";
    private static final String REGISTRAR_ABUSE_CONTACT_PHONE = "+1.2086851750";
    private static final String ADMIN_EMAIL = "Select Request Email Form at https://domains.markmonitor.com/whois/google.com";
    private static final String DNSSEC = "unsigned";
    private static final String LAST_UPDATED_WHOIS_DATABASE = "2023-08-31T20:53:26+0000";
    private static final String REGISTRAR_IANA_ID = "292";
    private static final String DOMAIN_STATUS = "serverDeleteProhibited (https://www.icann.org/epp#serverDeleteProhibited)";
    private static final String CREATION_DATE = "1997-09-15T07:00:00+0000";
    private static final String REGISTRANT_STATE_PROVINCE = "CA";
    private static final String NAME_SERVER = "ns3.google.com";
    private static final String REGISTRY_DOMAIN_ID = "2138514_DOMAIN_COM-VRSN";
    private static final String ADMIN_ORGANIZATION = "Google LLC";
    private static final String TECH_COUNTRY = "US";
    private static final String URL_ICANN_COMPLAINT_FORM = "http://wdprs.internic.net/";
    private static final String TECH_EMAIL = "Select Request Email Form at https://domains.markmonitor.com/whois/google.com";
    private static final String WHOIS_FOOTPRINT_FILE = "backend/src/test/resources/models/WhoIsFootPrint.json";

    private static final String DOMAIN_NAME_2 = "yahoo.com";
    private static final String REGISTRAR_URL_2 = "http://www.random.com";
    private static final String REGISTRAR_2 = "Random, Inc.";
    private static final String REGISTRANT_ORGANIZATION_2 = "Yahoo LLC";
    private static final String REGISTRANT_EMAIL_2 = "Select Request Email Form at https://domains.random.com/whois/google.com";
    private static final String REGISTRAR_ABUSE_CONTACT_EMAIL_2 = "abusecomplaints@random.com";
    private static final String ADMIN_COUNTRY_2 = "IN";
    private static final String UPDATED_DATE_2 = "2019-10-09T15:39:04+0000";
    private static final String TECH_STATE_PROVINCE_2 = "NY";
    private static final String TECH_ORGANIZATION_2 = "Yahoo LLC";
    private static final String REGISTRAR_REGISTRATION_EXPIRATION_DATE_2 = "2028-10-13T07:00:00+0000";
    private static final String REGISTRAR_WHOIS_SERVER_2 = "whois.random.com";
    private static final String REGISTRANT_COUNTRY_2 = "IN";
    private static final String REGISTRAR_ABUSE_CONTACT_PHONE_2 = "+1.5555551234";
    private static final String ADMIN_EMAIL_2 = "Select Request Email Form at https://domains.random.com/whois/google.com";
    private static final String DNSSEC_2 = "signed";
    private static final String LAST_UPDATED_WHOIS_DATABASE_2 = "2023-10-31T20:53:26+0000";
    private static final String REGISTRAR_IANA_ID_2 = "293";
    private static final String DOMAIN_STATUS_2 = "serverDeleteProhibited (https://www.icann.org/random#serverDeleteProhibited)";
    private static final String CREATION_DATE_2 = "1997-10-15T07:00:00+0000";
    private static final String REGISTRANT_STATE_PROVINCE_2 = "NY";
    private static final String NAME_SERVER_2 = "ns3.yahoo.com";
    private static final String REGISTRY_DOMAIN_ID_2 = "1234567_DOMAIN_COM-VRSN";
    private static final String ADMIN_ORGANIZATION_2 = "Yahoo LLC";
    private static final String TECH_COUNTRY_2 = "IN";
    private static final String URL_ICANN_COMPLAINT_FORM_2 = "http://random.internic.net/";
    private static final String TECH_EMAIL_2 = "Select Request Email Form at https://domains.random.com/whois/google.com";

    private static final String REGISTRANT_NAME = "yahoo";
    private static final String REGISTRANT_STREET = "123 random street";
    private static final String REGISTRANT_CITY = "Mumbai";
    private static final String REGISTRANT_POSTAL_CODE = "654321-12";
    private static final String REGISTRANT_PHONE = "+16-555-455-2134";
    private static final String REGISTRANT_PHONE_EXT = "0001";
    private static final String REGISTRANT_FAX = "+16-555-455-2143";
    private static final String ADMIN_NAME = "yahoo-admin";
    private static final String ADMIN_STREET = "Mumbai Street";
    private static final String ADMIN_CITY = "Mumbai";
    private static final String ADMIN_STATE = "Maharashtra";
    private static final String ADMIN_POSTAL_CODE = "12345-56";
    private static final String ADMIN_PHONE = "+16-555-454-8989";
    private static final String ADMIN_PHONE_EXT = "555";
    private static final String ADMIN_FAX = "+16-555-8989-0001";
    private static final String TECH_NAME = "yahoo-tech";
    private static final String TECH_STREET = "mumbai rd";
    private static final String TECH_CITY = "mumbai";
    private static final String TECH_POSTAL_CODE = "12345";
    private static final String TECH_PHONE = "+17-555-555-0001";
    private static final String TECH_PHONE_EXT = "0002";
    private static final String TECH_FAX = "+17-555-555-0003";

    private static WhoIsFootPrint whoIsFootPrint;

    @BeforeEach
    void setUp() throws IOException {
        whoIsFootPrint = (WhoIsFootPrint) TestHelper.convertModelFromFile(WHOIS_FOOTPRINT_FILE, WhoIsFootPrint.class, null);
    }

    @Test
    void getDomainName() {
        assertEquals(DOMAIN_NAME_1, whoIsFootPrint.getDomainName());
    }

    @Test
    void setDomainName() {
        whoIsFootPrint.setDomainName(DOMAIN_NAME_2);
        assertNotEquals(DOMAIN_NAME_1, whoIsFootPrint.getDomainName());
        assertEquals(DOMAIN_NAME_2, whoIsFootPrint.getDomainName());
    }

    @Test
    void getRegistryDomainId() {
        assertEquals(REGISTRY_DOMAIN_ID, whoIsFootPrint.getRegistryDomainId());
    }

    @Test
    void setRegistryDomainId() {
        whoIsFootPrint.setRegistryDomainId(REGISTRY_DOMAIN_ID_2);
        assertNotEquals(REGISTRY_DOMAIN_ID, whoIsFootPrint.getRegistryDomainId());
        assertEquals(REGISTRY_DOMAIN_ID_2, whoIsFootPrint.getRegistryDomainId());
    }

    @Test
    void getRegistrarWhoisServer() {
        assertEquals(REGISTRAR_WHOIS_SERVER, whoIsFootPrint.getRegistrarWhoisServer());
    }

    @Test
    void setRegistrarWhoIsServer() {
        whoIsFootPrint.setRegistrarWhoIsServer(REGISTRAR_WHOIS_SERVER_2);
        assertNotEquals(REGISTRAR_WHOIS_SERVER, whoIsFootPrint.getRegistrarWhoisServer());
        assertEquals(REGISTRAR_WHOIS_SERVER_2, whoIsFootPrint.getRegistrarWhoisServer());
    }

    @Test
    void getRegistrarUrl() {
        assertEquals(REGISTRAR_URL_1, whoIsFootPrint.getRegistrarUrl());
    }

    @Test
    void setRegistrarUrl() {
        whoIsFootPrint.setRegistrarUrl(REGISTRAR_URL_2);
        assertNotEquals(REGISTRAR_URL_1, whoIsFootPrint.getRegistrarUrl());
        assertEquals(REGISTRAR_URL_2, whoIsFootPrint.getRegistrarUrl());
    }

    @Test
    void getUpdatedDate() {
        assertEquals(UPDATED_DATE, whoIsFootPrint.getUpdatedDate());
    }

    @Test
    void setUpdatedDate() {
        whoIsFootPrint.setUpdatedDate(UPDATED_DATE_2);
        assertNotEquals(UPDATED_DATE, whoIsFootPrint.getUpdatedDate());
        assertEquals(UPDATED_DATE_2, whoIsFootPrint.getUpdatedDate());
    }

    @Test
    void getCreationDate() {
        assertEquals(CREATION_DATE, whoIsFootPrint.getCreationDate());
    }

    @Test
    void setCreationDate() {
        whoIsFootPrint.setCreationDate(CREATION_DATE_2);
        assertNotEquals(CREATION_DATE, whoIsFootPrint.getCreationDate());
        assertEquals(CREATION_DATE_2, whoIsFootPrint.getCreationDate());
    }

    @Test
    void getRegistrarRegistrationExpirationDate() {
        assertEquals(REGISTRAR_REGISTRATION_EXPIRATION_DATE, whoIsFootPrint.getRegistrarRegistrationExpirationDate());
    }

    @Test
    void setRegistrarRegistrationExpirationDate() {
        whoIsFootPrint.setRegistrarRegistrationExpirationDate(REGISTRAR_REGISTRATION_EXPIRATION_DATE_2);
        assertNotEquals(REGISTRAR_REGISTRATION_EXPIRATION_DATE, whoIsFootPrint.getRegistrarRegistrationExpirationDate());
        assertEquals(REGISTRAR_REGISTRATION_EXPIRATION_DATE_2, whoIsFootPrint.getRegistrarRegistrationExpirationDate());
    }

    @Test
    void getRegistrar() {
        assertEquals(REGISTRAR_1, whoIsFootPrint.getRegistrar());
    }

    @Test
    void setRegistrar() {
        whoIsFootPrint.setRegistrar(REGISTRAR_2);
        assertNotEquals(REGISTRAR_1, whoIsFootPrint.getRegistrar());
        assertEquals(REGISTRAR_2, whoIsFootPrint.getRegistrar());
    }

    @Test
    void getRegistrarIanaId() {
        assertEquals(REGISTRAR_IANA_ID, whoIsFootPrint.getRegistrarIanaId());
    }

    @Test
    void setRegistrarIanaId() {
        whoIsFootPrint.setRegistrarIanaId(REGISTRAR_IANA_ID_2);
        assertNotEquals(REGISTRAR_IANA_ID, whoIsFootPrint.getRegistrarIanaId());
        assertEquals(REGISTRAR_IANA_ID_2, whoIsFootPrint.getRegistrarIanaId());
    }

    @Test
    void getRegistrarAbuseContactEmail() {
        assertEquals(REGISTRAR_ABUSE_CONTACT_EMAIL, whoIsFootPrint.getRegistrarAbuseContactEmail());
    }

    @Test
    void setRegistrarAbuseContactEmail() {
        whoIsFootPrint.setRegistrarAbuseContactEmail(REGISTRAR_ABUSE_CONTACT_EMAIL_2);
        assertNotEquals(REGISTRAR_ABUSE_CONTACT_EMAIL, whoIsFootPrint.getRegistrarAbuseContactEmail());
        assertEquals(REGISTRAR_ABUSE_CONTACT_EMAIL_2, whoIsFootPrint.getRegistrarAbuseContactEmail());
    }

    @Test
    void getRegistrarAbuseContactPhone() {
        assertEquals(REGISTRAR_ABUSE_CONTACT_PHONE, whoIsFootPrint.getRegistrarAbuseContactPhone());
    }

    @Test
    void setRegistrarAbuseContactPhone() {
        whoIsFootPrint.setRegistrarAbuseContactPhone(REGISTRAR_ABUSE_CONTACT_PHONE_2);
        assertNotEquals(REGISTRAR_ABUSE_CONTACT_PHONE, whoIsFootPrint.getRegistrarAbuseContactPhone());
        assertEquals(REGISTRAR_ABUSE_CONTACT_PHONE_2, whoIsFootPrint.getRegistrarAbuseContactPhone());
    }

    @Test
    void getDomainStatus() {
        assertEquals(DOMAIN_STATUS, whoIsFootPrint.getDomainStatus());
    }

    @Test
    void setDomainStatus() {
        whoIsFootPrint.setDomainStatus(DOMAIN_STATUS_2);
        assertNotEquals(DOMAIN_STATUS, whoIsFootPrint.getDomainStatus());
        assertEquals(DOMAIN_STATUS_2, whoIsFootPrint.getDomainStatus());
    }

    @Test
    void getNameServer() {
        assertEquals(NAME_SERVER, whoIsFootPrint.getNameServer());
    }

    @Test
    void setNameServer() {
        whoIsFootPrint.setNameServer(NAME_SERVER_2);
        assertNotEquals(NAME_SERVER, whoIsFootPrint.getNameServer());
        assertEquals(NAME_SERVER_2, whoIsFootPrint.getNameServer());
    }

    @Test
    void getDnssec() {
        assertEquals(DNSSEC, whoIsFootPrint.getDnssec());
    }

    @Test
    void setDnssec() {
        whoIsFootPrint.setDnssec(DNSSEC_2);
        assertNotEquals(DNSSEC, whoIsFootPrint.getDnssec());
        assertEquals(DNSSEC_2, whoIsFootPrint.getDnssec());
    }

    @Test
    void getUrlIcannComplaintForm() {
        assertEquals(URL_ICANN_COMPLAINT_FORM, whoIsFootPrint.getUrlIcannComplaintForm());
    }

    @Test
    void setUrlIcannComplaintForm() {
        whoIsFootPrint.setUrlIcannComplaintForm(URL_ICANN_COMPLAINT_FORM_2);
        assertNotEquals(URL_ICANN_COMPLAINT_FORM, whoIsFootPrint.getUrlIcannComplaintForm());
        assertEquals(URL_ICANN_COMPLAINT_FORM_2, whoIsFootPrint.getUrlIcannComplaintForm());
    }

    @Test
    void getLastUpdateOfWhoIsDatabase() {
        assertEquals(LAST_UPDATED_WHOIS_DATABASE, whoIsFootPrint.getLastUpdateOfWhoIsDatabase());
    }

    @Test
    void setLastUpdateOfWhoIsDatabase() {
        whoIsFootPrint.setLastUpdateOfWhoIsDatabase(LAST_UPDATED_WHOIS_DATABASE_2);
        assertNotEquals(LAST_UPDATED_WHOIS_DATABASE, whoIsFootPrint.getLastUpdateOfWhoIsDatabase());
        assertEquals(LAST_UPDATED_WHOIS_DATABASE_2, whoIsFootPrint.getLastUpdateOfWhoIsDatabase());
    }

    @Test
    void getRegistrantName() {
        assertNull(whoIsFootPrint.getRegistrantName());
    }

    @Test
    void setRegistrantName() {
        whoIsFootPrint.setRegistrantName(REGISTRANT_NAME);
        assertEquals(REGISTRANT_NAME, whoIsFootPrint.getRegistrantName());
    }

    @Test
    void getRegistrantOrganization() {
        assertEquals(REGISTRANT_ORGANIZATION_1, whoIsFootPrint.getRegistrantOrganization());
    }

    @Test
    void setRegistrantOrganization() {
        whoIsFootPrint.setRegistrantOrganization(REGISTRANT_ORGANIZATION_2);
        assertNotEquals(REGISTRANT_ORGANIZATION_1, whoIsFootPrint.getRegistrantOrganization());
        assertEquals(REGISTRANT_ORGANIZATION_2, whoIsFootPrint.getRegistrantOrganization());
    }

    @Test
    void getRegistrantStreet() {
        assertNull(whoIsFootPrint.getRegistrantStreet());
    }

    @Test
    void setRegistrantStreet() {
        whoIsFootPrint.setRegistrantStreet(REGISTRANT_STREET);
        assertEquals(REGISTRANT_STREET, whoIsFootPrint.getRegistrantStreet());
    }

    @Test
    void getRegistrantCity() {
        assertNull(whoIsFootPrint.getRegistrantCity());
    }

    @Test
    void setRegistrantCity() {
        whoIsFootPrint.setRegistrantCity(REGISTRANT_CITY);
        assertEquals(REGISTRANT_CITY, whoIsFootPrint.getRegistrantCity());
    }

    @Test
    void getRegistrantStateProvince() {
        assertEquals(REGISTRANT_STATE_PROVINCE, whoIsFootPrint.getRegistrantStateProvince());
    }

    @Test
    void setRegistrantStateProvince() {
        whoIsFootPrint.setRegistrantStateProvince(REGISTRANT_STATE_PROVINCE_2);
        assertNotEquals(REGISTRANT_STATE_PROVINCE, whoIsFootPrint.getRegistrantStateProvince());
        assertEquals(REGISTRANT_STATE_PROVINCE_2, whoIsFootPrint.getRegistrantStateProvince());
    }

    @Test
    void getRegistrantPostalCode() {
        assertNull(whoIsFootPrint.getRegistrantPostalCode());
    }

    @Test
    void setRegistrantPostalCode() {
        whoIsFootPrint.setRegistrantPostalCode(REGISTRANT_POSTAL_CODE);
        assertEquals(REGISTRANT_POSTAL_CODE, whoIsFootPrint.getRegistrantPostalCode());
    }

    @Test
    void getRegistrantCountry() {
        assertEquals(REGISTRANT_COUNTRY, whoIsFootPrint.getRegistrantCountry());
    }

    @Test
    void setRegistrantCountry() {
        whoIsFootPrint.setRegistrantCountry(REGISTRANT_COUNTRY_2);
        assertNotEquals(REGISTRANT_COUNTRY, whoIsFootPrint.getRegistrantCountry());
        assertEquals(REGISTRANT_COUNTRY_2, whoIsFootPrint.getRegistrantCountry());
    }

    @Test
    void getRegistrantPhone() {
        assertNull(whoIsFootPrint.getRegistrantPhone());
    }

    @Test
    void setRegistrantPhone() {
        whoIsFootPrint.setRegistrantPhone(REGISTRANT_PHONE);
        assertEquals(REGISTRANT_PHONE, whoIsFootPrint.getRegistrantPhone());
    }

    @Test
    void getRegistrantPhoneExt() {
        assertNull(whoIsFootPrint.getRegistrantPhoneExt());
    }

    @Test
    void setRegistrantPhoneExt() {
        whoIsFootPrint.setRegistrantPhoneExt(REGISTRANT_PHONE_EXT);
        assertEquals(REGISTRANT_PHONE_EXT, whoIsFootPrint.getRegistrantPhoneExt());
    }

    @Test
    void getRegistrantFax() {
        assertNull(whoIsFootPrint.getRegistrantFax());
    }

    @Test
    void setRegistrantFax() {
        whoIsFootPrint.setRegistrantFax(REGISTRANT_FAX);
        assertEquals(REGISTRANT_FAX, whoIsFootPrint.getRegistrantFax());
    }

    @Test
    void getRegistrantEmail() {
        assertEquals(REGISTRANT_EMAIL_1, whoIsFootPrint.getRegistrantEmail());
    }

    @Test
    void setRegistrantEmail() {
        whoIsFootPrint.setRegistrantEmail(REGISTRANT_EMAIL_2);
        assertNotEquals(REGISTRANT_EMAIL_1, whoIsFootPrint.getRegistrantEmail());
        assertEquals(REGISTRANT_EMAIL_2, whoIsFootPrint.getRegistrantEmail());
    }

    @Test
    void getAdminName() {
        assertNull(whoIsFootPrint.getAdminName());
    }

    @Test
    void setAdminName() {
        whoIsFootPrint.setAdminName(ADMIN_NAME);
        assertEquals(ADMIN_NAME, whoIsFootPrint.getAdminName());
    }

    @Test
    void getAdminOrganization() {
        assertEquals(ADMIN_ORGANIZATION, whoIsFootPrint.getAdminOrganization());
    }

    @Test
    void setAdminOrganization() {
        whoIsFootPrint.setAdminOrganization(ADMIN_ORGANIZATION_2);
        assertNotEquals(ADMIN_ORGANIZATION, whoIsFootPrint.getAdminOrganization());
        assertEquals(ADMIN_ORGANIZATION_2, whoIsFootPrint.getAdminOrganization());
    }

    @Test
    void getAdminStreet() {
        assertNull(whoIsFootPrint.getAdminStreet());
    }

    @Test
    void setAdminStreet() {
        whoIsFootPrint.setAdminStreet(ADMIN_STREET);
        assertEquals(ADMIN_STREET, whoIsFootPrint.getAdminStreet());
    }

    @Test
    void getAdminCity() {
        assertNull(whoIsFootPrint.getAdminCity());
    }

    @Test
    void setAdminCity() {
        whoIsFootPrint.setAdminCity(ADMIN_CITY);
        assertEquals(ADMIN_CITY, whoIsFootPrint.getAdminCity());
    }

    @Test
    void getAdminState() {
        assertNull(whoIsFootPrint.getAdminState());
    }

    @Test
    void setAdminState() {
        whoIsFootPrint.setAdminState(ADMIN_STATE);
        assertEquals(ADMIN_STATE, whoIsFootPrint.getAdminState());
    }

    @Test
    void getAdminPostalCode() {
        assertNull(whoIsFootPrint.getAdminPostalCode());
    }

    @Test
    void setAdminPostalCode() {
        whoIsFootPrint.setAdminPostalCode(ADMIN_POSTAL_CODE);
        assertEquals(ADMIN_POSTAL_CODE, whoIsFootPrint.getAdminPostalCode());
    }

    @Test
    void getAdminCountry() {
        assertEquals(ADMIN_COUNTRY, whoIsFootPrint.getAdminCountry());
    }

    @Test
    void setAdminCountry() {
        whoIsFootPrint.setAdminCountry(ADMIN_COUNTRY_2);
        assertNotEquals(ADMIN_COUNTRY, whoIsFootPrint.getAdminCountry());
        assertEquals(ADMIN_COUNTRY_2, whoIsFootPrint.getAdminCountry());
    }

    @Test
    void getAdminPhone() {
        assertNull(whoIsFootPrint.getAdminPhone());
    }

    @Test
    void setAdminPhone() {
        whoIsFootPrint.setAdminPhone(ADMIN_PHONE);
        assertEquals(ADMIN_PHONE, whoIsFootPrint.getAdminPhone());
    }

    @Test
    void getAdminPhoneExt() {
        assertNull(whoIsFootPrint.getAdminPhoneExt());
    }

    @Test
    void setAdminPhoneExt() {
        whoIsFootPrint.setAdminPhoneExt(ADMIN_PHONE_EXT);
        assertEquals(ADMIN_PHONE_EXT, whoIsFootPrint.getAdminPhoneExt());
    }

    @Test
    void getAdminFax() {
        assertNull(whoIsFootPrint.getAdminFax());
    }

    @Test
    void setAdminFax() {
        whoIsFootPrint.setAdminFax(ADMIN_FAX);
        assertEquals(ADMIN_FAX, whoIsFootPrint.getAdminFax());
    }

    @Test
    void getAdminEmail() {
        assertEquals(ADMIN_EMAIL, whoIsFootPrint.getAdminEmail());
    }

    @Test
    void setAdminEmail() {
        whoIsFootPrint.setAdminEmail(ADMIN_EMAIL_2);
        assertNotEquals(ADMIN_EMAIL, whoIsFootPrint.getAdminEmail());
        assertEquals(ADMIN_EMAIL_2, whoIsFootPrint.getAdminEmail());
    }

    @Test
    void getTechName() {
        assertNull(whoIsFootPrint.getTechName());
    }

    @Test
    void setTechName() {
        whoIsFootPrint.setTechName(TECH_NAME);
        assertEquals(TECH_NAME, whoIsFootPrint.getTechName());
    }

    @Test
    void getTechOrganization() {
        assertEquals(TECH_ORGANIZATION, whoIsFootPrint.getTechOrganization());
    }

    @Test
    void setTechOrganization() {
        whoIsFootPrint.setTechOrganization(TECH_ORGANIZATION_2);
        assertNotEquals(TECH_ORGANIZATION, whoIsFootPrint.getTechOrganization());
        assertEquals(TECH_ORGANIZATION_2, whoIsFootPrint.getTechOrganization());
    }

    @Test
    void getTechStreet() {
        assertNull(whoIsFootPrint.getTechStreet());
    }

    @Test
    void setTechStreet() {
        whoIsFootPrint.setTechStreet(TECH_STREET);
        assertEquals(TECH_STREET, whoIsFootPrint.getTechStreet());
    }

    @Test
    void getTechCity() {
        assertNull(whoIsFootPrint.getTechCity());
    }

    @Test
    void setTechCity() {
        whoIsFootPrint.setTechCity(TECH_CITY);
        assertEquals(TECH_CITY, whoIsFootPrint.getTechCity());
    }

    @Test
    void getTechStateProvince() {
        assertEquals(TECH_STATE_PROVINCE, whoIsFootPrint.getTechStateProvince());
    }

    @Test
    void setTechStateProvince() {
        whoIsFootPrint.setTechStateProvince(TECH_STATE_PROVINCE_2);
        assertNotEquals(TECH_STATE_PROVINCE, whoIsFootPrint.getTechStateProvince());
        assertEquals(TECH_STATE_PROVINCE_2, whoIsFootPrint.getTechStateProvince());
    }

    @Test
    void getTechPostalCode() {
        assertNull(whoIsFootPrint.getTechPostalCode());
    }

    @Test
    void setTechPostalCode() {
        whoIsFootPrint.setTechPostalCode(TECH_POSTAL_CODE);
        assertEquals(TECH_POSTAL_CODE, whoIsFootPrint.getTechPostalCode());
    }

    @Test
    void getTechCountry() {
        assertEquals(TECH_COUNTRY, whoIsFootPrint.getTechCountry());
    }

    @Test
    void setTechCountry() {
        whoIsFootPrint.setTechCountry(TECH_COUNTRY_2);
        assertNotEquals(TECH_COUNTRY, whoIsFootPrint.getTechCountry());
        assertEquals(TECH_COUNTRY_2, whoIsFootPrint.getTechCountry());
    }

    @Test
    void getTechPhone() {
        assertNull(whoIsFootPrint.getTechPhone());
    }

    @Test
    void setTechPhone() {
        whoIsFootPrint.setTechPhone(TECH_PHONE);
        assertEquals(TECH_PHONE, whoIsFootPrint.getTechPhone());
    }

    @Test
    void getTechPhoneExt() {
        assertNull(whoIsFootPrint.getTechPhoneExt());
    }

    @Test
    void setTechPhoneExt() {
        whoIsFootPrint.setTechPhoneExt(TECH_PHONE_EXT);
        assertEquals(TECH_PHONE_EXT, whoIsFootPrint.getTechPhoneExt());
    }

    @Test
    void getTechFax() {
        assertNull(whoIsFootPrint.getTechFax());
    }

    @Test
    void setTechFax() {
        whoIsFootPrint.setTechFax(TECH_FAX);
        assertEquals(TECH_FAX, whoIsFootPrint.getTechFax());
    }

    @Test
    void getTechEmail() {
        assertEquals(TECH_EMAIL, whoIsFootPrint.getTechEmail());
    }

    @Test
    void setTechEmail() {
        whoIsFootPrint.setTechEmail(TECH_EMAIL_2);
        assertNotEquals(TECH_EMAIL, whoIsFootPrint.getTechEmail());
        assertEquals(TECH_EMAIL_2, whoIsFootPrint.getTechEmail());
    }
}
