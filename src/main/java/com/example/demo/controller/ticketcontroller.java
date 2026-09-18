package com.example.demo.controller;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.categoria;
import com.example.demo.model.ticket;
import com.example.demo.model.cliente;
import com.example.demo.repository.categoryRepository;
import com.example.demo.repository.ticketEstado;
import com.example.demo.repository.ticketPrioridad;
import com.example.demo.repository.ticketRepository;
import com.example.demo.repository.clienteRepository;

@RestController
@RequestMapping("/api")
public class ticketcontroller {

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private static final ZoneId OUTPUT_ZONE = ZoneId.of("America/Lima");

	@Autowired
	private ticketRepository ticketRepo;
	@Autowired
	private categoryRepository categoriaRepo;
	@Autowired
	private clienteRepository clienteRepo;

	// -------- GET lista --------
	@GetMapping("/ticket")
	public List<Map<String, Object>> listarTickets() {
		return ticketRepo.listarConJoins().stream().map(this::toMapRow).collect(Collectors.toList());
	}

	// -------- GET por id--------
	@GetMapping("/ticket/{id}")
	public Map<String, Object> obtenerTicket(@PathVariable Long id) {
		List<Object[]> rows = ticketRepo.obtenerConJoinsPorId(id);
		if (rows.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado");
		}
		return toMapRow(rows.get(0));
	}

	// -------- POST crear --------
	@PostMapping("/ticket")
	public ticket crearTicket(@RequestBody ticket nuevoTicket) { 
	    
		Long idUsuario = (nuevoTicket.getCliente() != null && 
	                      nuevoTicket.getCliente().getUsuario() != null) 
	                      ? nuevoTicket.getCliente().getUsuario().getIdusuario()
	                      : null;


	    if (idUsuario == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario logueado no proporcionado");
	    }

	    // Buscar cliente asociado al usuario logueado
	    cliente c = clienteRepo.buscarUsuarioPorId(idUsuario)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, 
	                "Cliente del usuario logueado no encontrado"));

	    // Validar categoría
	    categoria cat = categoriaRepo.findById(nuevoTicket.getCategoria().getIdcategoria())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no encontrada"));

	    // Validar prioridad y estado
	    ticketPrioridad.fromCodigo(nToInt(nuevoTicket.getPrioridad()));
	    ticketEstado.fromCodigo(nToInt(nuevoTicket.getEstado()));

	    // Asignar cliente y categoría al ticket
	    nuevoTicket.setCliente(c);
	    nuevoTicket.setCategoria(cat);
	    nuevoTicket.setFechacreacion(LocalDateTime.now());

	    Integer estado = nuevoTicket.getEstado();
	    if (estado == null || estado == 0) {
	        estado = ticketEstado.ABIERTO.getCodigo();
	    }
	    nuevoTicket.setEstado(estado);

	    return ticketRepo.save(nuevoTicket);
	}


	@PutMapping("/ticket/estado/{id}")
	public ticket actualizarEstado(@PathVariable("id") Long ticketId, @RequestBody Map<String, Object> payload) {
		Object estadoObj = payload.get("estado");
		if (estadoObj == null) {
			throw new RuntimeException("El campo 'estado' es requerido");
		}

		Integer estado = Integer.valueOf(estadoObj.toString());

		// Buscar el ticket
		ticket t = ticketRepo.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

		// Actualizar estado y fecha
		t.setEstado(estado);
		t.setFechacierre(LocalDateTime.now());

		return ticketRepo.save(t);
	}

	// -------- Helpers --------

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
		// Fallback para cadenas ISO-8601
		try {
			return OffsetDateTime.parse(v.toString()).atZoneSameInstant(OUTPUT_ZONE).format(FMT);
		} catch (Exception ignore) {
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

		String prioridadDesc = ticketPrioridad.fromCodigo(prioridadCode).getDescripcion();
		String estadoDesc = ticketEstado.fromCodigo(estadoCode).getDescripcion();

		LinkedHashMap<String, Object> m = new LinkedHashMap<>();
		m.put("idticket", idticket);
		m.put("titulo", titulo);
		m.put("descripcion", descripcion);

		// Raw + pretty
		m.put("fechacreacion", fmtDate(fechacreacion));
		m.put("fechacierre", fmtDate(fechacierre));

		m.put("prioridad", prioridadCode);
		m.put("prioridadDesc", prioridadDesc);
		m.put("estado", estadoCode);
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
