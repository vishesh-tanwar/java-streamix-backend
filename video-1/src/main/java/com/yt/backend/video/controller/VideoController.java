package com.yt.backend.video.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    public Page<GetVideoResponseDto> getVideos(
            @RequestParam(defaultValue = "-1") int type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return videoService.getVideos(type,page, size);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String,String>> uploadVideo(@ModelAttribute uploadVideoRequestDto requestDto, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        System.out.println("Authenticated User ID: " + userId);
        final String msg = videoService.uploadVideo(requestDto, userId);
                    return ResponseEntity.ok().body(Map.of("message", msg,"status","200"));       
    }
}
