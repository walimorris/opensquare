package com.morris.opensquare.models.digitalfootprints;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NSLookupFootPrint {

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("{name:%s,address:%s}", name, address);
    }
}
