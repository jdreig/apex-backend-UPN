package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.TicketAgente;

public interface TicketAgenteRepository extends JpaRepository<TicketAgente, Long> {
    
    @Query(value = """
            SELECT t.idticket, t.titulo, t.descripcion,
            t.fechaCreacion,
            t.fechaCierre,
            u2.nombres AS cliente_nombres, u2.apellidos AS cliente_apellidos,
            u2.correo AS cliente_correo, u2.celular AS cliente_celular, u.nombres AS usuario_nombres, u.apellidos AS usuario_apellidos, u.documento,
            u.correo, u.celular, ta.respuesta, u2.documento,
            ta.fecharespuesta, t.estado
            FROM ticketAgente ta
            INNER JOIN ticket t ON ta.idticket = t.idticket
            INNER JOIN usuario u ON u.idusuario = ta.idusuario
            INNER JOIN cliente c ON c.idcliente = t.idcliente
            INNER JOIN usuario u2 ON u2.idusuario = c.idusuario
            WHERE t.idticket = :id
            """, nativeQuery = true)
    List<Object[]> generarRespuestasTicket(@Param("id") Long id);

    @Query(value = """
            SELECT DISTINCT idusuario 
            FROM ticketAgente 
            WHERE idusuario = :idUsuario
            """, nativeQuery = true)
    long existByUsuarioAgenteId(@Param("idUsuario") long idUsuario);
}