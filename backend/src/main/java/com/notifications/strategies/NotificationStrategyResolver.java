package com.notifications.strategies;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resolver responsible for selecting the correct notification strategy by channel code.
 *
 * <p>This component implements the lookup mechanism for the Strategy pattern.
 * It receives all available {@link NotificationStrategy} implementations from
 * Spring and stores them in a map indexed by channel code, providing efficient
 * constant-time resolution during notification processing.</p>
 *
 * <p>Adding a new notification channel only requires creating a new strategy
 * implementation. The resolver discovers it automatically through dependency
 * injection, preserving scalability and the Open Closed Principle.</p>
 */
@Component
public class NotificationStrategyResolver {

    private final Map<String, NotificationStrategy> strategiesByChannelCode;

    public NotificationStrategyResolver(List<NotificationStrategy> strategies) {
        this.strategiesByChannelCode = strategies
                .stream()
                .collect(Collectors.toUnmodifiableMap(NotificationStrategy::getChannelCode, Function.identity()));
    }

    /**
     * Resolves the strategy assigned to the provided channel code.
     *
     * @param channelCode channel code to resolve
     * @return matching notification strategy
     * @throws IllegalArgumentException when no strategy exists for the channel code
     */
    public NotificationStrategy resolve(String channelCode) {
        NotificationStrategy strategy = strategiesByChannelCode.get(channelCode);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported notification channel: " + channelCode);
        }

        return strategy;
    }
}
