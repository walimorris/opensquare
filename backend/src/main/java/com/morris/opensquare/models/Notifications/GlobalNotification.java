package com.morris.opensquare.models.Notifications;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("global_notifications")
public class GlobalNotification {

    @Id
    private ObjectId id;

    /**
     * In order to make a TTL field on a MDB collection that deletes a Document based on the given date, you
     * must create an index such as <b>db.collection.createIndex({ "expiration": 1}, {expireAfterSeconds: 0})</b>
     * on the expiring Date field in the collection. This, effectively, creates a TTL field on that collection.
     * <br><br>
     * @see <a href="https://www.mongodb.com/docs/manual/tutorial/expire-data/#std-label-expire-data-atlas-ui">TTL DOCS</a>
     */
    private Date expiration;
    private String message;

    private String sender;

    public GlobalNotification() {}

    public GlobalNotification(Builder builder) {
        this.id = builder.id;
        this.expiration = builder.expiration;
        this.message = builder.message;
        this.sender = builder.sender;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
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

    @Override
    public String toString() {
        return "GlobalNotification{" +
                "id=" + id +
                ", expiration=" + expiration +
                ", message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }

    public static class Builder {
        private ObjectId id;
        private Date expiration;
        private String message;
        private String sender;

        public Builder() {}

        public Builder id(ObjectId id) {
            this.id = id;
            return this;
        }

        public Builder expiration(Date expiration) {
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

        public GlobalNotification build() {
            return new GlobalNotification(this);
        }
    }
}
