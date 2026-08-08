package com.yt.backend.video.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponseDto {

    private Long id;

    private String thumbnail;

    private Long userId;

    private String userName;

    private Long videoId;

    private String userImage;

    private Long duration;

    private String title;

    private String description;

    private String category;

    private Long views;

    private Long likes;
}