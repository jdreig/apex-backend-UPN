package com.example.demo;

import org.junit.jupiter.api.Test;
import com.example.demo.model.usuario;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    public void deberiaRechazarCorreoInvalido() {
        usuario u = new usuario();
        u.setCorreo("correoInvalidoSinArroba");

        assertFalse(u.esCorreoValido(), "El correo sin formato adecuado debe ser rechazado");
    }
    
    @Test
    public void deberiaRechazarDocumentoInvalido() {
        usuario u = new usuario();
        u.setDocumento("123"); // DNI corto inválido

        assertFalse(u.esDocumentoValido(), "El documento debe tener un formato válido de DNI");
    }
}