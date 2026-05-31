package com.yt.backend.video.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.yt.backend.video.dto.SearchResponseDto;
import com.yt.backend.video.elastic.VideoDocument;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient elasticsearchClient;

    // SEARCH SUGGESTIONS

    public List<String> getSuggestions(
            String keyword
    ) {

        try {

            SearchResponse<VideoDocument> response =
                    elasticsearchClient.search(s -> s

                                    .index("videos")

                                    .size(20)

                                    .query(q -> q
                                            .multiMatch(m -> m
                                                    .query(keyword)

                                                    .fields(
                                                            "title",
                                                            "description",
                                                            "userName"
                                                    )

                                                    .fuzziness("AUTO")
                                            )
                                    ),

                            VideoDocument.class
                    );

            List<String> suggestions =
                    new ArrayList<>();

            response.hits()
                    .hits()
                    .forEach(hit -> {

                        if (hit.source() != null) {

                            suggestions.add(
                                    hit.source().getTitle()
                            );
                        }
                    });

            return suggestions;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Suggestion search failed"
            );
        }
    }

    public List<SearchResponseDto> searchVideos(
        String keyword,
        int page,
        int size
    ) {
    try {
        SearchResponse<VideoDocument> response = elasticsearchClient.search(s -> s
                                .index("videos")
                                .from(page * size)
                                .size(size)
                                .query(q -> q
                                        .multiMatch(m -> m
                                                .query(keyword)
                                                .fields(
                                                        "title^3",
                                                        "description",
                                                        "userName^2"
                                                )
                                                .fuzziness("AUTO")
                                        )
                                )
                                .sort(sort -> sort
                                        .score(sc -> sc.order(
                                                co.elastic.clients.elasticsearch._types.SortOrder.Desc
                                        ))
                                )
                        ,
                        VideoDocument.class
                );

        List<SearchResponseDto> videos =
                new ArrayList<>();

        response.hits()
                .hits()
                .forEach(hit -> {
                    VideoDocument doc =
                            hit.source();

                    if (doc != null) {
                        videos.add(
                                SearchResponseDto.builder()
                                        .id(doc.getId())
                                        .title(doc.getTitle())
                                        .description(doc.getDescription())
                                        .channelName(doc.getUserName())
                                        .views(doc.getViews())
                                        .likes(doc.getLikes())
                                        .build()
                        );
                    }
                });

        return videos;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Search failed"
            );
        }
    }
}