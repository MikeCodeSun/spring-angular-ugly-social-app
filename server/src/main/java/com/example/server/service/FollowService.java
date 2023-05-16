package com.example.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.model.Follow;
import com.example.server.model.User;
import com.example.server.repository.FollowRepository;

@Service
public class FollowService {
  
  @Autowired
  public FollowRepository followRepository;
  
  public Follow hasFollow(User currentUser, User followUser) {
    return followRepository.findFollowByBothUsers(currentUser, followUser);
  }

  public Follow follow(User currentUser, User followUser) {
    Follow follow = new Follow();
    follow.setFrom(currentUser);
    follow.setTo(followUser);
    followRepository.save(follow);
    return follow;
  }

  public void unFollow(Follow follow) {
    followRepository.delete(follow);
    return;
  }
}
