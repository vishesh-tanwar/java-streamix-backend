package com.yt.backend.subscription.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.yt.backend.subscription.exceptions.SubscriptionException;
import com.yt.backend.subscription.model.SubscriptionModel;
import com.yt.backend.subscription.repository.SubscriptionRepo;
import com.yt.backend.subscription.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Autowired
    private UserRepo userRepo;

    public SubscriptionModel subscribe(Long subscriberId, Long channelId) {

        // Check if channel exists
        userRepo.findById(channelId)
                .orElseThrow(() -> new SubscriptionException("Channel does not exist"));

        // Prevent self subscription
        if (subscriberId.equals(channelId)) {
            throw new SubscriptionException("You cannot subscribe to yourself");
        }

        // Check if already subscribed
        if (subscriptionRepo.existsByUserIdAndChannelId(subscriberId, channelId)) {
            throw new SubscriptionException("User already subscribed to this channel");
        }

        // Create new subscription
        SubscriptionModel subscription = new SubscriptionModel();
        subscription.setUserId(subscriberId);
        subscription.setChannelId(channelId);

        return subscriptionRepo.save(subscription);
    }

    public void unsubscribe(Long subscriberId, Long channelId) {

        SubscriptionModel sub = subscriptionRepo
                .findByUserIdAndChannelId(subscriberId, channelId)
                .orElseThrow(() -> new SubscriptionException("Subscription not found"));

        subscriptionRepo.delete(sub);
    }

    @KafkaListener(topics = "user-deleted-topic", groupId = "subscription-group")
    @Transactional
    public void handleUserDeleted(Long userId) {

        System.out.println("Kafka Event Received: " + userId);

        subscriptionRepo.deleteByUserId(userId);
        subscriptionRepo.deleteByChannelId(userId);
    }

}
