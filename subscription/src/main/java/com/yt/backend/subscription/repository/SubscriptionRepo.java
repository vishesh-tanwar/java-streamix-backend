package com.yt.backend.subscription.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yt.backend.subscription.model.SubscriptionModel;

@Repository
public interface SubscriptionRepo extends JpaRepository<SubscriptionModel, Long> {
    SubscriptionModel findByUserId(Long userId);

    boolean existsByUserIdAndChannelId(Long subscriberId, Long channelId);

    Optional<SubscriptionModel> findByUserIdAndChannelId(Long subscriberId, Long channelId);

    void deleteByUserId(Long subscriberId);

    void deleteByChannelId(Long channelId);

}
