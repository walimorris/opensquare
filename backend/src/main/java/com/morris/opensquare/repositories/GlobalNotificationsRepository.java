package com.morris.opensquare.repositories;

import com.morris.opensquare.models.Notifications.GlobalNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalNotificationsRepository extends MongoRepository<GlobalNotification, String> {
}
