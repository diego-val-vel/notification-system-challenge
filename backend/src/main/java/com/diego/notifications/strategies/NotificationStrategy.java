package com.diego.notifications.strategies;

/**
 * Strategy contract for sending notifications through a specific delivery channel.
 *
 * <p>This abstraction enables the system to follow the Open Closed Principle by
 * allowing new notification channels to be added through new implementations
 * without modifying the main notification orchestration flow.</p>
 *
 * <p>Each strategy is responsible for defining its supported channel and for
 * executing the simulated delivery process for that channel.</p>
 */
public interface NotificationStrategy {

    /**
     * Returns the unique channel code supported by this strategy.
     *
     * @return supported channel code
     */
    String getChannelCode();

    /**
     * Sends a notification to a specific recipient.
     *
     * @param recipient notification recipient destination
     * @param message notification message body
     */
    void send(String recipient, String message);
}
