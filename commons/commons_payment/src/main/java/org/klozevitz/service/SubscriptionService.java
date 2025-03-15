package org.klozevitz.service;

import lombok.RequiredArgsConstructor;
import org.klozevitz.entites.Subscription;
import org.klozevitz.repository.SubscriptionRepository;

import java.util.List;

@RequiredArgsConstructor
public class SubscriptionService {

    private static final SubscriptionService INSTANCE = new SubscriptionService(
            SubscriptionRepository.getInstance()
    );

    private final SubscriptionRepository subscriptionRepository;

    public List<Subscription> baseSubscription() {
        return subscriptionRepository.baseSubscription();
    }
    public List<Subscription> additionalSubscription() {
        return subscriptionRepository.additionalSubscription();
    }
    public static SubscriptionService getInstance() {
        return INSTANCE;
    }



}
