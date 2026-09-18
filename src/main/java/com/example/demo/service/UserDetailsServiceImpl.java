package com.example.demo.service;

import com.example.demo.model.usuario;
import com.example.demo.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired private usuarioRepository usuarioRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Ideal: agrega en el repo un método directo
    usuario u = usuarioRepo.findByNombreusuario(username)
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
