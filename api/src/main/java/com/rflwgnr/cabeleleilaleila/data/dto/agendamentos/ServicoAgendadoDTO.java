package com.rflwgnr.cabeleleilaleila.data.dto.agendamentos;

public class ServicoAgendadoDTO {
    private Long servicoId;
    private Integer quantidade;

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
