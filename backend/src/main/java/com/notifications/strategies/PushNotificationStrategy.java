package com.notifications.strategies;

import com.notifications.config.NotificationFailureProperties;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Notification strategy responsible for push notification delivery.
 *
 * <p>This implementation simulates an external push notification provider.
 * The failure probability is configurable through application properties,
 * making fault tolerance behavior reproducible and easy to adjust.</p>
 */
@Component
public class PushNotificationStrategy implements NotificationStrategy {

    private static final String CHANNEL_CODE = "PUSH";

    private final NotificationFailureProperties failureProperties;

    public PushNotificationStrategy(NotificationFailureProperties failureProperties) {
        this.failureProperties = failureProperties;
    }

    /**
     * Returns the supported notification channel code.
     *
     * @return push notification channel code
     */
    @Override
    public String getChannelCode() {
        return CHANNEL_CODE;
    }

    /**
     * Simulates sending a push notification.
     *
     * @param recipient device token or user destination identifier
     * @param message notification message body
     */
    @Override
    public void send(String recipient, String message) {
        simulateFailure(failureProperties.getPushRate());
    }

    private void simulateFailure(double failureRate) {
        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            throw new IllegalStateException("Push provider failed to deliver the notification.");
        }
    }
}
