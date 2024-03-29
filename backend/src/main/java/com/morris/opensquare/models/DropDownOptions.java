package com.morris.opensquare.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("drop_down_options")
public class DropDownOptions {
    private List<String> ages;
    private List<String> organizations;
    private List<String> professions;

    public DropDownOptions() {}

    public DropDownOptions(Builder builder) {
        this.ages = builder.ages;
        this.organizations = builder.organizations;
        this.professions = builder.professions;
    }

    public List<String> getAges() {
        return ages;
    }

    public void setAges(List<String> ages) {
        this.ages = ages;
    }

    public List<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }

    public List<String> getProfessions() {
        return professions;
    }

    public void setProfessions(List<String> professions) {
        this.professions = professions;
    }

    public static class Builder {
        private List<String> ages;
        private List<String> organizations;
        private List<String> professions;

        public Builder() {
            // nothing to complete inside Builder
        }

        public Builder ages(List<String> ages) {
            this.ages = ages;
            return this;
        }

        public Builder organizations(List<String> organizations) {
            this.organizations = organizations;
            return this;
        }

        public Builder professions(List<String> professions) {
            this.professions = professions;
            return this;
        }

        public DropDownOptions build() {
            return new DropDownOptions(this);
        }
    }
}
