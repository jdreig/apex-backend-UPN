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

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Habilita peticiones desde el frontend
public class tokenController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService usuarioDetailsService;
    
    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/autenticarToken")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationReq req) {
        System.out.println(">>> Intentando autenticar usuario: " + req.getUsuario());

        try {
            // 1. Validar credenciales
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsuario(), req.getClave())
            );

            // 2. Generar Token
            final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(req.getUsuario());
            final String jwt = jwtUtilService.generateToken(userDetails);
            
            System.out.println(">>> Token generado con éxito");
            return ResponseEntity.ok(new TokenInfo(jwt));

        } catch (BadCredentialsException e) {
            System.out.println(">>> ERROR: Credenciales incorrectas para " + req.getUsuario());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o clave incorrectos");
        } catch (Exception e) {
            System.out.println(">>> ERROR GENERAL: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor: " + e.getMessage());
        }
    }
}