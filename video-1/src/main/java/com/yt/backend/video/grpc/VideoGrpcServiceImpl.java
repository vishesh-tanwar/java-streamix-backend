package com.yt.backend.video.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;
import com.yt.backend.video.service.VideoService;

@GrpcService
public class VideoGrpcServiceImpl extends VideoGrpcServiceGrpc.VideoGrpcServiceImplBase {

    @Autowired
    private VideoService videoService;

    @Override
    public void incVideoViewCount(IncViewCountRequest request, StreamObserver<IncViewCountResponse> responseObserver) {
        boolean success = videoService.IncVideoViewCount(request.getVideoId());
        
        IncViewCountResponse response = IncViewCountResponse.newBuilder()
                .setSuccess(success)
                .setMessage(success ? "View count incremented successfully" : "Failed to increment view count")
                .build();
                
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
