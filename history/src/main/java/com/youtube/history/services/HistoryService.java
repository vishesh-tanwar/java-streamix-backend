package com.youtube.history.services;

import org.springframework.stereotype.Service;

import com.youtube.history.dto.AddHistoryReqDto;

@Service
public interface HistoryService {
    boolean addHistory(AddHistoryReqDto addHistoryReqDto);
}
