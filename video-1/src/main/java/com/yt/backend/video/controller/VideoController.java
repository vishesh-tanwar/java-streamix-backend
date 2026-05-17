package com.yt.backend.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yt.backend.video.dto.GetVideoResponseDto;
import com.yt.backend.video.dto.uploadVideoRequestDto;
import com.yt.backend.video.service.VideoService;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/get")
    public Page<GetVideoResponseDto> getVideos(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return videoService.getVideos(search, page, size);
    }

    @PostMapping("/upload")
    public String uploadVideo(@ModelAttribute uploadVideoRequestDto requestDto, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        System.out.println("Authenticated User ID: " + userId);

        return videoService.uploadVideo(requestDto, userId);
    }
}
