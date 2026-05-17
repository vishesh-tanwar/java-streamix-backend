package com.yt.backend.video.specification;

import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import com.yt.backend.video.model.VideoModel;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.*;

public class VideoSpecification {
    public static Specification<VideoModel> getSpecification(String query) {
        return new Specification<VideoModel>() {

            @Override
            public @Nullable Predicate toPredicate(
                    Root<VideoModel> root,
                    CriteriaQuery<?> criteriaQuery,
                    CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();

                String q = query.toString().toLowerCase();

                // title LIKE %query%
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + q + "%"));

                // description LIKE %query%
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + q + "%"));

                // tags @> query (array contains query)
                predicates.add(
                        cb.isNotNull(
                                cb.function(
                                        "array_position",
                                        Integer.class,
                                        root.get("tags"),
                                        cb.literal(q))));

                return cb.or(predicates.toArray(new Predicate[0]));
            }

        };
    }
}
