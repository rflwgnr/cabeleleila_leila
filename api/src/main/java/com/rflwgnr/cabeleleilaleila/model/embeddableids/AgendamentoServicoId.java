package com.rflwgnr.cabeleleilaleila.model.embeddableids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AgendamentoServicoId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long agendamentoId;

    private Long servicoId;

    public AgendamentoServicoId() {
    }

    public AgendamentoServicoId(Long agendamentoId, Long servicoId) {
        this.agendamentoId = agendamentoId;
        this.servicoId = servicoId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AgendamentoServicoId that = (AgendamentoServicoId) o;
        return Objects.equals(agendamentoId, that.agendamentoId) && Objects.equals(servicoId, that.servicoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agendamentoId, servicoId);
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
}
