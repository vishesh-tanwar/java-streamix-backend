package com.yt.backend.video.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Long likes;
}
