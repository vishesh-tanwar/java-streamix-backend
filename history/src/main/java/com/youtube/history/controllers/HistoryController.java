package com.youtube.history.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youtube.history.dto.AddHistoryReqDto;
import com.youtube.history.dto.ResponseDto;
import com.youtube.history.services.HistoryService;

@RestController
@RequestMapping("history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addHistory(@RequestParam String videoId, Authentication auth) {
        try {
            Long userId = auth != null && auth.getDetails() != null ? (Long) auth.getDetails() : null;
    
            if (userId == null) {
                return ResponseEntity.status(401).body(new ResponseDto("Unauthorized", false));
            }
            AddHistoryReqDto addHistoryReqDto = new AddHistoryReqDto(userId, videoId);
            historyService.addHistory(addHistoryReqDto);
            return ResponseEntity.ok(new ResponseDto("History added successfully", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDto("Failed to add history", false));
        }
    }    
}