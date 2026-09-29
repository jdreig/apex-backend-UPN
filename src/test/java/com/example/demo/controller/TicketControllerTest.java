package com.example.demo.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.JwtUtilService;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private JwtUtilService jwtUtilService; // Necesario si tu seguridad filtra peticiones

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deberiaListarTicketsCorrectamente() throws Exception {
        // Simulamos que el repositorio retorna una lista vacía de objetos joined
        given(ticketRepository.listarConJoins()).willReturn(Collections.emptyList());

        // Ejecutamos la petición GET al endpoint de tickets
        mockMvc.perform(get("/api/ticket"))
               .andExpect(status().isOk());
    }
}