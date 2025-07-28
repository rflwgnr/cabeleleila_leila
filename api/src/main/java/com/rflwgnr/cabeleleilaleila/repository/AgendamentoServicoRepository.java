package com.rflwgnr.cabeleleilaleila.repository;

import com.rflwgnr.cabeleleilaleila.model.AgendamentoServico;
import com.rflwgnr.cabeleleilaleila.model.embeddableids.AgendamentoServicoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoServicoRepository extends JpaRepository<AgendamentoServico, AgendamentoServicoId> {
}
