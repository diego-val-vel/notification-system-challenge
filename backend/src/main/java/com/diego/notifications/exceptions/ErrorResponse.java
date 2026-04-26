package com.diego.notifications.exceptions;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing a standardized API error response.
 *
 * <p>This structure provides consistent error payloads across the API, making
 * validation errors, not found errors, and unexpected failures easier to
 * understand from the frontend or external clients.</p>
 *
 * @param timestamp date and time when the error occurred
 * @param status HTTP status code
 * @param error HTTP error reason
 * @param message human-readable error message
 * @param details additional error details
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> details
) {
}
