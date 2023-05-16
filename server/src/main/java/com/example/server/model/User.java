package com.example.server.model;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.example.server.validator.Unique;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "usernmae", unique = true)
  @NotBlank(message = "Username must not be empty")
  @Size(min = 3, max = 10, message = "Username must between 3 and 10")
  @Unique(message = "Username already taken")
  private String username;

  @Column(name = "password")
  @Size(min = 6, max = 100, message = "Password must more 6")
  private String password;

  @Column(name = "created_at")
  @CreationTimestamp
  private Date created_at;

  @Column(name = "image")
  private String image;

  @OneToMany(mappedBy = "created_by", fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Post> posts;



  @OneToMany(mappedBy = "from", fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Follow> followers;

  @OneToMany(mappedBy = "to", fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Follow> following;

  
  @Transient
  private Integer followersNum;
  @Transient
  private Integer followingsNum;
  @Transient
  private boolean follow;

  @Transient()
  public void followersSize() {
    this.followersNum =  this.following.size();
  }

  @Transient
  public void followingSize() {
    this.followingsNum = this.followers.size();
  }
  @Transient
  public void setIsFollow(boolean follow) {
    this.follow = follow;
  }
}