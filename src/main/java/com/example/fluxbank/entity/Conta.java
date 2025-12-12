package com.example.fluxbank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "contas")
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String numeroConta;

    @Column(nullable = false, length = 4)
    private String agencia;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConta tipoConta;

    @Column(nullable = false)
    private Boolean ativa = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm = LocalDateTime.now();

    private LocalDateTime atuaizadaEm;

    private String chavePix;

    @Column(nullable = false)
    private String senhaTransacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL)
    private List<Transacao> transacoesEnviadas = new ArrayList<>();

    @OneToMany(mappedBy = "contaDestino", cascade = CascadeType.ALL)
    private List<Transacao> transacoesRecebidas = new ArrayList<>();

    @PreUpdate
    protected void aoAtualizar() {
        atuaizadaEm = LocalDateTime.now();
    }

    public enum TipoConta {
        CORRENTE,
        POUPANCA,
        EMPRESARIAL
    }

    public void creditar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor a ser creditado deve ser maior que zero.");
        }
        this.saldo = this.saldo.add(valor);
    }

    public void debitar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor a ser debitado deve ser maior que zero.");
        }
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        this.saldo = this.saldo.subtract(valor);
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

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }

    public LocalDateTime getAtuaizadaEm() {
        return atuaizadaEm;
    }

    public void setAtuaizadaEm(LocalDateTime atuaizadaEm) {
        this.atuaizadaEm = atuaizadaEm;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public String getSenhaTransacao() {
        return senhaTransacao;
    }

    public void setSenhaTransacao(String senhaTransacao) {
        this.senhaTransacao = senhaTransacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Transacao> getTransacoesEnviadas() {
        return transacoesEnviadas;
    }

    public void setTransacoesEnviadas(List<Transacao> transacoesEnviadas) {
        this.transacoesEnviadas = transacoesEnviadas;
    }

    public List<Transacao> getTransacoesRecebidas() {
        return transacoesRecebidas;
    }

    public void setTransacoesRecebidas(List<Transacao> transacoesRecebidas) {
        this.transacoesRecebidas = transacoesRecebidas;
    }

    public static String gerarNumeroConta() {
        Random random = new Random();
        StringBuilder numero = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            numero.append(random.nextInt(10));
        }

        int digitoVerificador = random.nextInt(10);
        numero.append(digitoVerificador);

        return numero.toString();
    }

    public static String gerarAgencia() {
        Random random = new Random();
        int agencia = random.nextInt(9999) + 1;
        return String.format("%04d", agencia);
    }
}