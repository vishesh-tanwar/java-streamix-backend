package com.yt.backend.video.projection;

import java.time.Instant;

public interface VideoProjection {
    Long getId();

    String getTitle();

    String getThumbnail();

    Long getUserId();

    String getUserName();

    String getUserImage();

    Integer getViews();

    Long getDuration();

    Instant getUploadDate();

    String getDescription();
    
    String getVideoUrl();
}
