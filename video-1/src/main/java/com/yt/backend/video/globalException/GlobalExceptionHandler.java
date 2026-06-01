package com.yt.backend.video.globalException;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(VideoUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleVideoUploadEx(VideoUploadException ex){
        Map<String,String> response = new HashMap<>();
        response.put("error", "Video upload failed: " + ex.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> handleGeneralEx(Exception ex){
        Map<String,String> response = new HashMap<>();
        response.put("error", "An unexpected error occurred: " + ex.getMessage());
        return response;
    }
}
