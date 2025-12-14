package com.example.fluxbank.repository;

import com.example.fluxbank.entity.Conta;
import com.example.fluxbank.entity.Transacao;
import com.example.fluxbank.entity.Transacao.StatusTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    Optional<Transacao> findByIdentificadorTransacao(String identificador);

    @Query("SELECT t FROM Transacao t WHERE t.contaOrigem.id = :contaId OR t.contaDestino.id = :contaId ORDER BY t.criadaEm DESC")
    List<Transacao> findByContaId(@Param("contaId") Long contaId);

    @Query("SELECT t FROM Transacao t WHERE (t.contaOrigem.id = :contaId OR t.contaDestino.id = :contaId) " +
            "AND t.criadaEm BETWEEN :inicio AND :fim ORDER BY t.criadaEm DESC")
    List<Transacao> findExtratoByPeriodo(
            @Param("contaId") Long contaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    List<Transacao> findByContaOrigemIdAndStatusTransacao(
            Long contaId,
            StatusTransacao status
    );

    Optional<Transacao> findByIdAndContaOrigemIdAndStatusTransacao(
            Long id,
            Long contaId,
            StatusTransacao status
    );

    List<Transacao> findTop10ByContaOrigemAndTipoTransacaoOrderByIdDesc(
            Conta contaOrigem,
            Transacao.TipoTransacao tipoTransacao
    );
}