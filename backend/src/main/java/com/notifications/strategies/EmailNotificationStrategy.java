package com.notifications.strategies;

import com.notifications.config.NotificationFailureProperties;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Notification strategy responsible for email delivery.
 *
 * <p>This implementation simulates an external email provider. The failure
 * probability is controlled through application properties so retry behavior
 * can be tested consistently during development and review.</p>
 */
@Component
public class EmailNotificationStrategy implements NotificationStrategy {

    private static final String CHANNEL_CODE = "EMAIL";

    private final NotificationFailureProperties failureProperties;

    public EmailNotificationStrategy(NotificationFailureProperties failureProperties) {
        this.failureProperties = failureProperties;
    }

    /**
     * Returns the supported notification channel code.
     *
     * @return email channel code
     */
    @Override
    public String getChannelCode() {
        return CHANNEL_CODE;
    }

    /**
     * Simulates sending an email notification.
     *
     * @param recipient email address that receives the notification
     * @param message notification message body
     */
    @Override
    public void send(String recipient, String message) {
        simulateFailure(failureProperties.getEmailRate());
    }

    private void simulateFailure(double failureRate) {
        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            throw new IllegalStateException("Email provider failed to deliver the notification.");
        }
    }
}
