package com.notifications.repositories;

import com.notifications.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Repository interface for managing {@link com.notifications.models.Message} entities.
 *
 * <p>Handles persistence operations for messages submitted to the system.
 * Messages represent the core domain entity and are associated with categories
 * and multiple notification delivery attempts.</p>
 *
 * <p>This repository enables storage and retrieval of message data required
 * during the notification processing lifecycle.</p>
 */
public interface MessageRepository extends JpaRepository<Message, UUID> {
}
