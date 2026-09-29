package com.example.demo.controller;

import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.TicketAgenteRepository;
import com.example.demo.repository.TicketRepository;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private static final String KEY_MENSAJE = "mensaje";
    private static final String KEY_OK = "ok";

    private static final String FIELD_NOMBREUSUARIO = "nombreusuario";
    private static final String FIELD_NOMBRES = "nombres";
    private static final String FIELD_APELLIDOS = "apellidos";
    private static final String FIELD_CORREO = "correo";
    private static final String FIELD_DOCUMENTO = "documento";
    private static final String FIELD_CELULAR = "celular";

    private final UsuarioRepository usuarioRepo;
    private final ClienteRepository clienteRepo;
    private final RolRepository rolRepo;
    private final TicketRepository ticketRepo;
    private final TicketAgenteRepository ticketAgenteRepo;

    // Inyección por constructor requerida por SonarQube
    public UsuarioController(UsuarioRepository usuarioRepo,
                             ClienteRepository clienteRepo,
                             RolRepository rolRepo,
                             TicketRepository ticketRepo,
                             TicketAgenteRepository ticketAgenteRepo) {
        this.usuarioRepo = usuarioRepo;
        this.clienteRepo = clienteRepo;
        this.rolRepo = rolRepo;
        this.ticketRepo = ticketRepo;
        this.ticketAgenteRepo = ticketAgenteRepo;
    }

    // ---------- GET: lista (con rol y empresa) ----------
    @GetMapping("/usuario")
    public List<Map<String, Object>> listarUsuarios() {
        List<Object[]> rows = usuarioRepo.listarConRolYEmpresa();
        return rows.stream().map(this::mapUsuarioJoin).toList();
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
    public Usuario crearUsuario(@RequestBody Map<String, Object> payload) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreusuario((String) payload.get(FIELD_NOMBREUSUARIO));
        nuevoUsuario.setContrasena(encoder.encode((String) payload.get("contrasena")));
        nuevoUsuario.setNombres((String) payload.get(FIELD_NOMBRES));
        nuevoUsuario.setApellidos((String) payload.get(FIELD_APELLIDOS));
        nuevoUsuario.setCorreo((String) payload.get(FIELD_CORREO));
        nuevoUsuario.setDocumento((String) payload.get(FIELD_DOCUMENTO));
        nuevoUsuario.setCelular((String) payload.get(FIELD_CELULAR));
        nuevoUsuario.setEstado((Integer) payload.get("estado"));

        if (!nuevoUsuario.esCorreoValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato del correo electrónico es inválido");
        }

        if (!nuevoUsuario.esDocumentoValido()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad debe ser un DNI de 8 dígitos o CE válido");
        }

        nuevoUsuario.setRol(rolRepo.findById(Long.valueOf(payload.get("idrol").toString()))
                          .orElseThrow(() -> new RuntimeException("Rol no encontrado")));

        Usuario savedUsuario = usuarioRepo.save(nuevoUsuario);
        
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
    public Usuario actualizarUsuario(@PathVariable("id") Long id, @RequestBody Map<String, Object> payload) {
        Usuario existente = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        existente.setNombreusuario((String) payload.get(FIELD_NOMBREUSUARIO));
        existente.setNombres((String) payload.get(FIELD_NOMBRES));
        existente.setApellidos((String) payload.get(FIELD_APELLIDOS));
        existente.setDocumento((String) payload.get(FIELD_DOCUMENTO));
        existente.setCelular((String) payload.get(FIELD_CELULAR));
        existente.setCorreo((String) payload.get(FIELD_CORREO));
        existente.setEstado((Integer) payload.get("estado"));

        if (!existente.esCorreoValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico actualizado no es válido");
        }

        String pass = (String) payload.get("contrasena");
        if (pass != null && !pass.isBlank()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            existente.setContrasena(encoder.encode(pass));
        }

        Object idEmpresaObj = payload.get("idempresa");
        if (idEmpresaObj != null) {
            Long idEmpresa = Long.valueOf(idEmpresaObj.toString());
            
            Integer count = clienteRepo.empresaExiste(idEmpresa);
            if (count == null || count == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empresa no existe");
            }
        }

        return usuarioRepo.save(existente);
    }

    // ---------- Helper: mapea fila del SELECT a Map (Corregido con las constantes) ----------
    private Map<String, Object> mapUsuarioJoin(Object[] row) {
        return Map.of(
            "idusuario",           row[0],
            FIELD_NOMBREUSUARIO,   row[1],
            FIELD_NOMBRES,         row[2],
            FIELD_APELLIDOS,       row[3],
            FIELD_CORREO,          row[4],
            FIELD_DOCUMENTO,       row[5],
            FIELD_CELULAR,         row[6],
            "rol",                 row[7],
            "empresa",             row[8],
            "tiporol",             row[9]
        );
    }
    
    @DeleteMapping("/usuario/{id}")
    @Transactional
    public ResponseEntity<Object> eliminarUsuario(@PathVariable("id") Long id) {
        
        Usuario existente = usuarioRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        long nTickets = ticketRepo.countByUsuarioId(id);
        if (nTickets > 0) {
            var empresas = ticketRepo.empresasConTicketsPorUsuario(id);

            var body = new HashMap<String, Object>();
            body.put(KEY_OK, false);
            body.put(KEY_MENSAJE, "No se puede eliminar: el usuario tiene tickets vinculados");
            body.put("tickets", nTickets);
            body.put("empresas", empresas); 

            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        } else {
            long nTicketsAgente = ticketAgenteRepo.existByUsuarioAgenteId(id);
            if (nTicketsAgente > 0) {         
                var body = new HashMap<String, Object>();
                body.put(KEY_OK, false);
                body.put(KEY_MENSAJE, "No se puede eliminar: el usuario, es parte de las atenciones vinculadas");
                body.put("empresas", null); 

                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
            }
        }

        clienteRepo.deleteByUsuarioId(id);
        usuarioRepo.delete(existente);

        var bodyOk = new HashMap<String, Object>();
        bodyOk.put(KEY_OK, true);
        bodyOk.put(KEY_MENSAJE, "Usuario eliminado correctamente");
        bodyOk.put("idusuario", id);

        return ResponseEntity.ok(bodyOk);
    }
}