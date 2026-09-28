package com.example.demo;

import org.junit.jupiter.api.Test;
import com.example.demo.model.empresa;
import static org.junit.jupiter.api.Assertions.*;

public class EmpresaTest {

    @Test
    public void deberiaRechazarRucInvalidoPorLongitud() {
        empresa e = new empresa();
        e.setRuc("12345678"); // RUC inválido de 8 dígitos
        
        assertFalse(e.esRucValido(), "El RUC debe tener exactamente 11 dígitos");
    }

    @Test
    public void deberiaAceptarRucValido() {
        empresa e = new empresa();
        e.setRuc("20123456789"); // RUC válido de 11 dígitos (SUNAT)
        
        assertTrue(e.esRucValido(), "El RUC de 11 dígitos debe ser aceptado");
    }
}