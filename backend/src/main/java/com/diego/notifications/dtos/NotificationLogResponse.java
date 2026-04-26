package com.diego.notifications.dtos;

import com.diego.notifications.models.NotificationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing a single notification delivery result.
 *
 * <p>This DTO exposes the relevant delivery information to the client without
 * leaking persistence implementation details from the entity layer.</p>
 *
 * @param id notification log identifier
 * @param channelCode delivery channel code
 * @param userId recipient user identifier
 * @param userName recipient user name
 * @param status final delivery status
 * @param attempts number of attempts performed
 * @param errorMessage error message when delivery fails
 * @param createdAt creation timestamp
 * @param updatedAt last update timestamp
 */
public record NotificationLogResponse(
        UUID id,
        String channelCode,
        String userId,
        String userName,
        NotificationStatus status,
        Integer attempts,
        String errorMessage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
