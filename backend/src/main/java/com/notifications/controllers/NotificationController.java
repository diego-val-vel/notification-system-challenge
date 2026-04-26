package com.notifications.controllers;

import com.notifications.dtos.CreateMessageRequest;
import com.notifications.dtos.NotificationResponse;
import com.notifications.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for exposing notification operations.
 *
 * <p>This controller receives client requests, validates the payload through
 * Bean Validation, and delegates the notification orchestration process to the
 * service layer. It does not contain business logic, preserving separation of
 * concerns between routing and application behavior.</p>
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Creates a new notification controller.
     *
     * @param notificationService service responsible for notification orchestration
     */
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Creates a message and sends notifications to subscribed users.
     *
     * @param request validated notification creation request
     * @return notification processing result
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse createNotification(@Valid @RequestBody CreateMessageRequest request) {
        return notificationService.createAndSendNotification(request);
    }
}
