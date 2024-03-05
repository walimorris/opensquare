package com.morris.opensquare.models.digitalfootprints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WhoIsFootPrint implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("domainName")
    private String domainName;

    @JsonProperty("registryDomainId")
    private String registryDomainId;

    @JsonProperty("registrarWhoisServer")
    private String registrarWhoisServer;

    @JsonProperty("registrarUrl")
    private String registrarUrl;

    @JsonProperty("updatedDate")
    private String updatedDate;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("registrarRegistrationExpirationDate")
    private String registrarRegistrationExpirationDate;

    @JsonProperty("registrar")
    private String registrar;

    @JsonProperty("registrarIanaId")
    private String registrarIanaId;

    @JsonProperty("registrarAbuseContactEmail")
    private String registrarAbuseContactEmail;

    @JsonProperty("registrarAbuseContactPhone")
    private String registrarAbuseContactPhone;

    @JsonProperty("domainStatus")
    private String domainStatus;

    @JsonProperty("nameServer")
    private String nameServer;

    @JsonProperty("dnssec")
    private String dnssec;

    @JsonProperty("urlIcannComplaintForm")
    private String urlIcannComplaintForm;

    @JsonProperty("lastUpdateOfWhoIsDatabase")
    private String lastUpdateOfWhoIsDatabase;

    @JsonProperty("registrantName")
    private String registrantName;

    @JsonProperty("registrantOrganization")
    private String registrantOrganization;

    @JsonProperty("registrantStreet")
    private String registrantStreet;

    @JsonProperty("registrantCity")
    private String registrantCity;

    @JsonProperty("registrantStateProvince")
    private String registrantStateProvince;

    @JsonProperty("registrantPostalCode")
    private String registrantPostalCode;

    @JsonProperty("registrantCountry")
    private String registrantCountry;

    @JsonProperty("registrantPhone")
    private String registrantPhone;

    @JsonProperty("registrantPhoneExt")
    private String registrantPhoneExt;

    @JsonProperty("registrantFax")
    private String registrantFax;

    @JsonProperty("registrantEmail")
    private String registrantEmail;

    @JsonProperty("adminName")
    private String adminName;

    @JsonProperty("adminOrganization")
    private String adminOrganization;

    @JsonProperty("adminStreet")
    private String adminStreet;

    @JsonProperty("adminCity")
    private String adminCity;

    @JsonProperty("adminState")
    private String adminState;

    @JsonProperty("adminPostalCode")
    private String adminPostalCode;

    @JsonProperty("adminCountry")
    private String adminCountry;

    @JsonProperty("adminPhone")
    private String adminPhone;

    @JsonProperty("adminPhoneExt")
    private String adminPhoneExt;

    @JsonProperty("adminFax")
    private String adminFax;

    @JsonProperty("adminEmail")
    private String adminEmail;

    @JsonProperty("techName")
    private String techName;

    @JsonProperty("techOrganization")
    private String techOrganization;

    @JsonProperty("techStreet")
    private String techStreet;

    @JsonProperty("techCity")
    private String techCity;

    @JsonProperty("techStateProvince")
    private String techStateProvince;

    @JsonProperty("techPostalCode")
    private String techPostalCode;

    @JsonProperty("techCountry")
    private String techCountry;

    @JsonProperty("techPhone")
    private String techPhone;

    @JsonProperty("techPhoneExt")
    private String techPhoneExt;

    @JsonProperty("techFax")
    private String techFax;

    @JsonProperty("techEmail")
    private String techEmail;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getRegistryDomainId() {
        return registryDomainId;
    }

    public void setRegistryDomainId(String registryDomainId) {
        this.registryDomainId = registryDomainId;
    }

    public String getRegistrarWhoisServer() {
        return registrarWhoisServer;
    }

    public void setRegistrarWhoIsServer(String registrarWhoisServer) {
        this.registrarWhoisServer = registrarWhoisServer;
    }

    public String getRegistrarUrl() {
        return registrarUrl;
    }

    public void setRegistrarUrl(String registrarUrl) {
        this.registrarUrl = registrarUrl;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRegistrarRegistrationExpirationDate() {
        return registrarRegistrationExpirationDate;
    }

    public void setRegistrarRegistrationExpirationDate(String registrarRegistrationExpirationDate) {
        this.registrarRegistrationExpirationDate = registrarRegistrationExpirationDate;
    }

    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public String getRegistrarIanaId() {
        return registrarIanaId;
    }

    public void setRegistrarIanaId(String registrarIanaId) {
        this.registrarIanaId = registrarIanaId;
    }

    public String getRegistrarAbuseContactEmail() {
        return registrarAbuseContactEmail;
    }

    public void setRegistrarAbuseContactEmail(String registrarAbuseContactEmail) {
        this.registrarAbuseContactEmail = registrarAbuseContactEmail;
    }

    public String getRegistrarAbuseContactPhone() {
        return registrarAbuseContactPhone;
    }

    public void setRegistrarAbuseContactPhone(String registrarAbuseContactPhone) {
        this.registrarAbuseContactPhone = registrarAbuseContactPhone;
    }

    public String getDomainStatus() {
        return domainStatus;
    }

    public void setDomainStatus(String domainStatus) {
        this.domainStatus = domainStatus;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getDnssec() {
        return dnssec;
    }

    public void setDnssec(String dnssec) {
        this.dnssec = dnssec;
    }

    public String getUrlIcannComplaintForm() {
        return urlIcannComplaintForm;
    }

    public void setUrlIcannComplaintForm(String urlIcannComplaintForm) {
        this.urlIcannComplaintForm = urlIcannComplaintForm;
    }

    public String getLastUpdateOfWhoIsDatabase() {
        return lastUpdateOfWhoIsDatabase;
    }

    public void setLastUpdateOfWhoIsDatabase(String lastUpdateOfWhoIsDatabase) {
        this.lastUpdateOfWhoIsDatabase = lastUpdateOfWhoIsDatabase;
    }

    public String getRegistrantName() {
        return registrantName;
    }

    public void setRegistrantName(String registrantName) {
        this.registrantName = registrantName;
    }

    public String getRegistrantOrganization() {
        return registrantOrganization;
    }

    public void setRegistrantOrganization(String registrantOrganization) {
        this.registrantOrganization = registrantOrganization;
    }

    public String getRegistrantStreet() {
        return registrantStreet;
    }

    public void setRegistrantStreet(String registrantStreet) {
        this.registrantStreet = registrantStreet;
    }

    public String getRegistrantCity() {
        return registrantCity;
    }

    public void setRegistrantCity(String registrantCity) {
        this.registrantCity = registrantCity;
    }

    public String getRegistrantStateProvince() {
        return registrantStateProvince;
    }

    public void setRegistrantStateProvince(String registrantStateProvince) {
        this.registrantStateProvince = registrantStateProvince;
    }

    public String getRegistrantPostalCode() {
        return registrantPostalCode;
    }

    public void setRegistrantPostalCode(String registrantPostalCode) {
        this.registrantPostalCode = registrantPostalCode;
    }

    public String getRegistrantCountry() {
        return registrantCountry;
    }

    public void setRegistrantCountry(String registrantCountry) {
        this.registrantCountry = registrantCountry;
    }

    public String getRegistrantPhone() {
        return registrantPhone;
    }

    public void setRegistrantPhone(String registrantPhone) {
        this.registrantPhone = registrantPhone;
    }

    public String getRegistrantPhoneExt() {
        return registrantPhoneExt;
    }

    public void setRegistrantPhoneExt(String registrantPhoneExt) {
        this.registrantPhoneExt = registrantPhoneExt;
    }

    public String getRegistrantFax() {
        return registrantFax;
    }

    public void setRegistrantFax(String registrantFax) {
        this.registrantFax = registrantFax;
    }

    public String getRegistrantEmail() {
        return registrantEmail;
    }

    public void setRegistrantEmail(String registrantEmail) {
        this.registrantEmail = registrantEmail;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminOrganization() {
        return adminOrganization;
    }

    public void setAdminOrganization(String adminOrganization) {
        this.adminOrganization = adminOrganization;
    }

    public String getAdminStreet() {
        return adminStreet;
    }

    public void setAdminStreet(String adminStreet) {
        this.adminStreet = adminStreet;
    }

    public String getAdminCity() {
        return adminCity;
    }

    public void setAdminCity(String adminCity) {
        this.adminCity = adminCity;
    }

    public String getAdminState() {
        return adminState;
    }

    public void setAdminState(String adminState) {
        this.adminState = adminState;
    }

    public String getAdminPostalCode() {
        return adminPostalCode;
    }

    public void setAdminPostalCode(String adminPostalCode) {
        this.adminPostalCode = adminPostalCode;
    }

    public String getAdminCountry() {
        return adminCountry;
    }

    public void setAdminCountry(String adminCountry) {
        this.adminCountry = adminCountry;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getAdminPhoneExt() {
        return adminPhoneExt;
    }

    public void setAdminPhoneExt(String adminPhoneExt) {
        this.adminPhoneExt = adminPhoneExt;
    }

    public String getAdminFax() {
        return adminFax;
    }

    public void setAdminFax(String adminFax) {
        this.adminFax = adminFax;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getTechOrganization() {
        return techOrganization;
    }

    public void setTechOrganization(String techOrganization) {
        this.techOrganization = techOrganization;
    }

    public String getTechStreet() {
        return techStreet;
    }

    public void setTechStreet(String techStreet) {
        this.techStreet = techStreet;
    }

    public String getTechCity() {
        return techCity;
    }

    public void setTechCity(String techCity) {
        this.techCity = techCity;
    }

    public String getTechStateProvince() {
        return techStateProvince;
    }

    public void setTechStateProvince(String techStateProvince) {
        this.techStateProvince = techStateProvince;
    }

    public String getTechPostalCode() {
        return techPostalCode;
    }

    public void setTechPostalCode(String techPostalCode) {
        this.techPostalCode = techPostalCode;
    }

    public String getTechCountry() {
        return techCountry;
    }

    public void setTechCountry(String techCountry) {
        this.techCountry = techCountry;
    }

    public String getTechPhone() {
        return techPhone;
    }

    public void setTechPhone(String techPhone) {
        this.techPhone = techPhone;
    }

    public String getTechPhoneExt() {
        return techPhoneExt;
    }

    public void setTechPhoneExt(String techPhoneExt) {
        this.techPhoneExt = techPhoneExt;
    }

    public String getTechFax() {
        return techFax;
    }

    public void setTechFax(String techFax) {
        this.techFax = techFax;
    }

    public String getTechEmail() {
        return techEmail;
    }

    public void setTechEmail(String techEmail) {
        this.techEmail = techEmail;
    }
}
