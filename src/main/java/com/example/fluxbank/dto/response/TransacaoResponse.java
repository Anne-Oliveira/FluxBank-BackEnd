package com.example.fluxbank.dto.response;

import com.example.fluxbank.entity.Transacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoResponse {

    private Long id;
    private String identificadorTransacao;
    private BigDecimal valor;
    private String tipoTransacao;
    private String statusTransacao;
    private String descricao;
    private String nomeDestinatario;
    private LocalDateTime criadaEm;
    private LocalDateTime processadaEm;
    private Boolean requerVerificacao;
    private Boolean ehEntrada;
    private String contaOrigem;
    private String contaDestino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificadorTransacao() {
        return identificadorTransacao;
    }

    public void setIdentificadorTransacao(String identificadorTransacao) {
        this.identificadorTransacao = identificadorTransacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(String tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public String getStatusTransacao() {
        return statusTransacao;
    }

    public void setStatusTransacao(String statusTransacao) {
        this.statusTransacao = statusTransacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeDestinatario() {
        return nomeDestinatario;
    }

    public void setNomeDestinatario(String nomeDestinatario) {
        this.nomeDestinatario = nomeDestinatario;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }

    public LocalDateTime getProcessadaEm() {
        return processadaEm;
    }

    public void setProcessadaEm(LocalDateTime processadaEm) {
        this.processadaEm = processadaEm;
    }

    public Boolean getRequerVerificacao() {
        return requerVerificacao;
    }

    public void setRequerVerificacao(Boolean requerVerificacao) {
        this.requerVerificacao = requerVerificacao;
    }

    public Boolean getEhEntrada() {
        return ehEntrada;
    }

    public void setEhEntrada(Boolean ehEntrada) {
        this.ehEntrada = ehEntrada;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(String contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }

    public static TransacaoResponse fromTransacao(Transacao transacao, Long contaId) {
        TransacaoResponse response = new TransacaoResponse();
        response.setId(transacao.getId());
        response.setIdentificadorTransacao(transacao.getIdentificadorTransacao());
        response.setValor(transacao.getValor());
        response.setTipoTransacao(transacao.getTipoTransacao().name());
        response.setStatusTransacao(transacao.getStatusTransacao().name());
        response.setNomeDestinatario(transacao.getNomeDestinatario());
        response.setCriadaEm(transacao.getCriadaEm());
        response.setProcessadaEm(transacao.getProcessadaEm());
        response.setRequerVerificacao(
                transacao.getStatusTransacao() == Transacao.StatusTransacao.AGUARDANDO_CODIGO
        );

        response.setEhEntrada(transacao.getContaDestino().getId().equals(contaId));
        response.setContaOrigem(transacao.getContaOrigem().getNumeroConta());
        response.setContaDestino(transacao.getContaDestino().getNumeroConta());

        return response;
    }
}