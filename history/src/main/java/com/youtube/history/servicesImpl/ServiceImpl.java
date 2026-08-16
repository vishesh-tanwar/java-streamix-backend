package com.youtube.history.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youtube.history.dto.AddHistoryReqDto;
import com.youtube.history.grpc.VideoGrpcClient;
import com.youtube.history.models.History;
import com.youtube.history.repository.HistoryRepo;
import com.youtube.history.services.HistoryService;

import jakarta.transaction.Transactional;

@Service
public class ServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private VideoGrpcClient videoGrpcClient;

    @Override
    public boolean addHistory(AddHistoryReqDto addHistoryReqDto) {
        try {
            saveToHistoryAndIncViewCount(addHistoryReqDto);
            
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Transactional
    private boolean saveToHistoryAndIncViewCount(AddHistoryReqDto addHistoryReqDto) {
        try {
            History history = new History();
            history.setUserId(addHistoryReqDto.getUserId());
            history.setVideoId(addHistoryReqDto.getVideoId());

            historyRepo.save(history);

            // Increment the view count for the video via gRPC to video service
            if (addHistoryReqDto.getVideoId() != null) {
                try {
                    Long videoId = Long.parseLong(addHistoryReqDto.getVideoId());
                    videoGrpcClient.incVideoViewCount(videoId);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid videoId format: " + addHistoryReqDto.getVideoId());
                }
            }

            return true; // Indicate success
        } catch (Exception e) {
            // Handle any exceptions that may occur during the process
            return false; // Indicate failure
        }
    }
    
}
