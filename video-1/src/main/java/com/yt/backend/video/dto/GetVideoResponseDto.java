package com.yt.backend.video.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetVideoResponseDto {
    private Long videoId;
    private String title;
    private String thumbnail;
    private Long views;
    private Long userId;
    private String userName;
    private String userImage;
    private Instant uploadDate;
    private Double duration;
    private String description;
    private String videoUrl;
}
