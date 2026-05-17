package com.yt.backend.demo.AuthService.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeletedEvent {
    private Long userId;

    public UserDeletedEvent() {
    }

    public UserDeletedEvent(Long userId) {
        this.userId = userId;
    }
}
