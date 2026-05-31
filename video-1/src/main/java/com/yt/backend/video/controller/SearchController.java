package com.yt.backend.video.controller;

import com.yt.backend.video.dto.SearchResponseDto;
import com.yt.backend.video.service.SearchService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")

@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // Suggestions API

    @GetMapping("/suggestions")
    public List<String> getSuggestions(

            @RequestParam String q

    ) {

        return searchService
                .getSuggestions(q);
    }

    // Video Search API

    @GetMapping("/videos")
    public List<SearchResponseDto> searchVideos(

            @RequestParam String q,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return searchService.searchVideos(
                q,
                page,
                size
        );
    }
}