package com.example.fluxbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VerificarTransacaoRequest {
    @NotNull(message = "ID da transação é obrigatório")
    private Long transacaoId;

    @NotBlank(message = "Código de verificação é obrigatório")
    private String codigoVerificacao;

    public VerificarTransacaoRequest() {}
    public VerificarTransacaoRequest(Long transacaoId, String codigoVerificacao) {
        this.transacaoId = transacaoId;
        this.codigoVerificacao = codigoVerificacao;
    }
    public Long getTransacaoId() { return transacaoId; }
    public void setTransacaoId(Long transacaoId) { this.transacaoId = transacaoId; }
    public String getCodigoVerificacao() { return codigoVerificacao; }
    public void setCodigoVerificacao(String codigoVerificacao) { this.codigoVerificacao = codigoVerificacao; }
}