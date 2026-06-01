package com.yt.backend.video.globalException;

public class VideoUploadException extends RuntimeException {
    public VideoUploadException(String message , Throwable cause) {
        super(message,cause);
    }
}
