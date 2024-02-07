package com.morris.opensquare.models.digitalfootprints;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomainRequest {

    @JsonProperty("domain")
    private String domain;

    public DomainRequest(String domain) {
        this.domain = domain;
    }

    public DomainRequest() {}

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
