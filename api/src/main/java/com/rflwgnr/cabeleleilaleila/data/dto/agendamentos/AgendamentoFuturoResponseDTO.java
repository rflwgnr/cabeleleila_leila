package com.rflwgnr.cabeleleilaleila.data.dto.agendamentos;

public class AgendamentoFuturoResponseDTO {
    private boolean temAgendamento;
    private Long idAgendamento;

    public AgendamentoFuturoResponseDTO(boolean temAgendamento, Long idAgendamento) {
        this.temAgendamento = temAgendamento;
        this.idAgendamento = idAgendamento;
    }

    // Getters e Setters
    public boolean isTemAgendamento() {
        return temAgendamento;
    }

    public void setTemAgendamento(boolean temAgendamento) {
        this.temAgendamento = temAgendamento;
    }

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Long idAgendamento) {
        this.idAgendamento = idAgendamento;
    }
}
