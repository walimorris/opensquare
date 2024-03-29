package com.morris.opensquare.models.notifications;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("user_notifications")
public class UserNotification extends GlobalNotification {

    private boolean seen;

    public UserNotification() {}

    public UserNotification(Builder builder) {
        this.seen = builder.seen;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }


    @Override
    public String toString() {
        return "UserNotification{" +
                super.toString() +
                "seen=" + seen +
                "} ";
    }

    public static class Builder {
        private boolean seen;

        public Builder() {
            // nothing to complete inside Builder
        }

        public Builder seen(boolean seen) {
            this.seen = seen;
            return this;
        }

        public UserNotification build() {
            return new UserNotification(this);
        }
    }
}
