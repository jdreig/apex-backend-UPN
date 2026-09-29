package com.example.demo.controller;

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
public class TokenController {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_MENSAJE = "mensaje";

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService usuarioDetailsService;
    private final JwtUtilService jwtUtilService;

    // Inyección por constructor requerida por SonarQube (sin @Autowired)
    public TokenController(AuthenticationManager authenticationManager,
                           UserDetailsService usuarioDetailsService,
                           JwtUtilService jwtUtilService) {
        this.authenticationManager = authenticationManager;
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtUtilService = jwtUtilService;
    }

    @PostMapping("/autenticarToken")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationReq req) {
        
        // Validación preventiva para campos nulos
        if (req.getUsuario() == null || req.getClave() == null) {
            Map<String, Object> error = new HashMap<>();
            error.put(KEY_SUCCESS, false);
            error.put(KEY_MENSAJE, "Los campos 'usuario' y 'clave' son obligatorios en el JSON.");
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

        } catch (BadCredentialsException _) {
            // Se usa '_' ya que la variable de excepción no se referencia en este bloque
            Map<String, Object> error = new HashMap<>();
            error.put(KEY_SUCCESS, false);
            error.put(KEY_MENSAJE, "Usuario o contraseña incorrectos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put(KEY_SUCCESS, false);
            error.put(KEY_MENSAJE, "Error interno en el servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}