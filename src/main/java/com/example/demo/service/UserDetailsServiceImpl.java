package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UsuarioRepository usuarioRepo;

  // Inyección por constructor requerida por SonarQube
  public UserDetailsServiceImpl(UsuarioRepository usuarioRepo) {
    this.usuarioRepo = usuarioRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario u = usuarioRepo.findByNombreusuario(username)
        .orElseThrow(() -> new UsernameNotFoundException("No existe: " + username));

    String rolName = (u.getRol() != null && u.getRol().getDescripcion() != null)
        ? u.getRol().getDescripcion()
        : "ROLE_USER";

    boolean enabled = u.getEstado() != null && u.getEstado() == 1;

    return User.withUsername(u.getNombreusuario())
        .password(u.getContrasena())              // BCrypt ya guardado en DB
        .authorities(new SimpleGrantedAuthority(rolName))
        .accountExpired(false).accountLocked(false)
        .credentialsExpired(false).disabled(!enabled)
        .build();
  }
}