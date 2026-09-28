package com.example.demo.controller;

import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.usuario;
import com.example.demo.repository.usuarioRepository;
import jakarta.transaction.Transactional;
import com.example.demo.repository.clienteRepository;
import com.example.demo.repository.rolRepository;
import com.example.demo.repository.ticketAgenteRepository;
import com.example.demo.repository.ticketRepository;

@RestController
@RequestMapping("/api")
public class usuariocontroller {

    @Autowired private usuarioRepository usuarioRepo;
    @Autowired private clienteRepository clienteRepo;
    @Autowired private rolRepository rolRepo;
    @Autowired private ticketRepository ticketRepo;
    @Autowired private ticketAgenteRepository ticketAgenteRepo;

    // ---------- GET: lista (con rol y empresa) ----------
    @GetMapping("/usuario")
    public List<Map<String, Object>> listarUsuarios() {
        List<Object[]> rows = usuarioRepo.listarConRolYEmpresa();
        return rows.stream().map(this::mapUsuarioJoin).collect(Collectors.toList());
    }

    // ---------- GET: por id (con rol y empresa) ----------
    @GetMapping("/usuario/{id}")
    public Map<String, Object> obtenerUsuario(@PathVariable("id") Long id) {
        List<Object[]> rows = usuarioRepo.obtenerConRolYEmpresaPorId(id);
        if (rows.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return mapUsuarioJoin(rows.get(0));
    }

    // Crear nuevo usuario
    @PostMapping("/usuario")
    public usuario crearUsuario(@RequestBody Map<String, Object> payload) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Crear objeto usuario
        usuario nuevoUsuario = new usuario();
        nuevoUsuario.setNombreusuario((String) payload.get("nombreusuario"));
        nuevoUsuario.setContrasena(encoder.encode((String) payload.get("contrasena")));
        nuevoUsuario.setNombres((String) payload.get("nombres"));
        nuevoUsuario.setApellidos((String) payload.get("apellidos"));
        nuevoUsuario.setCorreo((String) payload.get("correo"));
        nuevoUsuario.setDocumento((String) payload.get("documento"));
        nuevoUsuario.setCelular((String) payload.get("celular"));
        nuevoUsuario.setEstado((Integer) payload.get("estado"));

        // 🔵 REFACTORIZACIÓN TDD 1: Validar formato de correo electrónico
        if (!nuevoUsuario.esCorreoValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato del correo electrónico es inválido");
        }

        //  REFACTORIZACIÓN Validar documento de identidad (DNI de 8 dígitos o CE)
        if (!nuevoUsuario.esDocumentoValido()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad debe ser un DNI de 8 dígitos o CE válido");
        }

        nuevoUsuario.setRol(rolRepo.findById(Long.valueOf(payload.get("idrol").toString()))
                          .orElseThrow(() -> new RuntimeException("Rol no encontrado")));

        usuario savedUsuario = usuarioRepo.save(nuevoUsuario);
        
        Object idEmpresaObj = payload.get("idempresa");
        if (idEmpresaObj != null) {
            Long idEmpresa = Long.valueOf(idEmpresaObj.toString());

            Integer count = clienteRepo.empresaExiste(idEmpresa);
            if (count == null || count == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empresa no existe");
            }
            clienteRepo.insertaEmpresaExiste(savedUsuario.getIdusuario(), idEmpresa);
        }
        return savedUsuario;
    }

    // Actualizar usuario existente
    @PutMapping("/usuario/{id}")
    public usuario actualizarUsuario(@PathVariable("id") Long id, @RequestBody Map<String, Object> payload) {
        // Buscar el usuario
        usuario existente = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Actualizar campos básicos
        existente.setNombreusuario((String) payload.get("nombreusuario"));
        existente.setNombres((String) payload.get("nombres"));
        existente.setApellidos((String) payload.get("apellidos"));
        existente.setDocumento((String) payload.get("documento"));
        existente.setCelular((String) payload.get("celular"));
        existente.setCorreo((String) payload.get("correo"));
        existente.setEstado((Integer) payload.get("estado"));

        // 🔵 REFACTORIZACIÓN TDD 1: Validar correo en actualización
        if (!existente.esCorreoValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico actualizado no es válido");
        }

     //   // 🔵 REFACTORIZACIÓN TDD 3: Validar número de celular (9 dígitos comenzando con 9)
       // if (!existente.esCelularValido()) {
        //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El celular debe empezar con 9 y constar de 9 dígitos");
        //}

        // Actualizar contraseña si viene
        String pass = (String) payload.get("contrasena");
        if (pass != null && !pass.isBlank()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            existente.setContrasena(encoder.encode(pass));
        }

        // Actualizar cliente/empresa si se envía idempresa
        Object idEmpresaObj = payload.get("idempresa");
        if (idEmpresaObj != null) {
            Long idEmpresa = Long.valueOf(idEmpresaObj.toString());
            
            // Validar que exista la empresa
            Integer count = clienteRepo.empresaExiste(idEmpresa);
            if (count == null || count == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empresa no existe");
            }
        }

        // Guardar cambios en el usuario
        return usuarioRepo.save(existente);
    }

    // ---------- Helper: mapea fila del SELECT a Map ----------
    private Map<String, Object> mapUsuarioJoin(Object[] row) {
        return Map.of(
            "idusuario",     row[0],
            "nombreusuario", row[1],
            "nombres",       row[2],
            "apellidos",     row[3],
            "correo",        row[4],
            "documento",     row[5],
            "celular",       row[6],
            "rol",           row[7],
            "empresa",       row[8],
            "tiporol",       row[9]
        );
    }
    
    @DeleteMapping("/usuario/{id}")
    @Transactional
    public ResponseEntity<?> eliminarUsuario(@PathVariable("id") Long id) {
        
        // 1) Verificar existencia primero para cargar el objeto usuario
        usuario existente = usuarioRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // 🔵 REFACTORIZACIÓN TDD 4: Protección delegada al modelo mediante esEliminable()
       // if (!existente.esEliminable()) {
         //   var body = new HashMap<String, Object>();
           // body.put("ok", false);
            //body.put("mensaje", "No se puede eliminar al administrador principal de Apex");
            //return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body); // 403
        //}

        // 2) ¿Tiene tickets vinculados (via cliente)?
        long nTickets = ticketRepo.countByUsuarioId(id);
        if (nTickets > 0) {
            // Empresas vinculadas a esos tickets (para el mensaje)
            var empresas = ticketRepo.empresasConTicketsPorUsuario(id);

            var body = new HashMap<String, Object>();
            body.put("ok", false);
            body.put("mensaje", "No se puede eliminar: el usuario tiene tickets vinculados");
            body.put("tickets", nTickets);
            body.put("empresas", empresas); 

            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        } else {
            long nTicketsAgente = ticketAgenteRepo.existByUsuarioAgenteId(id);
            if (nTicketsAgente > 0) {         
                var body = new HashMap<String, Object>();
                body.put("ok", false);
                body.put("mensaje", "No se puede eliminar: el usuario, es parte de las atenciones vinculadas");
                body.put("empresas", null); 

                // 409 Conflict: está referenciado
                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
            }
        }
        

        // 3) Sin tickets: borrar filas de cliente (si existieran)
        clienteRepo.deleteByUsuarioId(id);

        // 4) Borrar usuario
        usuarioRepo.delete(existente);

        var bodyOk = new HashMap<String, Object>();
        bodyOk.put("ok", true);
        bodyOk.put("mensaje", "Usuario eliminado correctamente");
        bodyOk.put("idusuario", id);

        return ResponseEntity.ok(bodyOk);
    }
}