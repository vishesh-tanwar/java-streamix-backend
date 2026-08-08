package com.yt.backend.video.projection;

import java.time.Instant;

public interface VideoProjection {
    Long getId();

    String getTitle();

    String getThumbnail();

    Long getUserId();

    String getUserName();

    String getUserImage();

    Long getViews();

    Double getDuration();

    Instant getUploadDate();

    String getDescription();
    
    String getVideoUrl();

    Long getLikes();
}
