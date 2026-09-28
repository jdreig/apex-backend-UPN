package com.example.demo.config;

import com.example.demo.service.UserDetailsServiceImpl; 
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

  @Bean 
  public PasswordEncoder passwordEncoder() { 
      return new BCryptPasswordEncoder(); 
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(
          HttpSecurity http,
          UserDetailsServiceImpl userDetailsService,
          JwtAuthenticationFilter jwtAuthenticationFilter,
          RestAuthEntryPoint restAuthEntryPoint) throws Exception {
      
    http
      .csrf(csrf -> csrf.disable()) // Seguro en APIs REST stateless (JWT)
      .cors(cors -> {}) 
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authz -> authz
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .requestMatchers("/api/autenticarToken").permitAll()
        .anyRequest().authenticated()
      )
      .userDetailsService(userDetailsService)
      .exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthEntryPoint))
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}