package com.rflwgnr.cabeleleilaleila.data.dto.agendamentos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendamentoDTO {
    private LocalDate dataAgendada;
    private LocalTime horaAgendada;
    private List<ServicoAgendadoDTO> servicos;

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

    public List<ServicoAgendadoDTO> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoAgendadoDTO> servicos) {
        this.servicos = servicos;
    }
}
