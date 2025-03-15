package org.klozevitz.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.klozevitz.config.ObjectMapperConfig;
import org.klozevitz.entites.Subscription;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SubscriptionRepository {

    private static final SubscriptionRepository INSTANCE = new SubscriptionRepository(
            ObjectMapperConfig.getInstance()
    );

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public List<Subscription> baseSubscription() {
        List<Subscription> subscriptions = new ArrayList<>();
        try (InputStream subscriptionStream = getClass().getClassLoader()
                .getResourceAsStream("subscriptions.json")) {
            JsonNode rootNode = objectMapper.readTree(subscriptionStream);
            if (rootNode.hasNonNull("subscriptions")) {
                JsonNode subscriptionsNode = rootNode.get("subscriptions");
                subscriptions.addAll(List.of(objectMapper.convertValue(subscriptionsNode, Subscription[].class)));
            }
        }
        return subscriptions;
    }

    @SneakyThrows
    public List<Subscription> additionalSubscription() {
        List<Subscription> subscriptions = new ArrayList<>();
        try (InputStream subscriptionStream = getClass().getClassLoader()
                .getResourceAsStream("addSubscriptions.json")) {
            JsonNode rootNode = objectMapper.readTree(subscriptionStream);
            if (rootNode.hasNonNull("addSubscriptions")) {
                JsonNode subscriptionsNode = rootNode.get("addSubscriptions");
                subscriptions.addAll(List.of(objectMapper.convertValue(subscriptionsNode, Subscription[].class)));
            }
        }
        return subscriptions;
    }

    public static SubscriptionRepository getInstance() {
        return INSTANCE;
    }
}
