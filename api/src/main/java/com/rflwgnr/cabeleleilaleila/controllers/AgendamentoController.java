package com.rflwgnr.cabeleleilaleila.controllers;

import com.rflwgnr.cabeleleilaleila.data.dto.agendamentos.AgendamentoDTO;
import com.rflwgnr.cabeleleilaleila.data.dto.agendamentos.AgendamentoResponseDTO;
import com.rflwgnr.cabeleleilaleila.data.dto.agendamentos.AtualizarStatusAgendamentosDTO;
import com.rflwgnr.cabeleleilaleila.model.Agendamento;
import com.rflwgnr.cabeleleilaleila.services.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listarTodosAgendamentos() {
        List<Agendamento> agendamentos = service.buscarTodosAgendamentos();
        List<AgendamentoResponseDTO> response = agendamentos.stream()
                .map(service::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentoPorUsuario(@PathVariable Long id) {
        List<Agendamento> agendamentos = service.buscarPorUsuarioId(id);
        List<AgendamentoResponseDTO> response = agendamentos.stream()
                .map(service::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/futuros/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentosFuturosPorUsuario(@PathVariable Long id) {
        List<Agendamento> agendamentos = service.buscarFuturosPorUsuarioId(id);
        List<AgendamentoResponseDTO> response = agendamentos.stream()
                .map(service::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criarAgendamento(@RequestBody AgendamentoDTO dto, Principal principal) {
        String username = principal.getName();
        Agendamento agendamentoCriado = service.criarAgendamento(dto, username);
        AgendamentoResponseDTO resposta = service.toResponseDTO(agendamentoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PostMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> criarAgendamentoOutroUsuario(@PathVariable Long id, @RequestBody AgendamentoDTO dto, Principal principal) {
        String username = principal.getName();
        Agendamento agendamentoCriado = service.criarAgendamentoOutroUsuario(dto, username, id);
        AgendamentoResponseDTO resposta = service.toResponseDTO(agendamentoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PutMapping("/{usuarioId}/{agendamentoId}")
    public ResponseEntity<AgendamentoResponseDTO> atualizarAgendamento(@PathVariable Long usuarioId, @PathVariable Long agendamentoId,
                                                            @RequestBody AgendamentoDTO dto, Principal principal) {
        String username = principal.getName();
        Agendamento agendamentoAtualizado = service.atualizarAgendamento(dto, username, usuarioId, agendamentoId);
        AgendamentoResponseDTO resposta = service.toResponseDTO(agendamentoAtualizado);
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/status")
    public ResponseEntity<Void> atualizarStatusEmLote(@RequestBody AtualizarStatusAgendamentosDTO dto,
                                                      Principal principal) {
        service.atualizarStatusAgendamentos(dto, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
