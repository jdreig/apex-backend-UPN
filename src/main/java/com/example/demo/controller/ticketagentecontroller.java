package com.example.demo.controller;

import com.example.demo.model.ticketAgente;
import com.example.demo.model.ticket;
import com.example.demo.model.usuario;
import com.example.demo.repository.ticketAgenteRepository;
import com.example.demo.repository.ticketRepository;
import com.example.demo.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ticketagentecontroller {

    @Autowired private ticketAgenteRepository ticketAgenteRepository;
    @Autowired private ticketRepository ticketRepository;
    @Autowired private usuarioRepository usuarioRepository;

	    // Método GET: Obtener todos los ticketAgente con los datos de ticket y usuario
	    @GetMapping("/ticketagente")
	    public List<ticketAgente> getAllTicketAgentes() {
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

	        // Retornar la respuesta como un ResponseEntity
	        return ResponseEntity.ok(response);
	    }
	

	    @PostMapping("/ticketagente")
	    public ResponseEntity<ticketAgente> createTicketAgenteResponse(@RequestBody ticketAgente respuestaTicketAgente) {

	        // Buscar el ticket correspondiente por ID
	        Optional<ticket> ticketOpt = ticketRepository.findById(respuestaTicketAgente.getTicket());
	        if (!ticketOpt.isPresent()) {
	            return ResponseEntity.notFound().build();
	        }
	        Optional<usuario> usuarioOpt = usuarioRepository.findById(respuestaTicketAgente.getUsuario());
	        if (!usuarioOpt.isPresent()) {
	            return ResponseEntity.notFound().build();
	        }
	        ticketAgente nuevoTicketAgente = new ticketAgente();
	        
	        nuevoTicketAgente.setTicket(respuestaTicketAgente.getTicket());  
	        nuevoTicketAgente.setUsuario(respuestaTicketAgente.getUsuario());
	        nuevoTicketAgente.setRespuesta(respuestaTicketAgente.getRespuesta()); 
	        nuevoTicketAgente.setFechaRespuesta(LocalDateTime.now());  // Fecha actual

	        ticketAgente guardarTicketAgente = ticketAgenteRepository.save(nuevoTicketAgente);

	        // Retorna el ticketAgente guardado
	        return ResponseEntity.ok(guardarTicketAgente);
	    }
}
