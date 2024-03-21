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
        List<GlobalNotification> sessionAllNotifications = (List<GlobalNotification>) httpSession.getAttribute("globalNotifications");
        if (sessionAllNotifications != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sessionAllNotifications);
        }
        List<GlobalNotification> allNotifications = notificationService.readAllGlobalNotifications();
        httpSession.setAttribute("globalNotifications", allNotifications);
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
}
