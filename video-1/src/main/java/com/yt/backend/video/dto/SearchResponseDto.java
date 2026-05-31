package com.yt.backend.video.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponseDto {

    private Long id;

    private String title;

    private String description;

    private String category;

    private String channelName;

    private List<String> tags;

    private Long views;

    private Long likes;
}