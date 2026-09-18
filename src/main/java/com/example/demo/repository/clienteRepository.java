package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.cliente;

import jakarta.transaction.Transactional;

public interface clienteRepository extends JpaRepository<cliente, Long>{
	 	
		@Query(value = """
		    SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
		    FROM empresa
		    WHERE idempresa = :idEmpresa
		""", nativeQuery = true)
		Integer empresaExiste(@Param("idEmpresa") Long idEmpresa);
		
		@Query("SELECT c FROM cliente c WHERE c.usuario.idusuario = :idUsuario")
		Optional<cliente> buscarUsuarioPorId(@Param("idUsuario") Long idUsuario);
		
		@Query("SELECT c FROM cliente c WHERE c.usuario.idusuario = :idUsuario AND c.empresa.idempresa = :idEmpresa")
		Optional<cliente> findByUsuarioAndEmpresa(@Param("idUsuario") Long idUsuario, @Param("idEmpresa") Long idEmpresa);

		@Modifying
	    @Transactional
	    @Query(value = "UPDATE Cliente SET idempresa = :idempresa WHERE idusuario = :idusuario", nativeQuery = true)
	    int actualizarEmpresaPorId(@Param("idempresa") Long idempresa, @Param("idusuario") Long idusuario);
	
		
	    @Modifying
	    @Transactional
	    @Query(value = """
	        INSERT INTO cliente (idusuario, idempresa)
	        SELECT :idUsuario, :idEmpresa
	        FROM empresa e
	        WHERE e.idempresa = :idEmpresa
	        """, nativeQuery = true)
	    void insertaEmpresaExiste(Long idUsuario, Long idEmpresa);
	    
	    List<cliente> findByUsuario_Idusuario(Long idUsuario);

	    @Modifying
	    @Query("""
	        delete from cliente c
	        where c.usuario.idusuario = :idUsuario
	    """)
	    void deleteByUsuarioId(long idUsuario);
}
