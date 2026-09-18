package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.ticket;

public interface ticketRepository extends JpaRepository<ticket, Long> {
    @Query(value = """
        SELECT 
            t.idticket,                 -- 0
            t.titulo,                   -- 1
            t.descripcion,              -- 2
            t.fechacreacion,            -- 3
            t.fechacierre,              -- 4
            t.prioridad,                -- 5 (int)
            t.estado,                   -- 6 (int)
            u.nombres,                  -- 7
            u.apellidos,                -- 8
            e.razonsocial AS empresa,   -- 9
            c.descripcion AS categoria  -- 10
        FROM ticket t
		INNER JOIN cliente cl  ON t.idcliente = cl.idcliente
		INNER JOIN empresa e   ON cl.idempresa = e.idempresa
		INNER JOIN usuario u  ON cl.idusuario = u.idusuario
        INNER JOIN categoria c ON t.idcategoria = c.idcategoria
        ORDER BY t.idticket DESC
        """, nativeQuery = true)
    List<Object[]> listarConJoins();

    @Query(value = """
        SELECT 
            t.idticket, t.titulo, t.descripcion, t.fechacreacion, t.fechacierre,
            t.prioridad, t.estado, u.nombres, u.apellidos,
            e.razonsocial AS empresa,
            c.descripcion AS categoria
        FROM ticket t
		INNER JOIN cliente cl  ON t.idcliente = cl.idcliente
		INNER JOIN empresa e   ON cl.idempresa = e.idempresa
		INNER JOIN usuario u  ON cl.idusuario = u.idusuario
        INNER JOIN categoria c ON t.idcategoria = c.idcategoria
        WHERE t.idticket = :id
        """, nativeQuery = true)
    List<Object[]> obtenerConJoinsPorId(@Param("id") Long id);
    

    @Query("""
        select count(t)
    from ticket t where t.cliente.usuario.idusuario = :idUsuario
    """)
    long countByUsuarioId(long idUsuario);

    @Query("""
        select distinct c.empresa.razonsocial
        from ticket t inner join t.cliente c where c.usuario.idusuario = :idUsuario
    """)
    List<String> empresasConTicketsPorUsuario(long idUsuario);

}
