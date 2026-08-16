package com.youtube.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youtube.history.models.History;

public interface HistoryRepo extends JpaRepository<History ,Long> {
    
}
