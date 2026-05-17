package com.yt.backend.subscription.model;

public class UserDeletedEvent {

    private Long userId;

    public UserDeletedEvent() {
    }

    public UserDeletedEvent(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
