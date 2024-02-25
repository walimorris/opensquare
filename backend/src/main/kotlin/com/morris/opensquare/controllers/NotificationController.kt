package com.morris.opensquare.controllers

import com.morris.opensquare.models.Notifications.GlobalNotification
import com.morris.opensquare.models.Notifications.OwaspBlogReference
import com.morris.opensquare.services.NotificationService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/admin/api/notifications/globalAll")
    fun getAllGlobalNotifications(httpServletRequest: HttpServletRequest): ResponseEntity<out Any> {
        val httpSession: HttpSession = httpServletRequest.session
        val sessionAllNotifications: Any? = httpSession.getAttribute("globalNotifications")
        if (sessionAllNotifications != null) {
            println("reusing session data: ")
            println(sessionAllNotifications.toString())
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(sessionAllNotifications)
        }
        println("all notifications not in session")
        val allNotifications = notificationService.readAllGlobalNotifications()
        httpSession.setAttribute("globalNotifications", allNotifications)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(allNotifications)
    }

    @PostMapping("/admin/api/notifications/owasp")
    fun postOwaspNotificationBroadcast(@RequestParam message: String,
                                       @RequestParam owaspRef: OwaspBlogReference): ResponseEntity<GlobalNotification> {

        println("owaspRef = $owaspRef")
        val globalNotification = notificationService.broadcastOwaspNotification(message, owaspRef)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(globalNotification)
    }
}