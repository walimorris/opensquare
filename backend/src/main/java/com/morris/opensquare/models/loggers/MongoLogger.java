package com.morris.opensquare.models.loggers;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("mongo_logger")
public class MongoLogger {
    @Id
    private ObjectId id;
    private String module;
    private String message;

    @Field("created_at")
    private String createdAt;

    public MongoLogger() {}

    public MongoLogger(ObjectId id, String module, String message, String createdAt) {
        this.id = id;
        this.module = module;
        this.message = message;
        this.createdAt = createdAt;
    }

    public MongoLogger(Builder builder) {
        this.id = builder.id;
        this.message = builder.message;
        this.module = builder.module;
        this.createdAt = builder.createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        @Id
        private ObjectId id;
        private String module;
        private String message;

        @Field("created_at")
        private String createdAt;

        public Builder() {}

        public Builder id(ObjectId id) {
            this.id = id;
            return this;
        }

        public Builder module(String module) {
            this.module = module;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public MongoLogger build() {
            return new MongoLogger(this);
        }
    }
}
