package com.rflwgnr.cabeleleilaleila.services;

import com.rflwgnr.cabeleleilaleila.data.dto.horarios.AtualizarHorarioDTO;
import com.rflwgnr.cabeleleilaleila.exception.AccessDeniedException;
import com.rflwgnr.cabeleleilaleila.exception.ResourceNotFoundException;
import com.rflwgnr.cabeleleilaleila.model.HorarioFuncionamento;
import com.rflwgnr.cabeleleilaleila.repository.HorarioFuncionamentoRepository;
import com.rflwgnr.cabeleleilaleila.repository.UserRepository;
import com.rflwgnr.cabeleleilaleila.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class HorarioFuncionamentoService {

    @Autowired
    HorarioFuncionamentoRepository repository;

    @Autowired
    UserRepository userRepository;

    public HorarioFuncionamentoService(HorarioFuncionamentoRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<HorarioFuncionamento> listarTodosHorariosFuncionamento() {
        return repository.findAll();
    }

    public HorarioFuncionamento atualizar(Long id, AtualizarHorarioDTO dadosAtualizados) {
        boolean isAdmin = SecurityUtils.isAdmin();

        if (!isAdmin) {
            throw new AccessDeniedException("Usuários comuns não podem alterar o horário de funcionamento.");
        }

        HorarioFuncionamento existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dia da semana não encontrado"));

        HorarioFuncionamento existenteValidado = validarEntradaDados(existente, dadosAtualizados);

        return repository.save(existenteValidado);
    }

    private HorarioFuncionamento validarEntradaDados(HorarioFuncionamento existente, AtualizarHorarioDTO dadosAtualizados) {
        if (dadosAtualizados.getAtivo() != null && dadosAtualizados.getAtivo()) {
            LocalTime entrada1 = dadosAtualizados.getPrimeiraEntrada();
            LocalTime saida1 = dadosAtualizados.getPrimeiraSaida();
            LocalTime entrada2 = dadosAtualizados.getSegundaEntrada();
            LocalTime saida2 = dadosAtualizados.getSegundaSaida();

            // Validando a sequência de horários
            if (entrada1 == null || saida1 == null || entrada2 == null || saida2 == null) {
                throw new IllegalArgumentException("Todos os horários devem ser preenchidos se o registro estiver ativo.");
            }

            if (!entrada1.isBefore(saida1)) {
                throw new IllegalArgumentException("A primeira saída deve ser após a primeira entrada.");
            }

            if (!saida1.isBefore(entrada2)) {
                throw new IllegalArgumentException("A segunda entrada deve ser após a primeira saída.");
            }

            if (!entrada2.isBefore(saida2)) {
                throw new IllegalArgumentException("A segunda saída deve ser após a segunda entrada.");
            }

            existente.setPrimeiraEntrada(entrada1);
            existente.setPrimeiraSaida(saida1);
            existente.setSegundaEntrada(entrada2);
            existente.setSegundaSaida(saida2);
        }

        existente.setAtivo(dadosAtualizados.getAtivo());

        return existente;
    }

}
