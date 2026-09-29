package com.example.demo.controller;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Categoria;
import com.example.demo.model.Ticket;
import com.example.demo.model.Cliente;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TicketEstado;
import com.example.demo.repository.TicketPrioridad;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.ClienteRepository;

@RestController
@RequestMapping("/api")
public class TicketController {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final ZoneId OUTPUT_ZONE = ZoneId.of("America/Lima");

    // Constantes para evitar duplicación de literales detectados por SonarQube
    private static final String FIELD_ESTADO = "estado";
    private static final String FIELD_ID_USUARIO = "idusuario";
    private static final String FIELD_ID_CATEGORIA = "idcategoria";

    private final TicketRepository ticketRepo;
    private final CategoryRepository categoriaRepo;
    private final ClienteRepository clienteRepo;

    public TicketController(TicketRepository ticketRepo,
                            CategoryRepository categoriaRepo,
                            ClienteRepository clienteRepo) {
        this.ticketRepo = ticketRepo;
        this.categoriaRepo = categoriaRepo;
        this.clienteRepo = clienteRepo;
    }

    @GetMapping("/ticket")
    public List<Map<String, Object>> listarTickets() {
        return ticketRepo.listarConJoins().stream().map(this::toMapRow).toList();
    }

    @GetMapping("/ticket/{id}")
    public Map<String, Object> obtenerTicket(@PathVariable Long id) {
        List<Object[]> rows = ticketRepo.obtenerConJoinsPorId(id);
        if (rows.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado");
        }
        return toMapRow(rows.get(0));
    }

    @PostMapping("/ticket")
    public Ticket crearTicket(@RequestBody Map<String, Object> payload) {    
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setTitulo((String) payload.get("titulo"));
        nuevoTicket.setDescripcion((String) payload.get("descripcion"));

        Object prioridadObj = payload.get("prioridad");
        if (prioridadObj != null) {
            nuevoTicket.setPrioridad(Integer.valueOf(prioridadObj.toString()));
        }

        Object estadoObj = payload.get(FIELD_ESTADO);
        if (estadoObj != null) {
            nuevoTicket.setEstado(Integer.valueOf(estadoObj.toString()));
        }

        if (!nuevoTicket.esValido()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "El ticket debe incluir un título válido y una descripción detallada (mínimo 10 caracteres)"
            );
        }

        Long idUsuario = extraerIdUsuario(payload);
        if (idUsuario == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario logueado no proporcionado");
        }

        Cliente c = clienteRepo.buscarUsuarioPorId(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Cliente del usuario logueado no encontrado"));

        Long idCategoria = extraerIdCategoria(payload);
        if (idCategoria == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no encontrada");
        }

        Categoria cat = categoriaRepo.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no encontrada"));

        TicketPrioridad.fromCodigo(nToInt(nuevoTicket.getPrioridad()));
        TicketEstado.fromCodigo(nToInt(nuevoTicket.getEstado()));

        nuevoTicket.setCliente(c);
        nuevoTicket.setCategoria(cat);
        nuevoTicket.setFechacreacion(LocalDateTime.now(OUTPUT_ZONE));

        Integer estado = nuevoTicket.getEstado();
        if (estado == null || estado == 0) {
            estado = TicketEstado.ABIERTO.getCodigo();
        }
        nuevoTicket.setEstado(estado);

        return ticketRepo.save(nuevoTicket);
    }

