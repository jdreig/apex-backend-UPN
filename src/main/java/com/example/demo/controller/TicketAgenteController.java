package com.example.demo.controller;

import com.example.demo.dto.TicketAgenteDto;
import com.example.demo.model.TicketAgente;
import com.example.demo.model.Ticket;
import com.example.demo.model.Usuario;
import com.example.demo.repository.TicketAgenteRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TicketAgenteController {

    private final TicketAgenteRepository ticketAgenteRepository;
    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor (Requerido por SonarQube en lugar de @Autowired)
    public TicketAgenteController(TicketAgenteRepository ticketAgenteRepository,
                                  TicketRepository ticketRepository,
                                  UsuarioRepository usuarioRepository) {
        this.ticketAgenteRepository = ticketAgenteRepository;
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Método GET: Obtener todos los ticketAgente con los datos de ticket y usuario
    @GetMapping("/ticketagente")
    public List<TicketAgente> getAllTicketAgentes() {
        return ticketAgenteRepository.findAll();
    }

    // Método GET: Obtener los datos del ticketAgente por ID de ticket
    @GetMapping("/ticketagente/{idticket}")
    public ResponseEntity<List<Map<String, Object>>> getTicketAgenteById(@PathVariable("idticket") Long idticket) {

        List<Object[]> result = ticketAgenteRepository.generarRespuestasTicket(idticket);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : result) {
            Map<String, Object> rowMap = new HashMap<>();
            rowMap.put("idticket", row[0]);
            rowMap.put("titulo", row[1]);
            rowMap.put("descripcion", row[2]);
            rowMap.put("fechacreacion", row[3]);
            rowMap.put("fechacierre", row[4]);
            rowMap.put("cliente_nombres", row[5]);
            rowMap.put("cliente_apellidos", row[6]);
            rowMap.put("cliente_correo", row[7]);
            rowMap.put("cliente_celular", row[8]);
            rowMap.put("usuario_nombres", row[9]);
            rowMap.put("usuario_apellidos", row[10]);
            rowMap.put("cliente_documento", row[11]);
            rowMap.put("correo", row[12]);
            rowMap.put("celular", row[13]);
            rowMap.put("respuesta", row[14]);
            rowMap.put("documento", row[15]);
            rowMap.put("fechaRespuesta", row[16]);
            rowMap.put("estado", row[17]);

            response.add(rowMap);
        }

        return ResponseEntity.ok(response);
    }

    // Método POST utilizando el DTO para evitar el error de SonarQube
    @PostMapping("/ticketagente")
    public ResponseEntity<TicketAgente> createTicketAgenteResponse(@RequestBody TicketAgenteDto dto) {

        // Buscar el ticket correspondiente por ID
        Optional<Ticket> ticketOpt = ticketRepository.findById(dto.getIdticket());
        if (!ticketOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getIdusuario());
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TicketAgente nuevoTicketAgente = new TicketAgente();
        nuevoTicketAgente.setTicket(dto.getIdticket());  
        nuevoTicketAgente.setUsuario(dto.getIdusuario());
        nuevoTicketAgente.setRespuesta(dto.getRespuesta()); 
        
        // Solución a la advertencia de zona horaria
        nuevoTicketAgente.setFechaRespuesta(LocalDateTime.now(ZoneId.systemDefault()));

        TicketAgente guardarTicketAgente = ticketAgenteRepository.save(nuevoTicketAgente);

        return ResponseEntity.ok(guardarTicketAgente);
    }
}