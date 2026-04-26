package com.notifications.services;

import com.notifications.config.NotificationRetryProperties;
import com.notifications.models.Category;
import com.notifications.models.Message;
import com.notifications.models.NotificationChannel;
import com.notifications.models.NotificationLog;
import com.notifications.models.NotificationStatus;
import com.notifications.repositories.CategoryRepository;
import com.notifications.repositories.MessageRepository;
import com.notifications.repositories.NotificationChannelRepository;
import com.notifications.repositories.NotificationLogRepository;
import com.notifications.strategies.NotificationStrategyResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for notification log history retrieval.
 *
 * <p>These tests validate that the service returns persisted notification logs
 * using response DTOs and preserves the repository ordering from newest to
 * oldest.</p>
 */
class NotificationLogHistoryServiceTest {

    @Test
    @DisplayName("Should return notification log history ordered from newest to oldest")
    void shouldReturnNotificationLogHistoryOrderedFromNewestToOldest() {
        NotificationLogRepository notificationLogRepository = mock(NotificationLogRepository.class);

        NotificationService notificationService = new NotificationService(
                mock(CategoryRepository.class),
                mock(MessageRepository.class),
                mock(NotificationChannelRepository.class),
                notificationLogRepository,
                mock(UserSubscriptionService.class),
                mock(NotificationStrategyResolver.class),
                new NotificationRetryProperties()
        );

        NotificationLog newestLog = buildNotificationLog("Newest message", LocalDateTime.now());
        NotificationLog oldestLog = buildNotificationLog("Oldest message", LocalDateTime.now().minusHours(1));

        when(notificationLogRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(newestLog, oldestLog));

        var response = notificationService.findNotificationLogHistory();

        assertThat(response).hasSize(2);
        assertThat(response.getFirst().message()).isEqualTo("Newest message");
        assertThat(response.getLast().message()).isEqualTo("Oldest message");
    }

    private NotificationLog buildNotificationLog(String messageBody, LocalDateTime createdAt) {
        Category category = new Category();
        category.setCode("SPORTS");

        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setCategory(category);
        message.setBody(messageBody);
        message.setCreatedAt(createdAt);

        NotificationChannel channel = new NotificationChannel();
        channel.setCode("EMAIL");

        NotificationLog log = new NotificationLog();
        log.setId(UUID.randomUUID());
        log.setMessage(message);
        log.setChannel(channel);
        log.setUserId("user-001");
        log.setUserName("Alice Johnson");
        log.setUserEmail("alice@example.com");
        log.setUserPhone("+5215550000001");
        log.setStatus(NotificationStatus.SUCCESS);
        log.setAttempts(1);
        log.setErrorMessage(null);
        log.setIdempotencyKey(UUID.randomUUID() + ":user-001:EMAIL");
        log.setCreatedAt(createdAt);
        log.setUpdatedAt(createdAt);

        return log;
    }
}
