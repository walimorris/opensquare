package com.morris.opensquare.services

import com.morris.opensquare.models.Notifications.GlobalNotification
import com.morris.opensquare.models.Notifications.OwaspBlogReference
import com.morris.opensquare.repositories.GlobalNotificationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

@Service
class NotificationService @Autowired constructor(private val globalNotificationsRepository: GlobalNotificationsRepository) {

    companion object {
        private const val SENDER = "Opensquare Team"
        private const val PLUS_DAYS: Long = 5
        private const val OFFSET = "Z"
    }

    fun broadcastNotification(message: String): GlobalNotification {
        val notification = buildSimpleBroadCastNotification(message)
        return globalNotificationsRepository.insert(notification)
    }

    fun broadcastOwaspNotification(message: String, owaspReference: OwaspBlogReference): GlobalNotification {
        val notification = buildSimpleBroadCastNotification(message)
        notification.owaspRef = owaspReference
        return globalNotificationsRepository.insert(notification)
    }

    private fun buildSimpleBroadCastNotification(message:String): GlobalNotification {
        val weekExpiration = Date.from(LocalDate
            .now()
            .plusDays(PLUS_DAYS)
            .atStartOfDay()
            .toInstant(ZoneOffset.of(OFFSET)))

        return GlobalNotification.Builder()
            .expiration(weekExpiration)
            .message(message)
            .sender(SENDER)
            .build()
    }
}