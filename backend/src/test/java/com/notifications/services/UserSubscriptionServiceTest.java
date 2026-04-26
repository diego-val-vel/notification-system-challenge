package com.notifications.services;

import com.notifications.dtos.UserSubscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserSubscriptionService}.
 *
 * <p>These tests validate that user subscriptions are loaded from the mock JSON
 * source and filtered correctly by category, including case-insensitive input
 * handling and empty results for unsupported categories.</p>
 */
class UserSubscriptionServiceTest {

    private final UserSubscriptionService userSubscriptionService = new UserSubscriptionService(new ObjectMapper());

    @Test
    @DisplayName("Should find users subscribed to SPORTS category")
    void shouldFindUsersSubscribedToSportsCategory() {
        List<UserSubscription> users = userSubscriptionService.findUsersByCategory("SPORTS");

        assertThat(users)
                .extracting(UserSubscription::id)
                .containsExactlyInAnyOrder("user-001", "user-003");
    }

    @Test
    @DisplayName("Should find users subscribed to MOVIES category")
    void shouldFindUsersSubscribedToMoviesCategory() {
        List<UserSubscription> users = userSubscriptionService.findUsersByCategory("MOVIES");

        assertThat(users)
                .extracting(UserSubscription::id)
                .containsExactlyInAnyOrder("user-002", "user-003");
    }

    @Test
    @DisplayName("Should normalize category input to uppercase")
    void shouldNormalizeCategoryInputToUppercase() {
        List<UserSubscription> users = userSubscriptionService.findUsersByCategory("sports");

        assertThat(users)
                .extracting(UserSubscription::id)
                .containsExactlyInAnyOrder("user-001", "user-003");
    }

    @Test
    @DisplayName("Should return empty list when no users are subscribed to category")
    void shouldReturnEmptyListWhenNoUsersAreSubscribedToCategory() {
        List<UserSubscription> users = userSubscriptionService.findUsersByCategory("UNKNOWN");

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("Should expose subscribed channels for each matched user")
    void shouldExposeSubscribedChannelsForEachMatchedUser() {
        List<UserSubscription> users = userSubscriptionService.findUsersByCategory("FINANCE");

        assertThat(users).hasSize(1);
        assertThat(users.getFirst().subscribedChannels()).isEqualTo(Set.of("EMAIL", "SMS"));
    }
}
