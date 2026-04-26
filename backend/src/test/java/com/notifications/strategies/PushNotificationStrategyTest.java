package com.notifications.strategies;

import com.notifications.config.NotificationFailureProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link PushNotificationStrategy}.
 *
 * <p>These tests validate deterministic success and failure scenarios by using
 * boundary failure rates. The tests avoid randomness by forcing the provider
 * simulation to always succeed or always fail.</p>
 */
class PushNotificationStrategyTest {

    @Test
    @DisplayName("Should return PUSH as supported channel code")
    void shouldReturnPushAsSupportedChannelCode() {
        PushNotificationStrategy strategy = new PushNotificationStrategy(new NotificationFailureProperties());

        assertThat(strategy.getChannelCode()).isEqualTo("PUSH");
    }

    @Test
    @DisplayName("Should send push notification successfully when failure rate is zero")
    void shouldSendPushSuccessfullyWhenFailureRateIsZero() {
        NotificationFailureProperties properties = new NotificationFailureProperties();
        properties.setPushRate(0.0);

        PushNotificationStrategy strategy = new PushNotificationStrategy(properties);

        assertThatCode(() -> strategy.send("device-token", "Test message"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw exception when push failure rate is one")
    void shouldThrowExceptionWhenPushFailureRateIsOne() {
        NotificationFailureProperties properties = new NotificationFailureProperties();
        properties.setPushRate(1.0);

        PushNotificationStrategy strategy = new PushNotificationStrategy(properties);

        assertThatThrownBy(() -> strategy.send("device-token", "Test message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Push provider failed to deliver the notification.");
    }
}
