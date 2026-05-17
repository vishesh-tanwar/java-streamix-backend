package com.yt.backend.video.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class uploadVideoRequestDto {
    private String title;
    private String description;
    private MultipartFile video;
    private MultipartFile thumbnail;
    private int type;
    private List<String> tags;
}
