package com.yt.backend.demo.AuthService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userDeletedTopic() {
        return new NewTopic("user-deleted-topic", 1, (short) 1);
    }
}