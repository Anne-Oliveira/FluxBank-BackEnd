package com.example.fluxbank.dto.response;

import com.example.fluxbank.entity.Conta;
import java.math.BigDecimal;

public class ContaResponse {

    private Long id;
    private String numeroConta;
    private String agencia;
    private BigDecimal saldo;
    private Conta.TipoConta tipoConta;
    private Boolean ativa;
    private String chavePix;

    // Construtores
    public ContaResponse() {}

    public ContaResponse(Long id, String numeroConta, String agencia, BigDecimal saldo,
                         Conta.TipoConta tipoConta, Boolean ativa, String chavePix) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.ativa = ativa;
        this.chavePix = chavePix;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Conta.TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(Conta.TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    // Método estático para conversão
    public static ContaResponse fromConta(Conta conta) {
        ContaResponse response = new ContaResponse();
        response.setId(conta.getId());
        response.setNumeroConta(conta.getNumeroConta());
        response.setAgencia(conta.getAgencia());
        response.setSaldo(conta.getSaldo());
        response.setTipoConta(conta.getTipoConta());
        response.setAtiva(conta.getAtiva());
        response.setChavePix(conta.getChavePix());
        return response;
    }
}