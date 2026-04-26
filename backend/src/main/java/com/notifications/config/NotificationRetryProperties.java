package com.notifications.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties used to control notification retry behavior.
 *
 * <p>The challenge requires fault tolerance when sending notifications. This
 * configuration allows the maximum number of delivery attempts to be adjusted
 * without changing source code, making the retry behavior easier to test and
 * evolve.</p>
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "notification.retry")
public class NotificationRetryProperties {

    private int maxAttempts;
}
