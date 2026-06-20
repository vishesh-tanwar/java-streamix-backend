package com.yt.backend.video.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.yt.backend.video.dto.GetVideoResponseDto;
import com.yt.backend.video.dto.uploadVideoRequestDto;
import com.yt.backend.video.elastic.VideoDocument;
import com.yt.backend.video.globalException.VideoUploadException;
import com.yt.backend.video.model.VideoModel;
import com.yt.backend.video.repository.VideoRepo;

@Service
public class VideoService {
    // private final String VIDEO_UPLOAD_DIR = "/Users/visheshtanwar/uploads/videos/";
    // private final String THUMB_UPLOAD_DIR = "/Users/visheshtanwar/uploads/thumbnails/";

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ElasticSearchService elasticsearchService;

    public  String uploadVideo(uploadVideoRequestDto requestDto,Long userId) {
        try {
            Map videoUploadResult;
            Map thumbUploadResult;
                // VIDEO UPLOAD
            videoUploadResult = cloudinary.uploader().upload(
                requestDto.getVideo().getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", "streamix/videos"
                )
            );
            thumbUploadResult = cloudinary.uploader().upload(
                requestDto.getThumbnail().getBytes(),
                ObjectUtils.asMap(
                        "folder", "streamix/thumbnails"
                )
            );
            
            String videoUrl =
            videoUploadResult.get("secure_url").toString();

            Double duration = (Double) videoUploadResult.get("duration");
            
            // THUMBNAIL UPLOAD
            String thumbnailUrl =
            thumbUploadResult.get("secure_url").toString();
            
            // SAVE DB
            VideoModel video = new VideoModel();
            
            video.setTitle(requestDto.getTitle());
            video.setDescription(requestDto.getDescription());
            video.setType(requestDto.getType());
            video.setTags(requestDto.getTags());
            
            video.setVideo(videoUrl);
            video.setThumbnail(thumbnailUrl);
            video.setDuration(duration);
            
            video.setLikes(0l);
            video.setDislikes(0l);
            video.setViews(0l);
            
            video.setUserId(userId);
            video.setUserName("brock");
            
            videoRepo.save(video);
            
            VideoDocument document =
            VideoDocument.builder()
            .id(video.getId())
            .title(video.getTitle())
            .description(video.getDescription())
            .userName(video.getUserName())
            .tags(video.getTags())
            .views(video.getViews())
            .likes(video.getLikes())
            .build();
            
            elasticsearchService.indexVideo(document);
            
            return "Video uploaded successfully";       
        } catch (Exception e) {
            throw new VideoUploadException("upload failed", e);
        }
    }

    public Page<GetVideoResponseDto> getVideos(int type, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        Page<VideoModel> results ;

        if (type != -1) {
            results = videoRepo.findByType(type,pageable);
        } else {
            results = videoRepo.findAll(pageable);
        }

        return results.map(v -> {
            GetVideoResponseDto dto = new GetVideoResponseDto();
            dto.setVideoId(v.getId());
            dto.setVideoId(v.getId());
            dto.setTitle(v.getTitle());
            dto.setThumbnail(v.getThumbnail());
            dto.setViews(Long.valueOf(v.getViews()));
            dto.setUserId(v.getUserId());
            dto.setUserName(v.getUserName());
            dto.setUserImage(v.getUserImage());
            dto.setDuration(v.getDuration());
            dto.setUploadDate(v.getUploadDate());
            dto.setDescription(v.getDescription());
            dto.setVideoUrl(v.getVideo());
            return dto;
        });
    }
}
