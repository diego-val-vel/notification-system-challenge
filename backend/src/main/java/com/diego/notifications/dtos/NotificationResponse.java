package com.diego.notifications.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO returned after processing a notification message.
 *
 * <p>The response includes the persisted message information and the delivery
 * results generated for each user and channel combination.</p>
 *
 * @param messageId created message identifier
 * @param categoryCode category code associated with the message
 * @param message message content that was processed
 * @param createdAt message creation timestamp
 * @param logs delivery results
 */
public record NotificationResponse(
        UUID messageId,
        String categoryCode,
        String message,
        LocalDateTime createdAt,
        List<NotificationLogResponse> logs
) {
}
