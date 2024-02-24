package com.morris.opensquare.models.Notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.morris.opensquare.utils.Constants.OPENSQUARE_JAVA_MONGODB_JSON_PARSE_TIME_PATTERN;
import static com.morris.opensquare.utils.Constants.OPENSQUARE_JAVA_MONGODB_TIME_PATTERN;

@Document("global_notifications")
public class GlobalNotification {

    @Id
    private ObjectId id;

    /**
     * In order to make a TTL field on MDB collection that deletes a Document based on the given date, you
     * must create an index such as <b>db.collection.createIndex({ "expiration": 1}, {expireAfterSeconds: 0})</b>
     * on the expiring Date field in the collection. This, effectively, creates a TTL field on that collection.
     * <br><br>
     * @see <a href="https://www.mongodb.com/docs/manual/tutorial/expire-data/#std-label-expire-data-atlas-ui">TTL DOCS</a>
     */
    @JsonFormat(pattern = OPENSQUARE_JAVA_MONGODB_TIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expiration;
    private String message;
    private String sender;
    private OwaspBlogReference owaspRef;

    public GlobalNotification() {}

    public GlobalNotification(ObjectId id, LocalDateTime expiration, String message, String sender, OwaspBlogReference owaspRef) {
        this.id = id;
        this.expiration = expiration;
        this.message = message;
        this.sender = sender;
        this.owaspRef = owaspRef;
    }

    public GlobalNotification(Builder builder) {
        this.id = builder.id;
        this.expiration = builder.expiration;
        this.message = builder.message;
        this.sender = builder.sender;
        this.owaspRef = builder.owaspRef;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @JsonFormat(pattern = OPENSQUARE_JAVA_MONGODB_JSON_PARSE_TIME_PATTERN, shape = JsonFormat.Shape.STRING)
    public LocalDateTime getExpiration() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OPENSQUARE_JAVA_MONGODB_JSON_PARSE_TIME_PATTERN);
        System.out.println("parsing: " + this.expiration.toString());
        return LocalDateTime.parse(this.expiration.toString().split("\\.")[0], dateTimeFormatter);
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public OwaspBlogReference getOwaspRef() {
        return owaspRef;
    }

    public void setOwaspRef(OwaspBlogReference owaspRef) {
        this.owaspRef = owaspRef;
    }

    @Override
    public String toString() {
        String owaspString;
        if (owaspRef != null) {
            owaspString = owaspRef.toString();
        } else {
            owaspString = "";
        }
        return "GlobalNotification{" +
                "id=" + id +
                ", expiration=" + expiration +
                ", message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", owaspRef='" + owaspString + '\'' +
                '}';
    }

    public static class Builder {
        private ObjectId id;
        private LocalDateTime expiration;
        private String message;
        private String sender;
        private OwaspBlogReference owaspRef;

        public Builder() {}

        public Builder id(ObjectId id) {
            this.id = id;
            return this;
        }

        public Builder expiration(LocalDateTime expiration) {
            this.expiration = expiration;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder owaspRef(OwaspBlogReference owaspRef) {
            this.owaspRef = owaspRef;
            return this;
        }

        public GlobalNotification build() {
            return new GlobalNotification(this);
        }
    }
}
