package com.rflwgnr.cabeleleilaleila.data.dto.horarios;

import java.time.LocalTime;

public class AtualizarHorarioDTO {
    private Boolean ativo;
    private LocalTime primeiraEntrada;
    private LocalTime primeiraSaida;
    private LocalTime segundaEntrada;
    private LocalTime segundaSaida;

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
