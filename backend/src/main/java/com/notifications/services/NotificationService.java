package com.notifications.services;

import com.notifications.config.NotificationRetryProperties;
import com.notifications.dtos.CreateMessageRequest;
import com.notifications.dtos.NotificationLogHistoryResponse;
import com.notifications.dtos.NotificationLogResponse;
import com.notifications.dtos.NotificationResponse;
import com.notifications.dtos.UserSubscription;
import com.notifications.exceptions.ResourceNotFoundException;
import com.notifications.models.Category;
import com.notifications.models.Message;
import com.notifications.models.NotificationChannel;
import com.notifications.models.NotificationLog;
import com.notifications.models.NotificationStatus;
import com.notifications.repositories.CategoryRepository;
import com.notifications.repositories.MessageRepository;
import com.notifications.repositories.NotificationChannelRepository;
import com.notifications.repositories.NotificationLogRepository;
import com.notifications.strategies.NotificationStrategy;
import com.notifications.strategies.NotificationStrategyResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for orchestrating the notification delivery flow.
 *
 * <p>This service coordinates category validation, message persistence, user
 * subscription lookup, channel strategy resolution, retry execution,
 * idempotency checks, and consistent notification logging.</p>
 *
 * <p>The implementation keeps infrastructure details behind repositories and
 * notification strategies, preserving separation of concerns and allowing new
 * channels or user sources to be introduced with minimal changes.</p>
 */
@Service
public class NotificationService {

    private final CategoryRepository categoryRepository;

    private final MessageRepository messageRepository;

    private final NotificationChannelRepository channelRepository;

    private final NotificationLogRepository notificationLogRepository;

    private final UserSubscriptionService userSubscriptionService;

    private final NotificationStrategyResolver strategyResolver;

    private final NotificationRetryProperties retryProperties;

    public NotificationService(
            CategoryRepository categoryRepository,
            MessageRepository messageRepository,
            NotificationChannelRepository channelRepository,
            NotificationLogRepository notificationLogRepository,
            UserSubscriptionService userSubscriptionService,
            NotificationStrategyResolver strategyResolver,
            NotificationRetryProperties retryProperties
    ) {
        this.categoryRepository = categoryRepository;
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.notificationLogRepository = notificationLogRepository;
        this.userSubscriptionService = userSubscriptionService;
        this.strategyResolver = strategyResolver;
        this.retryProperties = retryProperties;
    }

