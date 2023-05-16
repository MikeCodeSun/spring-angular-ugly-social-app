package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.server.model.Follow;
import com.example.server.model.User;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
  
  @Query("SELECT f FROM Follow f WHERE (f.from = :currentUser AND f.to = :followUser)")
  Follow findFollowByBothUsers(@Param("currentUser") User currentUser,@Param("followUser") User followUser);
}
