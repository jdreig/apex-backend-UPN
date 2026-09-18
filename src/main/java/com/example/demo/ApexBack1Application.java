package com.example.demo;

import org.springframework.boot.CommandLineRunner; 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ApexBack1Application {

	public static void main(String[] args) {
		SpringApplication.run(ApexBack1Application.class, args);
	}
	
	@Bean
    public CommandLineRunner imprimirClaveAdmin() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashAdmin = encoder.encode("admin");

            System.out.println("\n==================================================");
            System.out.println(">>> HASH BCRYPT PARA 'admin': " + hashAdmin);
            System.out.println("==================================================\n");
        };

 }
}