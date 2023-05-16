package com.example.server.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.Follow;
import com.example.server.model.Post;
import com.example.server.model.User;
import com.example.server.service.CustomUserDetailService;
import com.example.server.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("post")
public class PostControler {

  @Autowired
  private PostService postService;

  @Autowired
  private CustomUserDetailService customUserDetailService;

  public User getPrincipalUser() {
    CustomUserDetail customUserDetail = (CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return customUserDetail.getUser();
  }
  
  @PostMapping("add")
  public ResponseEntity<?> addPost(Principal user, @Valid @RequestBody Post post, BindingResult result) {

    // check user input 
    if(result.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      for (FieldError err:result.getFieldErrors()){
        errors.put(err.getField(), err.getDefaultMessage());
      }
      return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
    }

    User createdByUser = getPrincipalUser();

    // use post service
    Post savedPost = postService.addNewPost(createdByUser, post.getContent());

    return new ResponseEntity<Post>(savedPost, HttpStatus.OK);
  }

  @GetMapping("all")
  public ResponseEntity<List<Post>> allPosts(){
    return new ResponseEntity<List<Post>>(postService.getAllPost(), HttpStatus.OK);
  }

  @GetMapping("id/{id}")
  public ResponseEntity<?> onePost(@PathVariable("id") Integer id) {
    Optional<Post> optPost = postService.getOnePost(id);
    if(optPost.isEmpty()) {
      return new ResponseEntity<String>("post_id: "+id+" not Found", HttpStatus.NOT_FOUND);
    }
    Post post =optPost.get();
    return new ResponseEntity<Post>(post, HttpStatus.OK);
  }

  @DeleteMapping("delete/{id}")
  public ResponseEntity<?> deletePost(@PathVariable("id") Integer id) {
    // check post exist?
    Optional<Post> optPost = postService.getOnePost(id);
    if(optPost.isEmpty()) {
      return new ResponseEntity<String>("post_id: "+id+" not Found", HttpStatus.NOT_FOUND);
    }
    // check post is belong to current user
    User user = getPrincipalUser();
    if(optPost.get().getCreated_by().getId() != user.getId()) {
      return new ResponseEntity<String>("You cant delete post not yours", HttpStatus.UNAUTHORIZED);
    }
    
    postService.deleteOnePost(id);
    return new ResponseEntity<String>("Delete", HttpStatus.OK);
  }

  @PatchMapping("update/{id}")
  public ResponseEntity<?> updatePost(@PathVariable("id") Integer id, @Valid @RequestBody Post post, BindingResult bResult) {
    
    // check user input 
    if(bResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      for (FieldError err:bResult.getFieldErrors()){
        errors.put(err.getField(), err.getDefaultMessage());
      }
      return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
    }
    // check post exist?
    Optional<Post> optPost = postService.getOnePost(id);
    if(optPost.isEmpty()) {
      return new ResponseEntity<String>("post_id: "+id+" not Found", HttpStatus.NOT_FOUND);
    }
    // check post is belong to current user
    User user = getPrincipalUser();
    Post updatePost =optPost.get();
    if(updatePost.getCreated_by().getId() != user.getId()) {
      return new ResponseEntity<String>("You cant update post not yours", HttpStatus.UNAUTHORIZED);
    }
    // 
    updatePost = postService.updatePost(updatePost, post.getContent());
    return new ResponseEntity<Post>(updatePost, HttpStatus.OK);
  }

  @GetMapping("user/{userid}")
  public ResponseEntity<?> getUserPosts(@PathVariable("userid") Integer userid){
    Optional<User> optUser = customUserDetailService.findUserById(userid);
    if(optUser.isEmpty()) {
      return new ResponseEntity<String>("user not Found", HttpStatus.OK);
    }
    User user = optUser.get();
    return new ResponseEntity<List<Post>>(postService.getUserPosts(user), HttpStatus.OK);
  }

  @GetMapping("follow")
  public ResponseEntity<?> getFollowPosts() {
    User currentUser = getPrincipalUser();
    Integer userId= currentUser.getId();
    User logUser = customUserDetailService.findUserById(userId).get();
    List<Follow> follows = logUser.getFollowers();
    // System.out.println("follows: "+ follows);
    List<Post> posts = new ArrayList<>();
    for(Follow f:follows) {
      // System.out.println("f: "+ f);
      User user =f.getTo();
      // System.out.println("user: "+ user);
      List<Post> userPosts = user.getPosts();
      // System.out.println("user posts: "+ userPosts);
      for(Post p:userPosts) {
        // System.out.println("p: "+ p.getId());
        posts.add(p);  
      }
    }
    if(posts.size() > 0) {
      Collections.sort(posts, new Comparator<Post>() {
        @Override
        public int compare(Post o1, Post o2) {
          return o1.getId().compareTo(o2.getId());
        }
      });
    }
    // follows.forEach(f -> {
    //   System.out.println("f: "+ f);
    //   f.getTo().getPosts().forEach(p -> {
    //     System.out.println("p: "+ p);
    //     posts.add(p);
    //   });
    // });
    return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
  }
}
