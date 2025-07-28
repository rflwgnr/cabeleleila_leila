package com.rflwgnr.cabeleleilaleila.model;

import com.rflwgnr.cabeleleilaleila.model.embeddableids.AgendamentoServicoId;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "agendamento_servico")
public class AgendamentoServico implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AgendamentoServicoId id;

    @MapsId("agendamentoId") // nome do atributo em AgendamentoServicoId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agendamento", referencedColumnName = "id")
    private Agendamento agendamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("servicoId")
    @JoinColumn(name = "id_servico")
    private Servico servico;

    @Column(nullable = false)
    private Integer quantidade;

    public AgendamentoServico() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AgendamentoServico that = (AgendamentoServico) o;
        return Objects.equals(id, that.id) && Objects.equals(agendamento, that.agendamento) && Objects.equals(servico, that.servico) && Objects.equals(quantidade, that.quantidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agendamento, servico, quantidade);
    }

    public AgendamentoServicoId getId() {
        return id;
    }

    public void setId(AgendamentoServicoId id) {
        this.id = id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
