package com.rflwgnr.cabeleleilaleila.data.dto.agendamentos;

public class AgendamentoComUsuarioId {
    private Long idAgendamento;
    private Long idUsuario;

    public AgendamentoComUsuarioId() {}

    public AgendamentoComUsuarioId(Long idAgendamento, Long idUsuario) {
        this.idAgendamento = idAgendamento;
        this.idUsuario = idUsuario;
    }

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Long idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}