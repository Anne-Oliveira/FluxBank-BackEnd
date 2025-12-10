package com.example.fluxbank.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ExtratoResponse {
    private String numeroConta;
    private String agencia;
    private BigDecimal saldoAtual;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private List<TransacaoResponse> transacoes;
    private Integer totalTransacoes;
    private BigDecimal totalEntradas;
    private BigDecimal totalSaidas;

    public ExtratoResponse() {}
    public ExtratoResponse(String numeroConta, String agencia, BigDecimal saldoAtual, LocalDateTime dataInicio, LocalDateTime dataFim, List<TransacaoResponse> transacoes, Integer totalTransacoes, BigDecimal totalEntradas, BigDecimal totalSaidas) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.saldoAtual = saldoAtual;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.transacoes = transacoes;
        this.totalTransacoes = totalTransacoes;
        this.totalEntradas = totalEntradas;
        this.totalSaidas = totalSaidas;
    }
    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }
    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }
    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { this.saldoAtual = saldoAtual; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public List<TransacaoResponse> getTransacoes() { return transacoes; }
    public void setTransacoes(List<TransacaoResponse> transacoes) { this.transacoes = transacoes; }
    public Integer getTotalTransacoes() { return totalTransacoes; }
    public void setTotalTransacoes(Integer totalTransacoes) { this.totalTransacoes = totalTransacoes; }
    public BigDecimal getTotalEntradas() { return totalEntradas; }
    public void setTotalEntradas(BigDecimal totalEntradas) { this.totalEntradas = totalEntradas; }
    public BigDecimal getTotalSaidas() { return totalSaidas; }
    public void setTotalSaidas(BigDecimal totalSaidas) { this.totalSaidas = totalSaidas; }
}