package com.morris.opensquare.models.validations;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("disposable_email_domains")
public class DisposableEmailDomain {

    @Id
    private ObjectId id;

    @Field("domain_name")
    private String domainName;

    public DisposableEmailDomain() {}

    public DisposableEmailDomain(Builder builder) {
        this.id = builder.id;
        this.domainName = builder.domainName;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Override
    public String toString() {
        return "DisposableEmailDomains{" +
                "id=" + id +
                ", domainName='" + domainName + '\'' +
                '}';
    }

    public static class Builder {
        private ObjectId id;

        @Field("domain_name")
        private String domainName;

        public Builder() {}

        public Builder id(ObjectId id) {
            this.id = id;
            return this;
        }

        public Builder domainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        public DisposableEmailDomain build() {
            return new DisposableEmailDomain(this);
        }
    }
}
