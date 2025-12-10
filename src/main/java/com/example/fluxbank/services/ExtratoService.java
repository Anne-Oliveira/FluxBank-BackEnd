package com.example.fluxbank.services;

import com.example.fluxbank.dto.response.ExtratoResponse;
import com.example.fluxbank.dto.response.TransacaoResponse;
import com.example.fluxbank.entity.Conta;
import com.example.fluxbank.entity.Transacao;
import com.example.fluxbank.repository.ContaRepository;
import com.example.fluxbank.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtratoService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public ExtratoService(ContaRepository contaRepository,
                          TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public ExtratoResponse gerarExtrato(Long contaId, LocalDateTime inicio, LocalDateTime fim) {
        // Buscar conta
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Buscar transações do período
        List<Transacao> transacoes = transacaoRepository.findExtratoByPeriodo(
                contaId, inicio, fim
        );

        // Converter para TransacaoResponse
        List<TransacaoResponse> transacoesResponse = transacoes.stream()
                .map(t -> TransacaoResponse.fromTransacao(t, contaId))
                .collect(Collectors.toList());

        // Calcular totais
        BigDecimal totalEntradas = BigDecimal.ZERO;
        BigDecimal totalSaidas = BigDecimal.ZERO;

        for (Transacao t : transacoes) {
            if (t.getContaDestino().getId().equals(contaId)) {
                // É entrada
                totalEntradas = totalEntradas.add(t.getValor());
            } else {
                // É saída
                totalSaidas = totalSaidas.add(t.getValor());
            }
        }

        // Montar response
        ExtratoResponse extrato = new ExtratoResponse();
        extrato.setNumeroConta(conta.getNumeroConta());
        extrato.setAgencia(conta.getAgencia());
        extrato.setSaldoAtual(conta.getSaldo());
        extrato.setDataInicio(inicio);
        extrato.setDataFim(fim);
        extrato.setTransacoes(transacoesResponse);
        extrato.setTotalTransacoes(transacoes.size());
        extrato.setTotalEntradas(totalEntradas);
        extrato.setTotalSaidas(totalSaidas);

        return extrato;
    }

    public ExtratoResponse gerarExtratoUltimos30Dias(Long contaId) {
        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusDays(30);
        return gerarExtrato(contaId, inicio, fim);
    }

    public List<TransacaoResponse> listarTransacoes(Long contaId) {
        // Buscar todas as transações
        List<Transacao> transacoes = transacaoRepository.findByContaId(contaId);

        // Converter para response
        return transacoes.stream()
                .map(t -> TransacaoResponse.fromTransacao(t, contaId))
                .collect(Collectors.toList());
    }
}