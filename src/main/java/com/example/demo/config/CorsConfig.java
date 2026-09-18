package com.example.demo.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")           // Rutas que se permitirán
              //  .allowedOrigins("http://localhost:4200", "https://*.onrender.com")  // Origen permitido (puedes agregar más URLs si lo necesitas)
		        .allowedOriginPatterns(
		                "http://localhost:4200",
		                "http://localhost:8080",
		                "https://apex-backend-api-z0oy.onrender.com",
		                "https://apex-frontend-upn.onrender.com",
		                "https://*.onrender.com"
		            )
        		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // Métodos permitidos
                .allowedHeaders("*")   // Todos los headers permitidos
                .allowCredentials(true)  // Permite enviar credenciales (cookies, authorization headers)
                .maxAge(3600);  // Maximo tiempo que la respuesta CORS es válida en caché (en segundos)
    }
}
