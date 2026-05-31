package com.yt.backend.video.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.analysis.TokenChar;
import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void createIndex() {

        try {

            boolean exists =
                    elasticsearchClient.indices()
                            .exists(e -> e.index("videos"))
                            .value();

            if (exists) {
                return;
            }

            elasticsearchClient.indices().create(c -> c
                    .index("videos")

                    .settings(s -> s
                            .analysis(a -> a
                                    .analyzer(
                                            "autocomplete_analyzer",
                                            an -> an
                                                    .custom(ca -> ca
                                                            .tokenizer(
                                                                    "autocomplete_tokenizer"
                                                            )
                                                            .filter(
                                                                    "lowercase"
                                                            )
                                                    )
                                    )

                                    .tokenizer(
                                        "autocomplete_tokenizer",
                                        tk -> tk
                                            .definition(td -> td
                                                .edgeNgram(ng -> ng
                                                    .minGram(2)
                                                    .maxGram(10)
                                                    .tokenChars(
                                                        TokenChar.Letter,
                                                        TokenChar.Digit
                                                    )
                                                )
                                            )
                                    )
                            )
                    )

                    .mappings(m -> m
                            .properties("title", p -> p
                                    .text(t -> t
                                            .analyzer(
                                                    "autocomplete_analyzer"
                                            )
                                            .searchAnalyzer("standard")
                                    )
                            )

                            .properties("description", p -> p
                                    .text(t -> t)
                            )

                            .properties("userName", p -> p
                                    .text(t -> t)
                            )

                            .properties("views", p -> p
                                    .long_(l -> l)
                            )

                            .properties("likes", p -> p
                                    .long_(l -> l)
                            )
                    )
            );

            System.out.println(
                    "Videos index created"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}