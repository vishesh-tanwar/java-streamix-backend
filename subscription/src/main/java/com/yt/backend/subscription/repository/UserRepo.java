package com.yt.backend.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yt.backend.subscription.model.UserModel;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Long> {
}
