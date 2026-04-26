package com.notifications.strategies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link NotificationStrategyResolver}.
 *
 * <p>These tests validate that the resolver selects the correct notification
 * strategy by channel code and fails clearly when an unsupported channel is
 * requested.</p>
 */
class NotificationStrategyResolverTest {

    @Test
    @DisplayName("Should resolve strategy by channel code")
    void shouldResolveStrategyByChannelCode() {
        NotificationStrategy emailStrategy = new TestNotificationStrategy("EMAIL");
        NotificationStrategy smsStrategy = new TestNotificationStrategy("SMS");
        NotificationStrategyResolver resolver = new NotificationStrategyResolver(List.of(emailStrategy, smsStrategy));

        NotificationStrategy resolvedStrategy = resolver.resolve("EMAIL");

        assertThat(resolvedStrategy).isSameAs(emailStrategy);
    }

    @Test
    @DisplayName("Should throw exception when channel code is not supported")
    void shouldThrowExceptionWhenChannelCodeIsNotSupported() {
        NotificationStrategyResolver resolver = new NotificationStrategyResolver(List.of(new TestNotificationStrategy("EMAIL")));

        assertThatThrownBy(() -> resolver.resolve("PUSH"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported notification channel: PUSH");
    }

    private record TestNotificationStrategy(String channelCode) implements NotificationStrategy {

        @Override
        public String getChannelCode() {
            return channelCode;
        }

        @Override
        public void send(String recipient, String message) {
        }
    }
}
