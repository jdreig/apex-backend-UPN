package com.example.demo;

import org.junit.jupiter.api.Test;
import com.example.demo.model.ticket;
import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    @Test
    public void deberiaRechazarTicketSinTitulo() {
        ticket t = new ticket();
        t.setTitulo(" "); // Título en blanco
        t.setDescripcion("Problema de conexión en la red principal");

        assertFalse(t.esValido(), "El ticket debe ser rechazado si el título está vacío");
    }
}