    /**
     * Creates a message and delivers it to users subscribed to its category.
     *
     * <p>The method persists the message, searches subscribed users, sends the
     * notification through each enabled channel, applies retry logic, prevents
     * duplicate processing through idempotency keys, and stores one consistent
     * log per user and channel.</p>
     *
     * @param request validated request containing the category and message body
     * @return notification processing response with delivery logs
     */
    @Transactional
    public NotificationResponse createAndSendNotification(CreateMessageRequest request) {
        String categoryCode = request.categoryCode().toUpperCase();
        LocalDateTime now = LocalDateTime.now();

        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryCode));

        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setCategory(category);
        message.setBody(request.message());
        message.setCreatedAt(now);

        Message savedMessage = messageRepository.save(message);
        List<UserSubscription> subscribedUsers = userSubscriptionService.findUsersByCategory(categoryCode);
        List<NotificationLogResponse> logResponses = new ArrayList<>();

        for (UserSubscription user : subscribedUsers) {
            for (String channelCode : user.subscribedChannels()) {
                NotificationLog notificationLog = processChannelDelivery(savedMessage, user, channelCode);
                logResponses.add(toLogResponse(notificationLog));
            }
        }

        return new NotificationResponse(
                savedMessage.getId(),
                category.getCode(),
                savedMessage.getBody(),
                savedMessage.getCreatedAt(),
                logResponses
        );
    }

    private NotificationLog processChannelDelivery(Message message, UserSubscription user, String channelCode) {
        String normalizedChannelCode = channelCode.toUpperCase();
        String idempotencyKey = buildIdempotencyKey(message.getId(), user.id(), normalizedChannelCode);

        return notificationLogRepository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> createNotificationLog(message, user, normalizedChannelCode, idempotencyKey));
    }

    private NotificationLog createNotificationLog(
            Message message,
            UserSubscription user,
            String channelCode,
            String idempotencyKey
    ) {
        LocalDateTime now = LocalDateTime.now();
        NotificationChannel channel = channelRepository.findByCode(channelCode)
                .orElseThrow(() -> new ResourceNotFoundException("Notification channel not found: " + channelCode));

        DeliveryResult deliveryResult = sendWithRetry(user, channelCode, message.getBody());

        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setId(UUID.randomUUID());
        notificationLog.setMessage(message);
        notificationLog.setChannel(channel);
        notificationLog.setUserId(user.id());
        notificationLog.setUserName(user.name());
        notificationLog.setUserEmail(user.email());
        notificationLog.setUserPhone(user.phone());
        notificationLog.setStatus(deliveryResult.status());
        notificationLog.setAttempts(deliveryResult.attempts());
        notificationLog.setErrorMessage(deliveryResult.errorMessage());
        notificationLog.setIdempotencyKey(idempotencyKey);
        notificationLog.setCreatedAt(now);
        notificationLog.setUpdatedAt(now);

        return notificationLogRepository.save(notificationLog);
    }

    private DeliveryResult sendWithRetry(UserSubscription user, String channelCode, String message) {
        NotificationStrategy strategy = strategyResolver.resolve(channelCode);
        String recipient = resolveRecipient(user, channelCode);
        int maxAttempts = Math.max(1, retryProperties.getMaxAttempts());
        String lastErrorMessage = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                strategy.send(recipient, message);
                return new DeliveryResult(NotificationStatus.SUCCESS, attempt, null);
            } catch (RuntimeException exception) {
                lastErrorMessage = exception.getMessage();
            }
        }

        return new DeliveryResult(NotificationStatus.FAILED, maxAttempts, lastErrorMessage);
    }

    private String resolveRecipient(UserSubscription user, String channelCode) {
        return switch (channelCode) {
            case "EMAIL" -> user.email();
            case "SMS" -> user.phone();
            case "PUSH" -> user.deviceToken();
            default -> throw new IllegalArgumentException("Unsupported recipient channel: " + channelCode);
        };
    }

    private String buildIdempotencyKey(UUID messageId, String userId, String channelCode) {
        return messageId + ":" + userId + ":" + channelCode;
    }

    private NotificationLogResponse toLogResponse(NotificationLog notificationLog) {
        return new NotificationLogResponse(
                notificationLog.getId(),
                notificationLog.getChannel().getCode(),
                notificationLog.getUserId(),
                notificationLog.getUserName(),
                notificationLog.getStatus(),
                notificationLog.getAttempts(),
                notificationLog.getErrorMessage(),
                notificationLog.getCreatedAt(),
                notificationLog.getUpdatedAt()
        );
    }

    private record DeliveryResult(
            NotificationStatus status,
            Integer attempts,
            String errorMessage
    ) {
    }

    /**
     * Retrieves notification delivery logs ordered from newest to oldest.
     *
     * <p>This method supports the log history requirement from the user interface.
     * It returns all relevant persisted delivery information without exposing JPA
     * entities directly to the client.</p>
     *
     * @return notification log history ordered by creation timestamp descending
     */
    @Transactional(readOnly = true)
    public List<NotificationLogHistoryResponse> findNotificationLogHistory() {
        return notificationLogRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toLogHistoryResponse)
                .toList();
    }

    private NotificationLogHistoryResponse toLogHistoryResponse(NotificationLog notificationLog) {
        return new NotificationLogHistoryResponse(
                notificationLog.getId(),
                notificationLog.getMessage().getId(),
                notificationLog.getMessage().getCategory().getCode(),
                notificationLog.getMessage().getBody(),
                notificationLog.getChannel().getCode(),
                notificationLog.getUserId(),
                notificationLog.getUserName(),
                notificationLog.getUserEmail(),
                notificationLog.getUserPhone(),
                notificationLog.getStatus(),
                notificationLog.getAttempts(),
                notificationLog.getErrorMessage(),
                notificationLog.getCreatedAt(),
                notificationLog.getUpdatedAt()
        );
    }
}
