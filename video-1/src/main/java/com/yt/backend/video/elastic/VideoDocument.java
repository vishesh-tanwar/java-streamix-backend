package com.yt.backend.video.elastic;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDocument {

    private Long id;

    private String title;

    private String description;

    private String userName;

    private List<String> tags;

    private Long views;

    private Long likes;
}