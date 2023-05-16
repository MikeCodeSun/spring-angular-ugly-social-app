package com.example.server.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.server.detail.CustomUserDetail;
import com.example.server.service.CustomUserDetailService;


@Component
public class CustomAuthProvide implements AuthenticationProvider {

  @Autowired
  private CustomUserDetailService customUserDetailService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String)authentication.getCredentials();

  if(username.isBlank() || password.isBlank()) {
    // System.out.println("no name or password");
    throw new UsernameNotFoundException("Usernmae and Password must not be empty!");
  }
  
  if(customUserDetailService != null && customUserDetailService.existUserByUsername(username) == false){
    // System.out.println("no user");
    throw new UsernameNotFoundException("Username not exist");
  } else {
    CustomUserDetail customUserDetail = customUserDetailService.loadUserByUsername(username);
    BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
    boolean isValid = bc.matches(password, customUserDetail.getPassword());
    if(!isValid) {
      // System.out.println("wrong password");
      throw new BadCredentialsException("password not match");
    }else {
      // System.out.println("go!");
      return new UsernamePasswordAuthenticationToken(customUserDetail, password, null);
    }
  }

  
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }

}
