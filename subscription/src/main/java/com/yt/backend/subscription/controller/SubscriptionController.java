package com.yt.backend.subscription.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yt.backend.subscription.model.SubscriptionModel;
import com.yt.backend.subscription.service.SubscriptionService;

@RequestMapping("/subscriptions")
@RestController
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(
            @RequestParam Long channelId, Authentication auth) {
        Long userId = (Long) auth.getDetails();

        SubscriptionModel result = subscriptionService.subscribe(userId, channelId);
        Map<String, Object> response = Map.of(
                "status", "ok",
                "data", result);
        return ResponseEntity.ok(response);
    }
}
