package com.rflwgnr.cabeleleilaleila.data.dto.agendamentos;

public class AgendamentoServicoResponseDTO {
    private Long agendamentoId;
    private Long servicoId;
    private Integer quantidade;

    public AgendamentoServicoResponseDTO(Long agendamentoId, Long servicoId, Integer quantidade) {
        this.agendamentoId = agendamentoId;
        this.servicoId = servicoId;
        this.quantidade = quantidade;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
