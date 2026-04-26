package com.notifications.strategies;

import com.notifications.config.NotificationFailureProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link SmsNotificationStrategy}.
 *
 * <p>These tests validate deterministic success and failure scenarios by using
 * boundary failure rates. This ensures retry-related behavior can be tested
 * without relying on random outcomes.</p>
 */
class SmsNotificationStrategyTest {

    @Test
    @DisplayName("Should return SMS as supported channel code")
    void shouldReturnSmsAsSupportedChannelCode() {
        SmsNotificationStrategy strategy = new SmsNotificationStrategy(new NotificationFailureProperties());

        assertThat(strategy.getChannelCode()).isEqualTo("SMS");
    }

    @Test
    @DisplayName("Should send SMS successfully when failure rate is zero")
    void shouldSendSmsSuccessfullyWhenFailureRateIsZero() {
        NotificationFailureProperties properties = new NotificationFailureProperties();
        properties.setSmsRate(0.0);

        SmsNotificationStrategy strategy = new SmsNotificationStrategy(properties);

        assertThatCode(() -> strategy.send("+5215550000001", "Test message"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw exception when SMS failure rate is one")
    void shouldThrowExceptionWhenSmsFailureRateIsOne() {
        NotificationFailureProperties properties = new NotificationFailureProperties();
        properties.setSmsRate(1.0);

        SmsNotificationStrategy strategy = new SmsNotificationStrategy(properties);

        assertThatThrownBy(() -> strategy.send("+5215550000001", "Test message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("SMS provider failed to deliver the notification.");
    }
}
