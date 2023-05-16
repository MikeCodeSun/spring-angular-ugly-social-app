package com.example.server.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.server.handler.CustomLoginFailureHandler;
import com.example.server.provider.CustomAuthProvide;

 

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Autowired
  private CustomAuthProvide customAuthProvide;

  @Autowired
  private CustomLoginFailureHandler customLoginFailureHandler;

  // @Autowired
  // private CustomUserDetailService customUserDetailService;

  // @Bean 
  // public BCryptPasswordEncoder bCryptPasswordEncoder() {
  //   return new BCryptPasswordEncoder();
  // }

  // @Bean
  // public DaoAuthenticationProvider daoAuthenticationProvider() {
  //   DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
  //   daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
  //   daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
  //   return daoAuthenticationProvider;
  // }

  @Bean 
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(customAuthProvide);
    return authenticationManagerBuilder.build();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
    .cors().and()
    .csrf().disable()
    .authorizeHttpRequests( ahr -> ahr.requestMatchers("/home", "/register", "/errormsg/*", "/post/all").permitAll().anyRequest().authenticated()).logout().logoutSuccessUrl("/api/v1/bye").deleteCookies("JSESSIONID").permitAll().and().formLogin().defaultSuccessUrl("/api/v1/login").failureHandler(customLoginFailureHandler).permitAll().and().httpBasic().and().authenticationManager(authenticationManager(http));
    return http.build();
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource  source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
    config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type","Authorization"));
    config.setExposedHeaders(Arrays.asList("Authorization" ));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  
}
