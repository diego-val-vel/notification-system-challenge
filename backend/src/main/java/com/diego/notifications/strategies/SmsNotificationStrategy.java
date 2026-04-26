package com.diego.notifications.strategies;

import com.diego.notifications.config.NotificationFailureProperties;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Notification strategy responsible for SMS delivery.
 *
 * <p>This implementation simulates an external SMS provider. The failure
 * probability is configurable to support controlled testing of retry and
 * fault tolerance behavior.</p>
 */
@Component
public class SmsNotificationStrategy implements NotificationStrategy {

    private static final String CHANNEL_CODE = "SMS";

    private final NotificationFailureProperties failureProperties;

    public SmsNotificationStrategy(NotificationFailureProperties failureProperties) {
        this.failureProperties = failureProperties;
    }

    /**
     * Returns the supported notification channel code.
     *
     * @return SMS channel code
     */
    @Override
    public String getChannelCode() {
        return CHANNEL_CODE;
    }

    /**
     * Simulates sending an SMS notification.
     *
     * @param recipient phone number that receives the notification
     * @param message notification message body
     */
    @Override
    public void send(String recipient, String message) {
        simulateFailure(failureProperties.getSmsRate());
    }

    private void simulateFailure(double failureRate) {
        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            throw new IllegalStateException("SMS provider failed to deliver the notification.");
        }
    }
}
