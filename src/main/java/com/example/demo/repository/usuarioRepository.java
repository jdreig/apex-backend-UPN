package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.usuario;


public interface usuarioRepository extends JpaRepository<usuario, Long> {

    @Query(value = """
    SELECT  u.idusuario, u.nombreusuario,
            u.nombres, u.apellidos,
            u.correo, u.documento,
            u.celular,
            r.descripcion as rol,
            COALESCE(e.razonsocial, 'Empleado Apex') as empresa,
            r.tiporol as tiporol
        FROM usuario u
        LEFT JOIN rol r ON u.idrol = r.idrol
        LEFT JOIN cliente cl ON u.idusuario = cl.idusuario
		LEFT JOIN empresa e ON e.idempresa = cl.idempresa
        """, nativeQuery = true)
    List<Object[]> listarConRolYEmpresa();

    @Query(value = """
    SELECT  u.idusuario, u.nombreusuario,
            u.nombres, u.apellidos,
            u.correo, u.documento,
            u.celular,
            r.descripcion as rol,
            COALESCE(e.razonsocial, 'Empleado Apex') as empresa,
            r.tiporol as tiporol
        FROM usuario u
        LEFT JOIN rol r    ON u.idrol = r.idrol
        LEFT JOIN cliente cl ON u.idusuario = cl.idusuario
		LEFT JOIN empresa e ON e.idempresa = cl.idempresa
        WHERE u.idusuario = :id
        """, nativeQuery = true)
    List<Object[]> obtenerConRolYEmpresaPorId(@Param("id") Long id);
    
    Optional<usuario> findByNombreusuario(String nombreusuario);

}
