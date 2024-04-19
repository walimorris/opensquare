package com.morris.opensquare.controllers;

import com.morris.opensquare.TestHelper;
import com.morris.opensquare.models.notifications.GlobalNotification;
import com.morris.opensquare.services.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.morris.opensquare.controllers.NotificationController.GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS;
import static com.morris.opensquare.controllers.NotificationController.REEVALUATION_IN_MINUTES;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @MockBean
    NotificationService notificationService;

    @MockBean
    MockHttpSession mockHttpSession;

    @Autowired
    MockMvc mockModelViewController;

    public static final String GLOBAL_NOTIFICATIONS_LIST_FILE = "backend/src/test/resources/globalNotifications.json";
    public static final String SENDER = "Opensquare Team";
    public static final String MESSAGE_PARAM = "message";
    private static final String GLOBAL_NOTIFICATIONS = "global-notifications";
    public static final String GLOBAL_NOTIFICATION_1 = "This is a test, 123...";
    public static final String POST_GLOBAL_NOTIFICATION_BROADCAST_REQUEST = "/opensquare/admin/api/notifications";
    public static final String GET_ALL_GLOBAL_NOTIFICATIONS_REQUEST = "/opensquare/admin/api/notifications/globalAll";

    @Test
    void shouldConductSessionContentEvaluationFalse() {
        HttpSession httpSession = new MockHttpSession();

        // add a time 5 minutes ago - 300,000 ms
        Long past = System.currentTimeMillis() - 300000;
        httpSession.setAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS, past);

        boolean evaluation = shouldConductSessionContentEvaluation(httpSession);
        Assertions.assertFalse(evaluation);
    }

    @Test
    void shouldConductSessionContentEvaluationTrue() {
        HttpSession httpSession = new MockHttpSession();

        // add a time 16 minutes ago - 960,000 ms
        Long past = System.currentTimeMillis() - 960000;
        httpSession.setAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS, past);

        boolean evaluation = shouldConductSessionContentEvaluation(httpSession);
        Assertions.assertTrue(evaluation);
    }

    @Test
    void shouldConductSessionContentEvaluationNull() {
        HttpSession httpSession = new MockHttpSession();
        boolean evaluation = shouldConductSessionContentEvaluation(httpSession);
        Assertions.assertFalse(evaluation);
    }


    @Test
    void postGlobalNotificationBroadcast() throws Exception {
        GlobalNotification notification = new GlobalNotification.Builder()
                .id(new ObjectId())
                .expiration(LocalDateTime.now())
                .message(GLOBAL_NOTIFICATION_1)
                .sender(SENDER)
                .owaspRef(null)
                .build();
        String notificationAsString = TestHelper.writeValueAsString(notification);

        when(notificationService.broadcastNotification(GLOBAL_NOTIFICATION_1)).thenReturn(notification);
        this.mockModelViewController.perform(post(POST_GLOBAL_NOTIFICATION_BROADCAST_REQUEST).param(MESSAGE_PARAM, GLOBAL_NOTIFICATION_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(notificationAsString));
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllGlobalNotificationsWithEmptySession() throws Exception {
        List<GlobalNotification> notificationsList = (List<GlobalNotification>) TestHelper.convertModelFromFile(GLOBAL_NOTIFICATIONS_LIST_FILE, List.class, GlobalNotification.class);
        String notificationsListAsString = TestHelper.writeValueAsString(notificationsList);
        System.out.println(notificationsListAsString);

        when(mockHttpSession.getAttribute(GLOBAL_NOTIFICATIONS)).thenReturn(null);
        when(notificationService.readAllGlobalNotifications()).thenReturn(notificationsList);

        this.mockModelViewController.perform(get(GET_ALL_GLOBAL_NOTIFICATIONS_REQUEST))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(notificationsListAsString));
    }

    private boolean shouldConductSessionContentEvaluation(HttpSession session) {
        if (session.getAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS) != null) {
            long insertTime = (long) session.getAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS);
            long runningTime = System.currentTimeMillis() - insertTime;
            long runningTimeInMinutes = (runningTime / 1000) / 60;
            System.out.println("Global Notifications last evaluated minutes ago: " + runningTimeInMinutes);

            if (runningTimeInMinutes >= REEVALUATION_IN_MINUTES) {
                session.removeAttribute(GLOBAL_NOTIFICATION_INSERT_TIME_MILLIS);
                return true;
            }
        }
        return false;
    }
}