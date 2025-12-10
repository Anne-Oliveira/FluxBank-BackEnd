package com.example.fluxbank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id", nullable = false)
    private Conta contaDestino;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipoTransacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransacao statusTransacao = StatusTransacao.PENDENTE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm = LocalDateTime.now();

    private LocalDateTime processadaEm;

    @Column(nullable = false)
    private String nomeDestinatario;

    private String documentoDestinatario;

    private String codigoVerificacao;

    private LocalDateTime codigoExpiracao;

    private Boolean verificada = false;

    @Column(unique = true)
    private String identificadorTransacao;

    public enum TipoTransacao {
        PIX_ENVIADO,
        PIX_RECEBIDO,
        TED,
        DOC,
        TRANSFERENCIA_INTERNA,
        PAGAMENTO_BOLETO,
        DEPOSITO,
        SAQUE
    }

    public enum StatusTransacao {
        PENDENTE,
        AGUARDANDO_CODIGO,
        PROCESSANDO,
        CONCLUIDA,
        CANCELADA,
        FALHOU,
        ESTORNADA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(Conta contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public StatusTransacao getStatusTransacao() {
        return statusTransacao;
    }

    public void setStatusTransacao(StatusTransacao statusTransacao) {
        this.statusTransacao = statusTransacao;
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

    public String getNomeDestinatario() {
        return nomeDestinatario;
    }

    public void setNomeDestinatario(String nomeDestinatario) {
        this.nomeDestinatario = nomeDestinatario;
    }

    public String getDocumentoDestinatario() {
        return documentoDestinatario;
    }

    public void setDocumentoDestinatario(String documentoDestinatario) {
        this.documentoDestinatario = documentoDestinatario;
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public LocalDateTime getCodigoExpiracao() {
        return codigoExpiracao;
    }

    public void setCodigoExpiracao(LocalDateTime codigoExpiracao) {
        this.codigoExpiracao = codigoExpiracao;
    }

    public Boolean getVerificada() {
        return verificada;
    }

    public void setVerificada(Boolean verificada) {
        this.verificada = verificada;
    }

    public String getIdentificadorTransacao() {
        return identificadorTransacao;
    }

    public void setIdentificadorTransacao(String identificadorTransacao) {
        this.identificadorTransacao = identificadorTransacao;
    }

    @PrePersist
    protected void aoGerar() {
        if (identificadorTransacao == null) {
            identificadorTransacao = gerarIdentificador();
        }
    }

    private String gerarIdentificador() {
        return "PIX" + System.currentTimeMillis() +
                String.format("%04d", (int)(Math.random() * 10000));
    }

    public boolean codigoValido(String codigo) {
        if (codigoVerificacao == null || codigoExpiracao == null) {
            return false;
        }
        return codigoVerificacao.equals(codigo) &&
                LocalDateTime.now().isBefore(codigoExpiracao);
    }
}
