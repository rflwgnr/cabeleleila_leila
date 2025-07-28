package com.rflwgnr.cabeleleilaleila.data.dto.agendamentos;

import com.rflwgnr.cabeleleilaleila.model.Agendamento;

import java.util.List;

public class AtualizarStatusAgendamentosDTO {
    private List<AgendamentoComUsuarioId> agendamentos;
    private Long idStatus;

    public List<AgendamentoComUsuarioId> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<AgendamentoComUsuarioId> agendamentos) {
        this.agendamentos = agendamentos;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }
}
