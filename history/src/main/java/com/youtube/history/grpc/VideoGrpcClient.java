package com.youtube.history.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import net.devh.boot.grpc.client.inject.GrpcClient;
import com.yt.backend.video.grpc.VideoGrpcServiceGrpc;
import com.yt.backend.video.grpc.IncViewCountRequest;
import com.yt.backend.video.grpc.IncViewCountResponse;

@Service
public class VideoGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(VideoGrpcClient.class);

    @GrpcClient("videoService")
    private VideoGrpcServiceGrpc.VideoGrpcServiceBlockingStub videoGrpcStub;

    public boolean incVideoViewCount(Long videoId) {
        try {
            IncViewCountRequest request = IncViewCountRequest.newBuilder()
                    .setVideoId(videoId)
                    .build();

            IncViewCountResponse response = videoGrpcStub.incVideoViewCount(request);
            return response.getSuccess();
        } catch (Exception e) {
            log.error("Failed to call VideoService via gRPC for videoId {}: {}", videoId, e.getMessage());
            return false;
        }
    }
}
