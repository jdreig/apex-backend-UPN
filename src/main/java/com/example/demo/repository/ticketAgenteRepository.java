package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.ticketAgente;

public interface ticketAgenteRepository extends  JpaRepository<ticketAgente, Long>{
	   
	@Query(value = """
				SELECT t.idticket,t.titulo, t.descripcion,
				 t.fechaCreacion,
				 t.fechaCierre,
				u2.nombres AS cliente_nombres,u2.apellidos AS cliente_apellidos,
				u2.correo AS cliente_correo,  u2.celular AS cliente_celular,  u.nombres AS usuario_nombres, u.apellidos AS usuario_apellidos, u.documento, 
				u.correo, u.celular, ta.respuesta, u2.documento,     
				ta.fecharespuesta, t.estado

				FROM 
				    TicketAgente ta 
				    INNER JOIN Ticket t ON ta.idticket = t.idticket
				    INNER JOIN Usuario u ON u.idusuario = ta.idusuario
				    INNER JOIN Cliente c ON c.idcliente = t.idcliente
				    INNER JOIN Usuario u2 ON u2.idusuario = c.idusuario
					WHERE 
				    t.idticket =:id
			        """, nativeQuery = true)
			    List<Object[]> generarRespuestasTicket(@Param("id") Long id);
			    

			@Query("""
					select distinct idusuario
			      from ticketAgente where idusuario = :idUsuario
			        """)
			        long existByUsuarioAgenteId(long idUsuario);
}
