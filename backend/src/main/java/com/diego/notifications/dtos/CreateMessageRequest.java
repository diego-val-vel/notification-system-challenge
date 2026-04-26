package com.diego.notifications.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO used to create and send a notification message.
 *
 * <p>The request contains the selected category and the message content entered
 * by the client. Bean Validation annotations ensure that invalid input is
 * rejected before reaching the business layer.</p>
 *
 * @param categoryCode category code associated with the message
 * @param message message content to be delivered
 */
public record CreateMessageRequest(
        @NotBlank(message = "Category code is required.")
        @Size(max = 50, message = "Category code must not exceed 50 characters.")
        String categoryCode,

        @NotBlank(message = "Message is required.")
        @Size(max = 1000, message = "Message must not exceed 1000 characters.")
        String message
) {
}
