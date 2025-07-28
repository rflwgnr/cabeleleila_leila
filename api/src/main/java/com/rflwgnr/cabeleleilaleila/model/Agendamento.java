package com.rflwgnr.cabeleleilaleila.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "agendamento")
public class Agendamento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status")
    private Status status;

    @Column(name = "data_agendada")
    private LocalDate dataAgendada;

    @Column(name = "hora_agendada")
    private LocalTime horaAgendada;

    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<AgendamentoServico> agendamentosServicos = new ArrayList<>();

    public Agendamento() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(status, that.status) && Objects.equals(dataAgendada, that.dataAgendada) && Objects.equals(horaAgendada, that.horaAgendada) && Objects.equals(agendamentosServicos, that.agendamentosServicos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, status, dataAgendada, horaAgendada, agendamentosServicos);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public List<AgendamentoServico> getAgendamentosServicos() {
        return agendamentosServicos;
    }

    public void setAgendamentosServicos(List<AgendamentoServico> agendamentosServicos) {
        this.agendamentosServicos = agendamentosServicos;
    }
}
