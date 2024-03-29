package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.notifications.GlobalNotification;
import com.morris.opensquare.models.notifications.OwaspBlogReference;
import com.morris.opensquare.repositories.GlobalNotificationsRepository;
import com.morris.opensquare.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final String SENDER = "Opensquare Team";
    private static final long PLUS_DAYS = 5;

    private final GlobalNotificationsRepository globalNotificationsRepository;

    @Autowired
    public NotificationServiceImpl(GlobalNotificationsRepository globalNotificationsRepository) {
        this.globalNotificationsRepository = globalNotificationsRepository;
    }

    @Override
    public GlobalNotification broadcastNotification(String message) {
        GlobalNotification notification = buildSimpleBroadCastNotification(message);
        return globalNotificationsRepository.insert(notification);
    }

    @Override
    public List<GlobalNotification> readAllGlobalNotifications() {
        return globalNotificationsRepository.findAll();
    }

    @Override
    public GlobalNotification broadcastOwaspNotification(String message, OwaspBlogReference owaspBlogReference) {
        GlobalNotification notification = buildSimpleBroadCastNotification(message);
        notification.setOwaspRef(owaspBlogReference);
        return globalNotificationsRepository.insert(notification);
    }

    private GlobalNotification buildSimpleBroadCastNotification(String message) {
        LocalDateTime weekExpiration = LocalDateTime.now().plusDays(PLUS_DAYS);
        return new GlobalNotification.Builder()
                .expiration(weekExpiration.atOffset(ZoneOffset.UTC).toLocalDateTime())
                .message(message)
                .sender(SENDER)
                .build();
    }
}
