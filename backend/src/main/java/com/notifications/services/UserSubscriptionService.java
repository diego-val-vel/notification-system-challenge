package com.notifications.services;

import com.notifications.dtos.UserSubscription;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Service responsible for loading and searching user notification subscriptions.
 *
 * <p>User data is loaded from a JSON mock file because the challenge focuses on
 * notification delivery rather than user management. Keeping this logic inside
 * a dedicated service preserves separation of concerns and allows replacing the
 * JSON source with a database or external API without changing the notification
 * orchestration flow.</p>
 *
 * <p>The search method filters users by category using set membership checks,
 * which keeps the lookup simple, readable, and efficient for the current
 * challenge scope.</p>
 */
@Service
public class UserSubscriptionService {

    private static final String USERS_MOCK_PATH = "mock/users.json";

    private final ObjectMapper objectMapper;

    public UserSubscriptionService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Finds users subscribed to the requested category.
     *
     * <p>The method loads users from the mock JSON source and filters only those
     * whose subscribed categories contain the requested category code. The
     * category comparison is case-insensitive from the request perspective by
     * normalizing the input to uppercase.</p>
     *
     * @param categoryCode notification category code received from the request
     * @return users subscribed to the requested category
     */
    public List<UserSubscription> findUsersByCategory(String categoryCode) {
        String normalizedCategoryCode = categoryCode.toUpperCase();

        return loadUsers()
                .stream()
                .filter(user -> user.subscribedCategories().contains(normalizedCategoryCode))
                .toList();
    }

    private List<UserSubscription> loadUsers() {
        try (InputStream inputStream = new ClassPathResource(USERS_MOCK_PATH).getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load user subscriptions from mock source.", exception);
        }
    }
}
