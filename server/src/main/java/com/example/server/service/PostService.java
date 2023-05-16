package com.example.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.server.model.Post;
import com.example.server.model.User;
import com.example.server.repository.PostRepository;

@Service
public class PostService {
  @Autowired
  private PostRepository postRepository;

  public Post addNewPost(User user, String content){
    Post post = new Post();
    post.setContent(content);
    post.setCreated_by(user);

    return postRepository.save(post);
  }

  public List<Post> getAllPost() {
    return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
  }

  public Optional<Post> getOnePost(Integer id) {
    return postRepository.findById(id);
  }

  public void deleteOnePost(Integer id) {
    postRepository.deleteById(id);
    return;
  }

  public Post updatePost(Post post, String content) {
    post.setContent(content);
    return postRepository.save(post);
  }
  
  public List<Post> getUserPosts(User user) {
    return postRepository.findUsersPost(user);
  }
}
