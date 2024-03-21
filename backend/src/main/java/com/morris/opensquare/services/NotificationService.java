package com.morris.opensquare.services;

import com.morris.opensquare.models.Notifications.GlobalNotification;
import com.morris.opensquare.models.Notifications.OwaspBlogReference;

import java.util.List;

public interface NotificationService {

    /**
     * Broadcast a global notification message across the Opensquare platform to all users.
     *
     * @param message {@link String} global message
     *
     * @return {@link GlobalNotification}
     */
    GlobalNotification broadcastNotification(String message);

    /**
     * Reads all current global notifications.
     *
     * @return {@link List<GlobalNotification>}
     */
    List<GlobalNotification> readAllGlobalNotifications();

    /**
     * Broadcast a global notification message that contains owasp reference acroos the
     * Opensquare platform to all users.
     *
     * @param message {@link String} global owasp message
     * @param owaspBlogReference {@link OwaspBlogReference}
     *
     * @return {@link GlobalNotification}
     */
    GlobalNotification broadcastOwaspNotification(String message, OwaspBlogReference owaspBlogReference);
}
