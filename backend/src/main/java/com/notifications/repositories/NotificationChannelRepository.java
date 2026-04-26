package com.notifications.repositories;

import com.notifications.models.NotificationChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for managing {@link com.notifications.models.NotificationChannel} entities.
 *
 * <p>Provides access to notification channel catalog data, which defines the
 * available delivery mechanisms such as email, SMS, and push notifications.</p>
 *
 * <p>This repository is used to resolve channels dynamically, enabling a flexible
 * and scalable design that supports the addition of new delivery strategies.</p>
 */
public interface NotificationChannelRepository extends JpaRepository<NotificationChannel, Long> {

    Optional<NotificationChannel> findByCode(String code);
}
