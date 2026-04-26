package com.notifications.dtos;

import java.util.Set;

/**
 * DTO representing a user and their notification subscriptions.
 *
 * <p>The challenge does not require user management persistence, so this
 * structure models users loaded from a local JSON mock source. Each user
 * declares the categories they are interested in and the delivery channels
 * they have enabled.</p>
 *
 * <p>Using sets for subscriptions allows efficient membership checks when
 * filtering users by category or channel.</p>
 *
 * @param id user identifier
 * @param name user display name
 * @param email user email address
 * @param phone user phone number
 * @param deviceToken user device token for push notifications
 * @param subscribedCategories category codes enabled for the user
 * @param subscribedChannels channel codes enabled for the user
 */
public record UserSubscription(
        String id,
        String name,
        String email,
        String phone,
        String deviceToken,
        Set<String> subscribedCategories,
        Set<String> subscribedChannels
) {
}