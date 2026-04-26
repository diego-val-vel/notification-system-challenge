package com.notifications.dtos;

import com.notifications.models.NotificationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing a notification log record in history views.
 *
 * <p>This DTO exposes the persisted information required by the user interface
 * to display notification delivery history from newest to oldest, including
 * message, category, channel, user, status, attempts, timestamps, and errors.</p>
 *
 * @param id notification log identifier
 * @param messageId message identifier
 * @param categoryCode message category code
 * @param message notification message body
 * @param channelCode notification channel code
 * @param userId recipient user identifier
 * @param userName recipient user name
 * @param userEmail recipient email address
 * @param userPhone recipient phone number
 * @param status final delivery status
 * @param attempts number of delivery attempts
 * @param errorMessage error message when delivery fails
 * @param createdAt log creation timestamp
 * @param updatedAt log last update timestamp
 */
public record NotificationLogHistoryResponse(
        UUID id,
        UUID messageId,
        String categoryCode,
        String message,
        String channelCode,
        String userId,
        String userName,
        String userEmail,
        String userPhone,
        NotificationStatus status,
        Integer attempts,
        String errorMessage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
