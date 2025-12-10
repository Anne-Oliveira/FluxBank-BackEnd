package com.example.fluxbank.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PixRequest {
    @NotNull(message = "Conta de origem é obrigatória")
    private Long contaOrigemId;

    @NotBlank(message = "Chave Pix é obrigatória")
    private String chavePix;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor mínimo é R$ 0,01")
    private BigDecimal valor;

    private String descricao;
    private Boolean requerVerificacao = true;

    public PixRequest() {}
    public PixRequest(Long contaOrigemId, String chavePix, BigDecimal valor, String descricao, Boolean requerVerificacao) {
        this.contaOrigemId = contaOrigemId;
        this.chavePix = chavePix;
        this.valor = valor;
        this.descricao = descricao;
        this.requerVerificacao = requerVerificacao;
    }
    public Long getContaOrigemId() { return contaOrigemId; }
    public void setContaOrigemId(Long contaOrigemId) { this.contaOrigemId = contaOrigemId; }
    public String getChavePix() { return chavePix; }
    public void setChavePix(String chavePix) { this.chavePix = chavePix; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getRequerVerificacao() { return requerVerificacao; }
    public void setRequerVerificacao(Boolean requerVerificacao) { this.requerVerificacao = requerVerificacao; }
}