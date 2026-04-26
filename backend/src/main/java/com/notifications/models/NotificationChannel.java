package com.notifications.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entity representing a notification delivery channel.
 *
 * <p>This catalog defines the available mechanisms used to deliver
 * notifications to users. It enables scalability by allowing new
 * channels to be introduced with minimal impact.</p>
 *
 * <p>Examples: EMAIL, SMS, PUSH</p>
 */
@Getter
@Setter
@Entity
@Table(name = "notification_channels")
public class NotificationChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
