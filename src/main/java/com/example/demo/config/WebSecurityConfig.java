package com.example.demo.config;

import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

  @Autowired private UserDetailsServiceImpl userDetailsService;
  @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private RestAuthEntryPoint restAuthEntryPoint;

  @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> {}) // <<--- habilita CORS usando tu CorsConfig (WebMvcConfigurer)
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authz -> authz
        // Preflight
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        // Público para obtener el JWT
        .requestMatchers("/api/autenticarToken").permitAll()

        // Resto protegido
        .anyRequest().authenticated()
      )
      .userDetailsService(userDetailsService)
      .exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthEntryPoint))
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
