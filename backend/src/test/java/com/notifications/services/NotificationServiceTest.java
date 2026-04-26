package com.notifications.services;

import com.notifications.config.NotificationRetryProperties;
import com.notifications.dtos.CreateMessageRequest;
import com.notifications.dtos.UserSubscription;
import com.notifications.models.Category;
import com.notifications.models.NotificationChannel;
import com.notifications.models.NotificationLog;
import com.notifications.repositories.CategoryRepository;
import com.notifications.repositories.MessageRepository;
import com.notifications.repositories.NotificationChannelRepository;
import com.notifications.repositories.NotificationLogRepository;
import com.notifications.strategies.NotificationStrategy;
import com.notifications.strategies.NotificationStrategyResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationService}.
 *
 * <p>This test validates the orchestration logic, including:
 * category lookup, user filtering, strategy execution,
 * retry behavior, and log persistence.</p>
 */
class NotificationServiceTest {

    private CategoryRepository categoryRepository;
    private MessageRepository messageRepository;
    private NotificationChannelRepository channelRepository;
    private NotificationLogRepository notificationLogRepository;
    private UserSubscriptionService userSubscriptionService;
    private NotificationStrategyResolver strategyResolver;
    private NotificationRetryProperties retryProperties;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        messageRepository = mock(MessageRepository.class);
        channelRepository = mock(NotificationChannelRepository.class);
        notificationLogRepository = mock(NotificationLogRepository.class);
        userSubscriptionService = mock(UserSubscriptionService.class);
        strategyResolver = mock(NotificationStrategyResolver.class);
        retryProperties = new NotificationRetryProperties();
        retryProperties.setMaxAttempts(3);

        notificationService = new NotificationService(
                categoryRepository,
                messageRepository,
                channelRepository,
                notificationLogRepository,
                userSubscriptionService,
                strategyResolver,
                retryProperties
        );
    }

    @Test
    @DisplayName("Should process notification and create logs successfully")
    void shouldProcessNotificationAndCreateLogsSuccessfully() {

        CreateMessageRequest request = new CreateMessageRequest("SPORTS", "Test message");

        Category category = new Category();
        category.setCode("SPORTS");

        when(categoryRepository.findByCode("SPORTS")).thenReturn(Optional.of(category));
        when(messageRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserSubscription user = new UserSubscription(
                "user-001",
                "Alice",
                "alice@test.com",
                "+123",
                "device",
                Set.of("SPORTS"),
                Set.of("EMAIL")
        );

        when(userSubscriptionService.findUsersByCategory("SPORTS")).thenReturn(List.of(user));

        NotificationChannel channel = new NotificationChannel();
        channel.setCode("EMAIL");

        when(channelRepository.findByCode("EMAIL")).thenReturn(Optional.of(channel));

        NotificationStrategy strategy = mock(NotificationStrategy.class);
        when(strategyResolver.resolve("EMAIL")).thenReturn(strategy);

        when(notificationLogRepository.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        when(notificationLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = notificationService.createAndSendNotification(request);

        assertThat(response.logs()).hasSize(1);
        assertThat(response.logs().getFirst().channelCode()).isEqualTo("EMAIL");

        verify(strategy, times(1)).send(any(), any());
        verify(notificationLogRepository, times(1)).save(any(NotificationLog.class));
    }

    @Test
    @DisplayName("Should retry when strategy fails and eventually succeed")
    void shouldRetryWhenStrategyFailsAndEventuallySucceed() {

        CreateMessageRequest request = new CreateMessageRequest("SPORTS", "Retry test");

        Category category = new Category();
        category.setCode("SPORTS");

        when(categoryRepository.findByCode("SPORTS")).thenReturn(Optional.of(category));
        when(messageRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserSubscription user = new UserSubscription(
                "user-001",
                "Alice",
                "alice@test.com",
                "+123",
                "device",
                Set.of("SPORTS"),
                Set.of("EMAIL")
        );

        when(userSubscriptionService.findUsersByCategory("SPORTS")).thenReturn(List.of(user));

        NotificationChannel channel = new NotificationChannel();
        channel.setCode("EMAIL");

        when(channelRepository.findByCode("EMAIL")).thenReturn(Optional.of(channel));

        NotificationStrategy strategy = mock(NotificationStrategy.class);

        doThrow(new RuntimeException("fail"))
                .doThrow(new RuntimeException("fail"))
                .doNothing()
                .when(strategy).send(any(), any());

        when(strategyResolver.resolve("EMAIL")).thenReturn(strategy);

        when(notificationLogRepository.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        when(notificationLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = notificationService.createAndSendNotification(request);

        assertThat(response.logs().getFirst().attempts()).isEqualTo(3);

        verify(strategy, times(3)).send(any(), any());
    }
}
