package com.example.demo;

import org.springframework.boot.CommandLineRunner; 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApexBack1Application {

    public static void main(String[] args) {
        SpringApplication.run(ApexBack1Application.class, args);
    }
    
    @Bean
    public CommandLineRunner imprimirClaveAdmin() {
        return args -> {
            // Lámpar de prueba eliminada para evitar código muerto
        };
    }
}