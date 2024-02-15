package com.morris.opensquare.controllers

import com.morris.opensquare.models.Notifications.GlobalNotification
import com.morris.opensquare.services.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/opensquare")
class NotificationController @Autowired constructor(private val notificationService: NotificationService) {

    @PostMapping("/admin/api/notifications")
    fun postGlobalNotificationBroadCast(@RequestParam message: String): ResponseEntity<GlobalNotification> {
        println("message = $message")
        val globalNotification = notificationService.broadcastNotification(message)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(globalNotification)
    }
}