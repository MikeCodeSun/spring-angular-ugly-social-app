package com.example.server.controller;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.Follow;
import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import com.example.server.service.FollowService;

import jakarta.validation.Valid;


@RestController
public class AuthController {

  @Autowired
  private UserRepository userRepository;
  @Autowired 
  private FollowService followService;
  
  public User getCurrentUser() {
    CustomUserDetail customUserDetail = (CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = customUserDetail.getUser();
    return user;
  }

  @PostMapping("register")
  public ResponseEntity<Object> register(@Valid @RequestBody User user, BindingResult result) {
    if(result.hasErrors()){
      Map<String, String> errors = new HashMap<>();
      for(FieldError err:result.getFieldErrors()){
        errors.put(err.getField(), err.getDefaultMessage());
      }
      return new ResponseEntity<>(errors, HttpStatus.OK);
    }
    BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
    String encodePassword = bc.encode(user.getPassword());
    user.setPassword(encodePassword);
    userRepository.save(user);
    Map<String, String> msg = new HashMap<>();
    msg.put("message", "register successfully");
    return new ResponseEntity<>(msg, HttpStatus.OK);
  }

  @PostMapping("upload")
  public ResponseEntity<?>  uploadImage(@RequestParam("image") MultipartFile multipartFile){
    String uuidName = UUID.randomUUID().toString();
    String fileName = multipartFile.getOriginalFilename();
    String newFileName ="";
    
    int index = fileName.lastIndexOf('.');
    Long size = multipartFile.getSize();

    Map<String, String> msg = new HashMap<>();
    // check image size
    if(size > 110000) {
      msg.put("Exception", "Image size too big must less than 1mb");
      return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    // get image extention name and check type
    if(index>0) {
      String extention = fileName.substring(index+1);
      if(!extention.equals("png") && !extention.equals("jpg") && !extention.equals("jpeg")){
        return new ResponseEntity<>("Image type not support!", HttpStatus.OK);
      }
      newFileName = uuidName + "." + extention;
    }
      
    // create folder
    String dir = "image";
    Path uploadPath = Paths.get(dir);
    if(Files.notExists(uploadPath)){
      try {
        Files.createDirectories(uploadPath);
      } catch (Exception e) {
        System.out.println("Exception: " + e);
        return new ResponseEntity<>("Exception: " + e, HttpStatus.OK);
      }
    }
    // get user pic
    User currentUser =getCurrentUser();
    User user = userRepository.findById(currentUser.getId()).get();
    String userImageName = user.getImage();
    // check if user already get image, if exist then delete old one.
    if(userImageName != null) {
      try {
        Files.deleteIfExists(uploadPath.resolve(userImageName));
      } catch (Exception e) {
        return new ResponseEntity<>("Exception: " + e, HttpStatus.OK);
      }
    }
    // save file path
    Path filePath = uploadPath.resolve(newFileName);
    System.out.println("tofile + abs: "+uploadPath.toFile().getAbsolutePath());
    // save pic in folder
    try (InputStream inputStream = multipartFile.getInputStream()) {
      Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      return new ResponseEntity<>("Exception: " + e, HttpStatus.OK);
    }
    // save image name to user db
    user.setImage(newFileName);
    userRepository.save(user);
    
    msg.put("image", newFileName);
    return new ResponseEntity<>(msg, HttpStatus.OK);
  }

  @GetMapping("/errormsg/{msg}")
  public String handle_error(@PathVariable("msg") String msg ) {
    return msg;
  }
  
  @GetMapping("home")
  public Map<String, Object> home() {
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("id", UUID.randomUUID().toString());
    model.put("content", "good morning!");
    return model;
  }

  @GetMapping("/api/v1/user/{id}")
  public User getUser(@PathVariable("id") Integer id) {
    Optional<User> optUser= userRepository.findById(id);
    
    if(optUser.isEmpty()){
      return new User();
    }
    User user = optUser.get();
    User currentUser = getCurrentUser();
    Follow follow = followService.hasFollow(currentUser, user);

    if(follow == null) {
      user.setIsFollow(false);
    }else {
      user.setIsFollow(true);
    }

    user.followersSize();
    user.followingSize();

    
    return user;
  }
  
  @GetMapping("/api/v1/login")
  public String login() {
    return "login";
  }
  
  @GetMapping("/api/v1/hello")
  public String hello() {
    CustomUserDetail customUserDetail = (CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = customUserDetail.getUsername();
    return  username;
  }


  @GetMapping("/api/v1/bye")
  public String bye() {
    return "bye user ";
  }

}
