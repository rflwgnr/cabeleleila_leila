package com.rflwgnr.cabeleleilaleila.controllers;

import com.rflwgnr.cabeleleilaleila.model.Servico;
import com.rflwgnr.cabeleleilaleila.services.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService service;

    @GetMapping("/{id}")
    public Servico listarPorID(@PathVariable Long id) {
        return service.listarPorId(id);
    }

    @GetMapping
    public List<Servico> listarTodosServicos() {
        return service.listarTodosServicos();
    }

    @PostMapping
    public ResponseEntity<Servico> criarServico(@RequestBody Servico servico) {
        Servico servicoCriado = service.salvar(servico);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @RequestBody Servico servico) {
        Servico servicoAtualizado = service.atualizar(id, servico);
        return ResponseEntity.ok(servicoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
