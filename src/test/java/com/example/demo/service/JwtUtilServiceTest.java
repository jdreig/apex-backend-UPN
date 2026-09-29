package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilServiceTest {

    private JwtUtilService jwtUtilService;

    @BeforeEach
    void setUp() {
        jwtUtilService = new JwtUtilService();
        // Inyectamos el valor secreto que usa la anotación @Value
        ReflectionTestUtils.setField(jwtUtilService, "jwtSecretKey", "TExBVkVfTVVZX1NFQ1JFVEE9VExBVkVfTVVZX1NFQ1JFVEE=");
    }

    @Test
    void testGenerarYValidarToken() {
        UserDetails userDetails = new User(
            "admin", 
            "admin", 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtUtilService.generateToken(userDetails);
        assertNotNull(token);

        String username = jwtUtilService.extractUsername(token);
        assertEquals("admin", username);

        assertTrue(jwtUtilService.validateToken(token, userDetails));
    }
}