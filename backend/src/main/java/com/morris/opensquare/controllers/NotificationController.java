package com.morris.opensquare.controllers;

import com.morris.opensquare.models.Notifications.GlobalNotification;
import com.morris.opensquare.models.Notifications.OwaspBlogReference;
import com.morris.opensquare.services.NotificationService;
import com.morris.opensquare.services.loggers.LoggerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/opensquare")
public class NotificationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final LoggerService loggerService;
    private final NotificationService notificationService;

    private final static long REEVALUATION_IN_MINUTES = 15;
    private final static String GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS = "global-notification-insert-time-millis";
    private final static String GLOBAL_NOTIFICATIONS = "global-notifications";

    @Autowired
    public NotificationController(LoggerService loggerService, NotificationService notificationService) {
        this.loggerService = loggerService;
        this.notificationService = notificationService;
    }

    @PostMapping("/admin/api/notifications")
    public ResponseEntity<GlobalNotification> postGlobalNotificationBroadCast(@RequestParam String message) {
        GlobalNotification globalNotification = notificationService.broadcastNotification(message);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(globalNotification);
    }

    @GetMapping("/admin/api/notifications/globalAll")
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<GlobalNotification>> getAllGlobalNotifications(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        List<GlobalNotification> sessionAllNotifications = (List<GlobalNotification>) httpSession.getAttribute(GLOBAL_NOTIFICATIONS);

        if (sessionAllNotifications != null) {
            if (!shouldConductSessionContentEvaluation(httpSession)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(sessionAllNotifications);
            }
        }
        LOGGER.info("Reevaluating session content for notifications");
        List<GlobalNotification> allNotifications = notificationService.readAllGlobalNotifications();
        httpSession.setAttribute(GLOBAL_NOTIFICATIONS, allNotifications);
        httpSession.setAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS, System.currentTimeMillis());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allNotifications);
    }

    @PostMapping("/admin/api/notifications/owasp")
    public ResponseEntity<GlobalNotification> postOwaspNotificationBroadcast(@RequestParam String message, @RequestParam OwaspBlogReference owaspRef) {
        GlobalNotification globalNotification = notificationService.broadcastOwaspNotification(message, owaspRef);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(globalNotification);
    }

    /**
     * Session data is stored for the duration of a session. There are cases where UI
     * calls an API and returns the current session data. That data may have been
     * updated in the persistence layer. In order to respond with the most up-to-date
     * data, while not stressing the data-layer, this method determines if the session
     * store should release and reevaluate session attributes for most the up-to-date
     * information. This reevaluation time can vary, based on the REEVALUATE class
     * constant.
     *
     * @param session {@link HttpSession}
     *
     * @return boolean
     */
    private boolean shouldConductSessionContentEvaluation(HttpSession session) {
        if (session.getAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS) != null) {
            long insertTime = (long) session.getAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS);
            long runningTime = System.currentTimeMillis() - insertTime;
            long runningTimeInMinutes = (runningTime / 1000) / 60;
            LOGGER.info("Global Notifications last evaluated '{}' minutes ago", runningTimeInMinutes);

            if (runningTimeInMinutes >= REEVALUATION_IN_MINUTES) {
                session.removeAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS);
                return true;
            }
        }
        return false;
    }
}
