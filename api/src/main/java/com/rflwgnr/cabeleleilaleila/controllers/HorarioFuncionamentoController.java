package com.rflwgnr.cabeleleilaleila.controllers;

import com.rflwgnr.cabeleleilaleila.data.dto.horarios.AtualizarHorarioDTO;
import com.rflwgnr.cabeleleilaleila.model.HorarioFuncionamento;
import com.rflwgnr.cabeleleilaleila.model.Servico;
import com.rflwgnr.cabeleleilaleila.services.HorarioFuncionamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horarios")
public class HorarioFuncionamentoController {

    @Autowired
    private HorarioFuncionamentoService service;

    @GetMapping
    public List<HorarioFuncionamento> listarTodosHorariosFuncionamento() {
        return service.listarTodosHorariosFuncionamento();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioFuncionamento> atualizarHorarioFuncionamento(@PathVariable Long id, @RequestBody AtualizarHorarioDTO horarioFuncionamento) {
        HorarioFuncionamento horarioAtualizado = service.atualizar(id, horarioFuncionamento);
        return ResponseEntity.ok(horarioAtualizado);
    }

}
