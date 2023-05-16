package com.example.server.detail;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.server.model.User;

public class CustomUserDetail implements UserDetails {

  private User user;
  public CustomUserDetail(User user) {
    this.user = user;
  }

  public User getUser(){
    return user;
  }
  
  public Integer getId() {
    return user.getId();
  }

  

  @Override
  public String getPassword() {
    return user.getPassword();
  }
  @Override
  public String getUsername() {
    return user.getUsername();
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  @Override
  public boolean isEnabled() {
    return true;
  }
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }
}
