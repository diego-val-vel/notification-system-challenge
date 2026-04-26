package com.notifications.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties used to simulate notification delivery failures.
 *
 * <p>The challenge requires fault tolerance when sending notifications. These
 * properties allow each delivery channel to fail with a configurable probability,
 * making retries and error handling testable without integrating with real
 * external providers.</p>
 *
 * <p>Each value must be between {@code 0.0} and {@code 1.0}, where {@code 0.0}
 * means the provider never fails and {@code 1.0} means the provider always fails.</p>
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "notification.failure")
public class NotificationFailureProperties {

    private double emailRate;
    private double smsRate;
    private double pushRate;
}
