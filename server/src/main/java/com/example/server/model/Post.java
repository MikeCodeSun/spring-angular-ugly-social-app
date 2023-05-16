package com.example.server.model;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "content")
  @NotBlank(message = "Content must not be mepty!")
  @Size(max = 140, message = "Content must less than 140!")
  private String content;

  @Column(name = "created_at")
  @CreationTimestamp
  private Date createdAt;

  @ManyToOne()
  @JoinColumn(name = "created_by")
  private User created_by;

}