    // Método auxiliar para reducir la complejidad cognitiva de crearTicket
    private Long extraerIdUsuario(Map<String, Object> payload) {
        Object idUsuarioObj = payload.get(FIELD_ID_USUARIO);
        if (idUsuarioObj != null) {
            return Long.valueOf(idUsuarioObj.toString());
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> clienteMap = (Map<String, Object>) payload.get("cliente");
        if (clienteMap != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> usuarioMap = (Map<String, Object>) clienteMap.get("usuario");
            if (usuarioMap != null && usuarioMap.get(FIELD_ID_USUARIO) != null) {
                return Long.valueOf(usuarioMap.get(FIELD_ID_USUARIO).toString());
            }
        }
        return null;
    }

    // Método auxiliar para reducir la complejidad cognitiva de crearTicket
    private Long extraerIdCategoria(Map<String, Object> payload) {
        Object categoriaMapObj = payload.get("categoria");
        if (categoriaMapObj instanceof Map) {
            Object idCat = ((Map<?, ?>) categoriaMapObj).get(FIELD_ID_CATEGORIA);
            if (idCat != null) {
                return Long.valueOf(idCat.toString());
            }
        } else if (payload.get(FIELD_ID_CATEGORIA) != null) {
            return Long.valueOf(payload.get(FIELD_ID_CATEGORIA).toString());
        }
        return null;
    }

    @PutMapping("/ticket/estado/{id}")
    public Ticket actualizarEstado(@PathVariable("id") Long ticketId, @RequestBody Map<String, Object> payload) {
        Object estadoObj = payload.get(FIELD_ESTADO);
        if (estadoObj == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'estado' es requerido");
        }

        Integer estado = Integer.valueOf(estadoObj.toString());

        Ticket t = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado"));

        t.setEstado(estado);
        t.setFechacierre(LocalDateTime.now(OUTPUT_ZONE));

        return ticketRepo.save(t);
    }

    private static String fmtDate(Object v) {
        if (v == null)
            return null;

        if (v instanceof LocalDateTime ldt) {
            return ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(OUTPUT_ZONE).format(FMT);
        }
        if (v instanceof OffsetDateTime odt) {
            return odt.atZoneSameInstant(OUTPUT_ZONE).format(FMT);
        }
        if (v instanceof ZonedDateTime zdt) {
            return zdt.withZoneSameInstant(OUTPUT_ZONE).format(FMT);
        }
        if (v instanceof Timestamp ts) {
            LocalDateTime ldt = ts.toLocalDateTime();
            return ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(OUTPUT_ZONE).format(FMT);
        }

        try {
            return OffsetDateTime.parse(v.toString()).atZoneSameInstant(OUTPUT_ZONE).format(FMT);
        } catch (Exception _) {
            // Se ignora intencionalmente si el formato no es parseable, retornando el string plano al final
        }
        return v.toString();
    }

    private Map<String, Object> toMapRow(Object[] r) {
        Long idticket = toLong(r[0]);
        String titulo = (String) r[1];
        String descripcion = (String) r[2];
        Object fechacreacion = r[3];
        Object fechacierre = r[4];
        int prioridadCode = nToInt(r[5]);
        int estadoCode = nToInt(r[6]);
        String nombres = (String) r[7];
        String apellidos = (String) r[8];
        String empresa = (String) r[9];
        String categoria = (String) r[10];

        String prioridadDesc = TicketPrioridad.fromCodigo(prioridadCode).getDescripcion();
        String estadoDesc = TicketEstado.fromCodigo(estadoCode).getDescripcion();

        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("idticket", idticket);
        m.put("titulo", titulo);
        m.put("descripcion", descripcion);
        m.put("fechacreacion", fmtDate(fechacreacion));
        m.put("fechacierre", fmtDate(fechacierre));
        m.put("prioridad", prioridadCode);
        m.put("prioridadDesc", prioridadDesc);
        m.put(FIELD_ESTADO, estadoCode); // Utilizando la constante definida
        m.put("estadoDesc", estadoDesc);
        m.put("nombres", nombres);
        m.put("apellidos", apellidos);
        m.put("empresa", empresa);
        m.put("categoria", categoria);
        return m;
    }

    private static int nToInt(Object n) {
        return (n == null) ? 0 : ((Number) n).intValue();
    }

    private static Long toLong(Object n) {
        return (n == null) ? null : ((Number) n).longValue();
    }
}