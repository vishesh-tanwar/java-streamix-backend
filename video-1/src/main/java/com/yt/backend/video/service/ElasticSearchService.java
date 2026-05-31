package com.yt.backend.video.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.yt.backend.video.elastic.VideoDocument;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public void indexVideo(
            VideoDocument document
    ) {

        try {

            IndexResponse response =
                    elasticsearchClient.index(i -> i
                            .index("videos")
                            .id(document.getId().toString())
                            .document(document)
                    );

            System.out.println(
                    "Indexed with id: " +
                            response.id()
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to index video"
            );
        }
    }
}