package com.rflwgnr.cabeleleilaleila.services;

import com.rflwgnr.cabeleleilaleila.exception.ResourceNotFoundException;
import com.rflwgnr.cabeleleilaleila.model.Servico;
import com.rflwgnr.cabeleleilaleila.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public Servico listarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));
    }
    public List<Servico> listarTodosServicos() {
        return repository.findAll();
    }

    public Servico salvar(Servico servico) {
        return repository.save(servico);
    }

    public Servico atualizar(Long id, Servico dadosAtualizados) {
        Servico existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        existente.setDescription(dadosAtualizados.getDescription());
        existente.setPrice(dadosAtualizados.getPrice());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado");
        }
        repository.deleteById(id);
    }
}
