package com.example.fluxbank.dto.response;

import com.example.fluxbank.entity.Conta;

public class ContaInfoResponse {

    private Long id;
    private String numeroConta;
    private String agencia;
    private String nomeUsuario;
    private String cpf;
    private String cnpj;
    private String chavePix;
    private String instituicao = "FluxBank";

    public static ContaInfoResponse fromConta(Conta conta) {
        ContaInfoResponse response = new ContaInfoResponse();
        response.setId(conta.getId());
        response.setNumeroConta(conta.getNumeroConta());
        response.setAgencia(conta.getAgencia());
        response.setNomeUsuario(conta.getUsuario().getNomeCompleto());
        response.setCpf(conta.getUsuario().getCpf());
        response.setCnpj(conta.getUsuario().getCnpj());
        response.setChavePix(conta.getChavePix());
        return response;
    }

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

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }
}