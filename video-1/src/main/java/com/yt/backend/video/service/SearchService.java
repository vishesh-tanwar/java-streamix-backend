package com.yt.backend.video.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.yt.backend.video.dto.GetVideoResponseDto;
import com.yt.backend.video.elastic.VideoDocument;
import com.yt.backend.video.projection.VideoProjection;
import com.yt.backend.video.repository.VideoRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient elasticsearchClient;

    private final VideoRepo videoRepo;

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

public List<GetVideoResponseDto> searchVideos(
        String keyword,
        int page,
        int size
) {
    try {
        SearchResponse<VideoDocument> response =
                elasticsearchClient.search(s -> s
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
                                ),
                        VideoDocument.class
                );

        // Elasticsearch ranking
        List<Long> videoIds = response.hits()
                .hits()
                .stream()
                .map(hit -> hit.source())
                .filter(Objects::nonNull)
                .map(VideoDocument::getId)
                .toList();

        if (videoIds.isEmpty()) {
            return List.of();
        }

        // One DB query
        List<VideoProjection> projections =
                videoRepo.getVideosByIds(videoIds.toArray(new Long[0]));

        // Map DB results by ID
        Map<Long, VideoProjection> videoMap =
                projections.stream()
                        .collect(Collectors.toMap(
                                VideoProjection::getId,
                                Function.identity()
                        ));

        // Restore Elasticsearch ranking
        return videoIds.stream()
                .map(videoMap::get)
                .filter(Objects::nonNull)
                .map(video -> new GetVideoResponseDto(
                        video.getId(),
                        video.getTitle(),
                        video.getThumbnail(),
                        video.getViews(),
                        video.getUserId(),
                        video.getUserName(),
                        video.getUserImage(),
                        video.getUploadDate(),
                        video.getDuration(),
                        video.getDescription(),
                        video.getVideoUrl(),
                        video.getLikes()     
                ))
                .toList();

    } catch (Exception e) {
        throw new RuntimeException("Search failed", e);
    }
}
}