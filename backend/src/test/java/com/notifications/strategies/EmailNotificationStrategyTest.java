package com.notifications.strategies;

import com.notifications.config.NotificationFailureProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link EmailNotificationStrategy}.
 *
 * <p>These tests validate deterministic success and failure scenarios by using
 * boundary failure rates. A failure rate of {@code 0.0} guarantees delivery,
 * while a failure rate of {@code 1.0} guarantees provider failure.</p>
 */
class EmailNotificationStrategyTest {

    @Test
    @DisplayName("Should return EMAIL as supported channel code")
    void shouldReturnEmailAsSupportedChannelCode() {
        EmailNotificationStrategy strategy = new EmailNotificationStrategy(new NotificationFailureProperties());

        assertThatCode(() -> strategy.getChannelCode().equals("EMAIL")).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should send email successfully when failure rate is zero")
    void shouldSendEmailSuccessfullyWhenFailureRateIsZero() {
        NotificationFailureProperties properties = new NotificationFailureProperties();
        properties.setEmailRate(0.0);

        EmailNotificationStrategy strategy = new EmailNotificationStrategy(properties);

        assertThatCode(() -> strategy.send("user@example.com", "Test message"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw exception when email failure rate is one")
    void shouldThrowExceptionWhenEmailFailureRateIsOne() {
        NotificationFailureProperties properties = new NotificationFailureProperties();
        properties.setEmailRate(1.0);

        EmailNotificationStrategy strategy = new EmailNotificationStrategy(properties);

        assertThatThrownBy(() -> strategy.send("user@example.com", "Test message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Email provider failed to deliver the notification.");
    }
}
