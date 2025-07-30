package com.rflwgnr.cabeleleilaleila.services;

import com.rflwgnr.cabeleleilaleila.data.dto.agendamentos.*;
import com.rflwgnr.cabeleleilaleila.exception.AccessDeniedException;
import com.rflwgnr.cabeleleilaleila.exception.BadRequestException;
import com.rflwgnr.cabeleleilaleila.exception.InformationConflictException;
import com.rflwgnr.cabeleleilaleila.exception.ResourceNotFoundException;
import com.rflwgnr.cabeleleilaleila.model.*;
import com.rflwgnr.cabeleleilaleila.model.embeddableids.AgendamentoServicoId;
import com.rflwgnr.cabeleleilaleila.repository.*;
import com.rflwgnr.cabeleleilaleila.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    AgendamentoRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    ServicoRepository servicoRepository;

    @Autowired
    AgendamentoServicoRepository agendamentoServicoRepository;

    @Autowired
    HorarioFuncionamentoRepository horarioFuncionamentoRepository;

    public AgendamentoService(AgendamentoRepository repository, UserRepository userRepository, StatusRepository statusRepository,
                              ServicoRepository servicoRepository, AgendamentoServicoRepository agendamentoServicoRepository,
                              HorarioFuncionamentoRepository horarioFuncionamentoRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.servicoRepository = servicoRepository;
        this.agendamentoServicoRepository = agendamentoServicoRepository;
        this.horarioFuncionamentoRepository = horarioFuncionamentoRepository;
    }

    public List<Agendamento> buscarTodosAgendamentos() {
        boolean isAdmin = SecurityUtils.isAdmin();

        if (!isAdmin) {
            throw new AccessDeniedException("Você não tem permissão para acessar os agendamentos deste usuário.");
        }

        List<Agendamento> agendamentos = repository.findAll();

        if (agendamentos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum agendamento encontrado.");
        }

        return agendamentos;
    }

    public List<Agendamento> buscarPorUsuarioId(Long usuarioId) {
        String usernameLogado = SecurityUtils.getUsuarioLogadoUsername();
        boolean isAdmin = SecurityUtils.isAdmin();

        User userOn = userRepository.findByUsername(usernameLogado).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (!isAdmin && !userOn.getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para acessar os agendamentos deste usuário.");
        }

        User usuarioBusca = userRepository.findById(usuarioId).orElseThrow(() -> new ResourceNotFoundException("Usuário da busca não encontrado."));

        return repository.findAgendamentoByUserId(usuarioBusca.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Agendamentos não encontrados."));
    }

    public List<Agendamento> buscarFuturosPorUsuarioId(Long idUsuario) {
        String usernameLogado = SecurityUtils.getUsuarioLogadoUsername();
        boolean isAdmin = SecurityUtils.isAdmin();

        User userOn = userRepository.findByUsername(usernameLogado).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (!isAdmin && !userOn.getId().equals(idUsuario)) {
            throw new AccessDeniedException("Você não tem permissão para acessar os agendamentos deste usuário.");
        }

        User usuarioBusca = userRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuário da busca não encontrado."));

        LocalDate dataAtual = LocalDate.now();
        LocalTime horaAtual = LocalTime.now();
        List<Long> statusFiltro = Arrays.asList(1L, 2L);
        return repository.findAgendamentosFuturosByUsuario(usuarioBusca.getId(), dataAtual, horaAtual, statusFiltro);
    }

    @Transactional
    public Agendamento criarAgendamento(AgendamentoDTO dto, String usernameLogado) {

        User usuario = validaDadosUsuarioAgendamento(usernameLogado);

        validaHorarioAgendamento(dto, 0L);

        long idStatus;
        boolean isAdmin = SecurityUtils.isAdmin();
        if (isAdmin) {
            idStatus = 2L;
        } else {
            idStatus = 1L;
        }

        Status status = statusRepository.findById(idStatus).orElseThrow(() -> new ResourceNotFoundException("Status não encontrado"));

        return validaCadastroAgendamento(dto, status, usuario);
    }

    @Transactional
    public Agendamento criarAgendamentoOutroUsuario(AgendamentoDTO dto, String usernameLogado, Long idOutroUsuario) {

        User usuarioAgendado;

        validaDadosUsuarioAgendamento(usernameLogado);

        validaHorarioAgendamento(dto, 0L);

        long idStatus;
        boolean isAdmin = SecurityUtils.isAdmin();
        if (isAdmin) {
            idStatus = 2L;
             usuarioAgendado = userRepository.findById(idOutroUsuario)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário do Agendamento não encontrado"));
        } else {
            throw new AccessDeniedException("Apenas usuário admin pode agendar outros usuários");
        }

        Status status = statusRepository.findById(idStatus).orElseThrow(() -> new ResourceNotFoundException("Status não encontrado"));

        return validaCadastroAgendamento(dto, status, usuarioAgendado);
    }

    private User validaDadosUsuarioAgendamento(String usernameLogado) {
        User user = userRepository.findByUsername(usernameLogado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return user;
    }

    private void validaHorarioAgendamento(AgendamentoDTO dto, Long idAgendamentoIgnroado) {
        if (dto.getDataAgendada().isBefore(LocalDate.now()) ||
                (dto.getDataAgendada().isEqual(LocalDate.now()) && dto.getHoraAgendada().isBefore(LocalTime.now()))) {
            throw new RuntimeException("Não é possível agendar para uma data/hora no passado.");
        }

        int idDoDia = dto.getDataAgendada().getDayOfWeek().getValue() % 7 + 1;
        HorarioFuncionamento funcionamento = horarioFuncionamentoRepository
                .findById((long) idDoDia)
                .orElseThrow(() -> new RuntimeException("Horário de funcionamento não encontrado"));

        if (!funcionamento.getAtivo()) {
            throw new RuntimeException("Estabelecimento não funciona nesse dia.");
        }

        LocalTime horaAgendada = dto.getHoraAgendada();

        boolean dentroPrimeiroTurno =
                !horaAgendada.isBefore(funcionamento.getPrimeiraEntrada()) &&
                        !horaAgendada.isAfter(funcionamento.getPrimeiraSaida());

        boolean dentroSegundoTurno =
                funcionamento.getSegundaEntrada() != null && funcionamento.getSegundaSaida() != null &&
                        !horaAgendada.isBefore(funcionamento.getSegundaEntrada()) &&
                        !horaAgendada.isAfter(funcionamento.getSegundaSaida());

        if (!dentroPrimeiroTurno && !dentroSegundoTurno) {
            throw new RuntimeException("Horário fora do expediente.");
        }

        List<Long> statusFiltro = Arrays.asList(1L, 2L);
        boolean existeConflito;
        if(idAgendamentoIgnroado != 0L) {
            existeConflito = repository.existsByDataAgendadaAndHoraAgendadaAndStatusInAndIdNot(dto.getDataAgendada(),
                    dto.getHoraAgendada(), statusFiltro, idAgendamentoIgnroado);
        } else {
            existeConflito = repository.existsByDataAndHoraAndStatus(dto.getDataAgendada(), dto.getHoraAgendada(), statusFiltro);
        }
        if (existeConflito) {
            throw new InformationConflictException("Já existe um agendamento para essa data e hora");
        }
    }

    private Agendamento validaCadastroAgendamento(AgendamentoDTO dto, Status status, User usuario) {
        Agendamento agendamento = new Agendamento();
        agendamento.setDataAgendada(dto.getDataAgendada());
        agendamento.setHoraAgendada(dto.getHoraAgendada());
        agendamento.setStatus(status);
        agendamento.setUser(usuario);

        List<AgendamentoServico> servicos = dto.getServicos().stream().map(servicosAgendados -> {
            Servico servico = servicoRepository.findById(servicosAgendados.getServicoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

            AgendamentoServico agendamentoServico = new AgendamentoServico();
            agendamentoServico.setId(new AgendamentoServicoId(null, null));
            agendamentoServico.setAgendamento(agendamento);
            agendamentoServico.setServico(servico);
            agendamentoServico.setQuantidade(servicosAgendados.getQuantidade());

            return agendamentoServico;
        }).toList();

        agendamento.setAgendamentosServicos(servicos);

        return repository.save(agendamento);
    }

    public AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        List<AgendamentoServicoResponseDTO> servicosDTO = agendamento.getAgendamentosServicos().stream()
                .map(agendamentoServico -> new AgendamentoServicoResponseDTO(
                        agendamento.getId(),
                        agendamentoServico.getServico().getId(),
                        agendamentoServico.getQuantidade()
                )).toList();

        return new AgendamentoResponseDTO(
                agendamento.getId(),
                agendamento.getUser().getId(),
                agendamento.getDataAgendada(),
                agendamento.getHoraAgendada(),
                agendamento.getStatus().getId(),
                servicosDTO
        );
    }

    @Transactional
    public Agendamento atualizarAgendamento(AgendamentoDTO dto, String username, Long idUser, Long idAgendamento) {
        User usuarioLogado = validaDadosUsuarioAgendamento(username);

        boolean isAdmin = SecurityUtils.isAdmin();

        User usuarioBusca = userRepository.findById(idUser).orElseThrow(() -> new ResourceNotFoundException("Usuário da " +
                "atualização não encontrado."));

        Agendamento agendamento = repository.findByIdAndUserId(idAgendamento, usuarioBusca.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        if (!isAdmin && !agendamento.getUser().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não pode alterar agendamentos de outros usuários.");
        }

        validaHorarioAgendamento(dto, agendamento.getId());

        LocalDateTime dataHoraAgendada = LocalDateTime.of(dto.getDataAgendada(), dto.getHoraAgendada());
        if (!isAdmin && dataHoraAgendada.isBefore(LocalDateTime.now().plusDays(2))) {
            throw new BadRequestException("Agendamentos só podem ser alterados com no mínimo 2 dias de antecedência.");
        }

        agendamento.setDataAgendada(dto.getDataAgendada());
        agendamento.setHoraAgendada(dto.getHoraAgendada());

        List<AgendamentoServico> novosServicos = new ArrayList<>();
        for (var servicoDTO : dto.getServicos()) {
            Servico servico = servicoRepository.findById(servicoDTO.getServicoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

            AgendamentoServico novo = new AgendamentoServico();
            novo.setId(new AgendamentoServicoId(agendamento.getId(), servico.getId()));
            novo.setAgendamento(agendamento);
            novo.setServico(servico);
            novo.setQuantidade(servicoDTO.getQuantidade());

            novosServicos.add(novo);
        }

        agendamento.getAgendamentosServicos().clear();
        agendamento.getAgendamentosServicos().addAll(novosServicos);

        return repository.save(agendamento);
    }

    @Transactional
    public void atualizarStatusAgendamentos(AtualizarStatusAgendamentosDTO dto, String username) {
        User usuarioLogado = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        boolean isAdmin = SecurityUtils.isAdmin();

        Status novoStatus = statusRepository.findById(dto.getIdStatus())
                .orElseThrow(() -> new ResourceNotFoundException("Status não encontrado"));

        if (!isAdmin && dto.getIdStatus() != 3L) {
            throw new AccessDeniedException("Usuários comuns só podem alterar o status para Cancelado.");
        }

        for (AgendamentoComUsuarioId ids : dto.getAgendamentos()) {
            User usuarioBusca = userRepository.findById(ids.getIdUsuario()).orElseThrow(() -> new ResourceNotFoundException("Usuário da " +
                    "atualização não encontrado."));

            Agendamento agendamento = repository.findByIdAndUserId(ids.getIdAgendamento(), usuarioBusca.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

            if(agendamento.getStatus().getId() == 3L) {
                throw new BadRequestException("Não é possível alterar agendamentos cancelados.");
            }

            if (!isAdmin && !agendamento.getUser().getId().equals(usuarioLogado.getId())) {
                throw new AccessDeniedException("Você não pode alterar agendamentos de outros usuários.");
            }

            agendamento.setStatus(novoStatus);
            repository.save(agendamento);
        }
    }

    public AgendamentoFuturoResponseDTO verificarSeUsuarioTemAgendamentosNosProximos7Dias(Long idUsuario) {
        String usernameLogado = SecurityUtils.getUsuarioLogadoUsername();
        boolean isAdmin = SecurityUtils.isAdmin();

        User userOn = userRepository.findByUsername(usernameLogado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (!isAdmin && !userOn.getId().equals(idUsuario)) {
            throw new AccessDeniedException("Você não tem permissão para verificar esse usuário.");
        }

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(7);
        LocalTime horaAtual = LocalTime.now();
        List<Long> status = Arrays.asList(1L, 2L);

        Optional<Agendamento> agendamentoOpt = repository.findPrimeiroAgendamentoFuturoPorUsuario(
                idUsuario, hoje, horaAtual, dataLimite, status
        );

        return agendamentoOpt
                .map(agendamento -> new AgendamentoFuturoResponseDTO(true, agendamento.getId()))
                .orElseGet(() -> new AgendamentoFuturoResponseDTO(false, null));
    }
}
