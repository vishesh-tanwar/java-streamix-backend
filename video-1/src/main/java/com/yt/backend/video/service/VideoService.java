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
import com.yt.backend.video.model.VideoModel;
import com.yt.backend.video.projection.VideoProjection;
import com.yt.backend.video.repository.VideoRepo;

@Service
public class VideoService {
    // private final String VIDEO_UPLOAD_DIR = "/Users/visheshtanwar/uploads/videos/";
    // private final String THUMB_UPLOAD_DIR = "/Users/visheshtanwar/uploads/thumbnails/";

    @Autowired
    private VideoRepo videoRepo;

      @Autowired
    private Cloudinary cloudinary;

    // public String uploadVideo(uploadVideoRequestDto requestDto) {
    //     try {
    //         // 1. Save Video File
    //         String original = requestDto.getVideo().getOriginalFilename();
    //         String videoSafeName = original.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
    //         String videoFileName = System.currentTimeMillis() + "_" + videoSafeName;
    //         Path videoPath = Paths.get(VIDEO_UPLOAD_DIR + videoFileName);
    //         Files.createDirectories(videoPath.getParent());
    //         Files.copy(requestDto.getVideo().getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);

    //         String originalThumbnail = requestDto.getThumbnail().getOriginalFilename();
    //         String thumbnailSafeName = originalThumbnail.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
    //         String thumbnailFileName = System.currentTimeMillis() + "_" + thumbnailSafeName;
    //         Path thumbPath = Paths.get(THUMB_UPLOAD_DIR + thumbnailFileName);
    //         Files.createDirectories(thumbPath.getParent());
    //         Files.copy(requestDto.getThumbnail().getInputStream(), thumbPath, StandardCopyOption.REPLACE_EXISTING);

    //         VideoModel video = new VideoModel();
    //         video.setTitle(requestDto.getTitle());
    //         video.setDescription(requestDto.getDescription());
    //         video.setType(requestDto.getType());
    //         video.setTags(requestDto.getTags());
    //         video.setVideo("/videos/" + videoFileName);
    //         video.setThumbnail("/thumbnails/" + thumbnailFileName);
    //         video.setLikes(0);
    //         video.setDislikes(0);
    //         video.setViews(0);
    //         video.setDuration(null);
    //         video.setUserId(7L);
    //         video.setUserName("brock");
    //         video.setUserImage(null);

    //         videoRepo.save(video);

    //         return "Video uploaded successfully";
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return "Video upload failed: " + e.getMessage();
    //     }

    // }


    public String uploadVideo(uploadVideoRequestDto requestDto,Long userId) {
        try {

            // VIDEO UPLOAD
            Map videoUploadResult = cloudinary.uploader().upload(
                    requestDto.getVideo().getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "video",
                            "folder", "streamix/videos"
                    )
            );

            String videoUrl =
                    videoUploadResult.get("secure_url").toString();

            // THUMBNAIL UPLOAD
            Map thumbUploadResult = cloudinary.uploader().upload(
                    requestDto.getThumbnail().getBytes(),
                    ObjectUtils.asMap(
                            "folder", "streamix/thumbnails"
                    )
            );

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

            video.setLikes(0);
            video.setDislikes(0);
            video.setViews(0);

            video.setUserId(userId);
            video.setUserName("brock");

            videoRepo.save(video);

            return "Video uploaded successfully";

        } catch (Exception e) {

            e.printStackTrace();

            return "Upload failed: " + e.getMessage();
        }
    }

    // public Page<GetVideoResponseDto> getVideos(String query, int page, int size)
    // {
    // Specification<VideoModel> specs = VideoSpecification.getSpecification(query);
    // PageRequest pageRequest = PageRequest.of(page, size);
    // Page<VideoModel> videoPage = videoRepo.findAll(specs, pageRequest);
    // return null;

    // }

    public Page<GetVideoResponseDto> getVideos(String query, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        String q = (query == null || query.isBlank()) ? "" : query.toLowerCase();

        Page<VideoProjection> results = videoRepo.searchVideos(q, pageable);

        return results.map(v -> {
            GetVideoResponseDto dto = new GetVideoResponseDto();
            dto.setVideoId(v.getId());
            dto.setTitle(v.getTitle());
            dto.setThumbnail(v.getThumbnail());
            dto.setViews(Long.valueOf(v.getViews()));
            dto.setUserId(v.getUserId());
            dto.setUserName(v.getUserName());
            dto.setUserImage(v.getUserImage());
            dto.setDuration(v.getDuration());
            dto.setUploadDate(v.getUploadDate());
            return dto;
        });
    }
}
