package com.morris.opensquare.repositories;

import com.morris.opensquare.models.notifications.GlobalNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalNotificationsRepository extends MongoRepository<GlobalNotification, String> {
}
