package com.rflwgnr.cabeleleilaleila.data.dto.agendamentos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendamentoResponseDTO {
    private Long id;
    private Long userId;
    private LocalDate dataAgendada;
    private LocalTime horaAgendada;
    private Long status;
    private List<AgendamentoServicoResponseDTO> servicos;

    public AgendamentoResponseDTO(Long id, Long userId, LocalDate dataAgendada, LocalTime horaAgendada, Long status, List<AgendamentoServicoResponseDTO> servicos) {
        this.id = id;
        this.userId = userId;
        this.dataAgendada = dataAgendada;
        this.horaAgendada = horaAgendada;
        this.status = status;
        this.servicos = servicos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(LocalDate dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    public LocalTime getHoraAgendada() {
        return horaAgendada;
    }

    public void setHoraAgendada(LocalTime horaAgendada) {
        this.horaAgendada = horaAgendada;
    }

    public Long getUser() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public List<AgendamentoServicoResponseDTO> getServicos() {
        return servicos;
    }

    public void setServicos(List<AgendamentoServicoResponseDTO> servicos) {
        this.servicos = servicos;
    }
}
