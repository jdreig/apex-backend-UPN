package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.AuthenticationReq;
import com.example.demo.model.TokenInfo;
import com.example.demo.service.JwtUtilService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class tokenController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserDetailsService usuarioDetailsService;
    @Autowired private JwtUtilService jwtUtilService;

    @PostMapping("/autenticarToken")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationReq req) {
        
        // Validación preventiva para campos nulos
        if (req.getUsuario() == null || req.getClave() == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("mensaje", "Los campos 'usuario' y 'clave' son obligatorios en el JSON.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        try {
            // Autenticar credenciales
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsuario(), req.getClave())
            );

            // Generar JWT
            final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(req.getUsuario());
            final String jwt = jwtUtilService.generateToken(userDetails);

            return ResponseEntity.ok(new TokenInfo(jwt));

        } catch (BadCredentialsException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("mensaje", "Usuario o contraseña incorrectos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("mensaje", "Error interno en el servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}