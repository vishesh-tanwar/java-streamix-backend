package com.yt.backend.video.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yt.backend.video.model.VideoModel;
import com.yt.backend.video.projection.VideoProjection;

import org.springframework.data.domain.Page;

@Repository
public interface VideoRepo extends JpaRepository<VideoModel, Long> {
    Page<VideoModel> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query(value = """
            SELECT
                v.id AS id,
                v.title AS title,
                v.thumbnail AS thumbnail,
                v.user_Id AS userId,
                v.user_name AS userName,
                v.user_image AS userImage,
                v.views AS views,
                v.duration AS duration,
                v.upload_date AS uploadDate,
                v.description AS description,
                v.video AS videoUrl
            FROM videos v
            WHERE
                LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%'))
                OR :query = ANY(v.tags)
            """, countQuery = """
            SELECT COUNT(*) FROM videos v
            WHERE
                LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%'))
                OR :query = ANY(v.tags)
            """, nativeQuery = true)
    Page<VideoProjection> searchVideos(@Param("query") String query, Pageable pageable);

    Page<VideoModel> findByType(int type, Pageable pageable);
}
