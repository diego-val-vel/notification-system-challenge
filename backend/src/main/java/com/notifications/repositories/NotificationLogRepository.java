package com.notifications.repositories;

import com.notifications.models.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link com.notifications.models.NotificationLog} entities.
 *
 * <p>Provides access to notification delivery logs, which store the outcome of
 * sending messages to users through different channels.</p>
 *
 * <p>This repository plays a critical role in ensuring idempotency by allowing
 * lookups based on a unique idempotency key, preventing duplicate processing
 * and supporting fault tolerance mechanisms such as retries.</p>
 */
public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {

    Optional<NotificationLog> findByIdempotencyKey(String idempotencyKey);
    List<NotificationLog> findAllByOrderByCreatedAtDesc();
}
