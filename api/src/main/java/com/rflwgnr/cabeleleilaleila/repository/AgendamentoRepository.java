package com.rflwgnr.cabeleleilaleila.repository;

import com.rflwgnr.cabeleleilaleila.model.Agendamento;
import com.rflwgnr.cabeleleilaleila.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT a FROM Agendamento a WHERE a.user.id = :userId")
    Optional<List<Agendamento>> findAgendamentoByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Agendamento a WHERE a.user.id = :idUsuario AND " +
            "(a.dataAgendada > :dataAtual OR (a.dataAgendada = :dataAtual AND a.horaAgendada >= :horaAtual)) AND " +
            " a.status.id IN :statusIds")
    List<Agendamento> findAgendamentosFuturosByUsuario(@Param("idUsuario") Long idUsuario,
                                                       @Param("dataAtual") LocalDate dataAtual,
                                                       @Param("horaAtual") LocalTime horaAtual,
                                                       @Param("statusIds") List<Long> status);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.dataAgendada = :dataAgendada AND a.horaAgendada = :horaAgendada " +
            " AND a.status.id IN :statusIds ")
    boolean existsByDataAndHoraAndStatus(@Param("dataAgendada") LocalDate data, @Param("horaAgendada") LocalTime hora,
                                @Param("statusIds") List<Long> status);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.dataAgendada = :dataAgendada AND a.horaAgendada = :horaAgendada " +
            " AND a.status.id IN :statusIds AND a.id NOT IN :id ")
    boolean existsByDataAgendadaAndHoraAgendadaAndStatusInAndIdNot(@Param("dataAgendada") LocalDate dataAgendada,
                                                                   @Param("horaAgendada") LocalTime horaAgendada,
                                                                   @Param("statusIds") List<Long> status,
                                                                   @Param("id") Long idIgnorado
    );

    Optional<Agendamento> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT a FROM Agendamento a WHERE a.user.id = :idUsuario AND " +
            "((a.dataAgendada > :dataAtual OR (a.dataAgendada = :dataAtual AND a.horaAgendada >= :horaAtual)) AND " +
            "a.dataAgendada <= :dataLimite AND a.status.id IN :statusIds) " +
            "ORDER BY a.dataAgendada ASC, a.horaAgendada ASC")
    Optional<Agendamento> findPrimeiroAgendamentoFuturoPorUsuario(
            @Param("idUsuario") Long idUsuario,
            @Param("dataAtual") LocalDate dataAtual,
            @Param("horaAtual") LocalTime horaAtual,
            @Param("dataLimite") LocalDate dataLimite,
            @Param("statusIds") List<Long> statusIds);
}
