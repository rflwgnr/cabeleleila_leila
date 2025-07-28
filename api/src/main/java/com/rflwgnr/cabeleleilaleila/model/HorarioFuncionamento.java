package com.rflwgnr.cabeleleilaleila.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table (name = "horario_funcionamento")
public class HorarioFuncionamento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column
    private Boolean ativo;

    @Column
    private LocalTime primeiraEntrada;

    @Column
    private LocalTime primeiraSaida;

    @Column LocalTime segundaEntrada;

    @Column LocalTime segundaSaida;

    public HorarioFuncionamento() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HorarioFuncionamento that = (HorarioFuncionamento) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(ativo, that.ativo) && Objects.equals(primeiraEntrada, that.primeiraEntrada) && Objects.equals(primeiraSaida, that.primeiraSaida) && Objects.equals(segundaEntrada, that.segundaEntrada) && Objects.equals(segundaSaida, that.segundaSaida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, ativo, primeiraEntrada, primeiraSaida, segundaEntrada, segundaSaida);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalTime getPrimeiraEntrada() {
        return primeiraEntrada;
    }

    public void setPrimeiraEntrada(LocalTime primeiraEntrada) {
        this.primeiraEntrada = primeiraEntrada;
    }

    public LocalTime getPrimeiraSaida() {
        return primeiraSaida;
    }

    public void setPrimeiraSaida(LocalTime primeiraSaida) {
        this.primeiraSaida = primeiraSaida;
    }

    public LocalTime getSegundaEntrada() {
        return segundaEntrada;
    }

    public void setSegundaEntrada(LocalTime segundaEntrada) {
        this.segundaEntrada = segundaEntrada;
    }

    public LocalTime getSegundaSaida() {
        return segundaSaida;
    }

    public void setSegundaSaida(LocalTime segundaSaida) {
        this.segundaSaida = segundaSaida;
    }
}
