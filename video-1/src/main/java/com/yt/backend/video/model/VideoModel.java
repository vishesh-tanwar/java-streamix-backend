package com.yt.backend.video.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "videos")
@Getter
@Setter
public class VideoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String video; // path to file
    private String description;
    private String thumbnail;
    private int type;

    @Column(columnDefinition = "text[]")
    private List<String> tags;
    private Long views = 0l;
    private Long likes = 0l;
    private Long dislikes = 0l;
    private Long userId;
    private String userName;
    private String userImage;
    private Double duration;
    private Instant uploadDate = Instant.now();
}
