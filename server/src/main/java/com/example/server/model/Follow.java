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
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "follow")

public class Follow {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne()
  @JoinColumn(name = "follow_from")
  private User from;

  @ManyToOne()
  @JoinColumn(name = "follow_to")
  private User to;

  @Column(name = "created_at")
  @CreationTimestamp
  private Date created_at;
  
}
