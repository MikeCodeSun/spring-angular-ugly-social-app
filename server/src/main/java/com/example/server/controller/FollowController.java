package com.example.server.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.Follow;
import com.example.server.model.User;
import com.example.server.service.CustomUserDetailService;
import com.example.server.service.FollowService;

@RestController
@RequestMapping("follow")
public class FollowController {

  @Autowired
  private CustomUserDetailService customUserDetailService;
  @Autowired
  private FollowService followService;

  public User getCurrentUser() {
    CustomUserDetail customUserDetail = (CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return customUserDetail.getUser();
  }
  
  @PostMapping("user/{id}")
  public ResponseEntity<?> followOrUnFollow(@PathVariable("id") Integer id) {

    // get follow/unfollow user
    Optional<User> optFollowUser = customUserDetailService.findUserById(id);
    if(optFollowUser.isEmpty()) {
      return new ResponseEntity<String>("Not Found User", HttpStatus.NOT_FOUND);
    }
    User followUser = optFollowUser.get();
    // get current user
    User currentUser = getCurrentUser();
    // check currentUser is follow or not follow followUser 
    
    Follow follow = followService.hasFollow(currentUser, followUser);

    //if follow is null then follow, eles already follow then unfollow-delete follow
    if(follow == null){
      Follow newFollow = followService.follow(currentUser, followUser);
      return new ResponseEntity<String>("follow", HttpStatus.OK);
    }else {
      followService.unFollow(follow);
      return new ResponseEntity<String>("unfollow", HttpStatus.OK);
    }

  }
}
