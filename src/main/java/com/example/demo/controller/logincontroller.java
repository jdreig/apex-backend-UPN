package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.model.usuario;
import com.example.demo.repository.usuarioRepository;

@RestController
@RequestMapping("/api")
public class logincontroller {

    @Autowired
    private usuarioRepository usuarioRepo;
    public static class LoginRequest { 
        public String nombreusuario;
        public String contrasena;
    }
    public static class LoginResponse {
        public boolean success;
        public String mensaje;
        public Long idusuario;
        public String nombres;
        public String apellidos;
        public String rol;
        public Integer idTipoRol;
        public String empresa;

        public LoginResponse(boolean success, String mensaje) {
            this.success = success;
            this.mensaje = mensaje;
        }
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

     // LOG AGREGADO: Imprime en consola el hash generado para "admin" cada vez que ejecutas la petición
        System.out.println("==========================================");
        System.out.println("HASH BCRYPT PARA 'admin': " + encoder.encode("admin"));
        System.out.println("==========================================");
        
        usuario user = usuarioRepo.findAll().stream()
                .filter(u -> u.getNombreusuario().equals(request.nombreusuario))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return new LoginResponse(false, "Usuario o contraseña incorrectos");
        }

        // Validar contraseña encriptada
        if (!encoder.matches(request.contrasena, user.getContrasena())) {
            return new LoginResponse(false, "Usuario o contraseña incorrectos");
        }

        LoginResponse response = new LoginResponse(true, "Login exitoso");
        response.idusuario = user.getIdusuario();
        response.nombres = user.getNombres();
        response.apellidos = user.getApellidos();
        response.rol = user.getRol().getDescripcion();
        response.idTipoRol = user.getRol().getTiporol();
        
        
        
        return response;
    }
}
