package com.example.fluxbank.services;

import com.example.fluxbank.dto.request.PixRequest;
import com.example.fluxbank.dto.request.VerificarTransacaoRequest;
import com.example.fluxbank.dto.response.ContaInfoResponse;
import com.example.fluxbank.dto.response.TransacaoResponse;
import com.example.fluxbank.entity.Conta;
import com.example.fluxbank.entity.Transacao;
import com.example.fluxbank.repository.ContaRepository;
import com.example.fluxbank.repository.TransacaoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PixService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PixService(ContaRepository contaRepository,
                      TransacaoRepository transacaoRepository,
                      EmailService emailService,
                      PasswordEncoder passwordEncoder) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public ContaInfoResponse buscarContaPorChavePix(String chavePix) {
        Conta conta = contaRepository.findByChavePix(chavePix)
                .orElseThrow(() -> new RuntimeException("Chave Pix não encontrada"));

        if (!conta.getAtiva()) {
            throw new RuntimeException("Conta não está ativa");
        }

        return ContaInfoResponse.fromConta(conta);
    }

    @Transactional
    public TransacaoResponse iniciarPix(PixRequest request) {
        Conta contaOrigem = contaRepository.findById(request.getContaOrigemId())
                .orElseThrow(() -> new RuntimeException("Conta origem não encontrada"));

        if (!contaOrigem.getAtiva()) {
            throw new RuntimeException("Conta origem não está ativa");
        }

        java.util.Optional<Conta> contaDestinoOpt = contaRepository.findByChavePix(request.getChavePix());
        if (contaDestinoOpt.isEmpty()) {
            throw new RuntimeException("Chave Pix não encontrada");
        }
        Conta contaDestino = contaDestinoOpt.get();
        if (!contaDestino.getAtiva()) {
            throw new RuntimeException("Conta destino não está ativa");
        }

        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new RuntimeException("Não é possível transferir para a mesma conta");
        }

        if (contaOrigem.getSaldo().compareTo(request.getValor()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        Transacao transacao = new Transacao();
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setValor(request.getValor());
        transacao.setTipoTransacao(Transacao.TipoTransacao.PIX_ENVIADO);
        transacao.setNomeDestinatario(contaDestino.getUsuario().getNomeCompleto());
        transacao.setDocumentoDestinatario(
                contaDestino.getUsuario().getCpf() != null ?
                        contaDestino.getUsuario().getCpf() :
                        contaDestino.getUsuario().getCnpj()
        );

        transacao.setStatusTransacao(Transacao.StatusTransacao.AGUARDANDO_CODIGO);
        transacao.setVerificada(false);

        Transacao transacaoSalva = transacaoRepository.save(transacao);

        return TransacaoResponse.fromTransacao(transacaoSalva, contaOrigem.getId());
    }

    @Transactional
    public TransacaoResponse verificarEProcessar(VerificarTransacaoRequest request) {
        Transacao transacao = transacaoRepository.findById(request.getTransacaoId())
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if (transacao.getStatusTransacao() == Transacao.StatusTransacao.CONCLUIDA) {
            throw new RuntimeException("Transação já foi processada");
        }

        if (transacao.getStatusTransacao() != Transacao.StatusTransacao.AGUARDANDO_CODIGO) {
            throw new RuntimeException("Transação não está aguardando verificação");
        }

        Conta contaOrigem = transacao.getContaOrigem();
        String senhaTransacaoDigitada = request.getCodigoVerificacao();

        if (!passwordEncoder.matches(senhaTransacaoDigitada, contaOrigem.getSenhaTransacao())) {
            throw new RuntimeException("Senha de transação incorreta");
        }

        transacao.setVerificada(true);
        transacao.setStatusTransacao(Transacao.StatusTransacao.PROCESSANDO);
        transacaoRepository.save(transacao);

        processarTransacao(transacao);

        return TransacaoResponse.fromTransacao(transacao, transacao.getContaOrigem().getId());
    }

    private void processarTransacao(Transacao transacao) {
        try {
            Conta contaOrigem = transacao.getContaOrigem();
            Conta contaDestino = transacao.getContaDestino();
            BigDecimal valor = transacao.getValor();

            if (contaOrigem.getSaldo().compareTo(valor) < 0) {
                transacao.setStatusTransacao(Transacao.StatusTransacao.FALHOU);
                transacaoRepository.save(transacao);
                throw new RuntimeException("Saldo insuficiente");
            }

            contaOrigem.debitar(valor);
            contaRepository.save(contaOrigem);

            contaDestino.creditar(valor);
            contaRepository.save(contaDestino);

            transacao.setStatusTransacao(Transacao.StatusTransacao.CONCLUIDA);
            transacao.setProcessadaEm(LocalDateTime.now());
            transacaoRepository.save(transacao);

        } catch (Exception e) {
            transacao.setStatusTransacao(Transacao.StatusTransacao.FALHOU);
            transacaoRepository.save(transacao);
            throw new RuntimeException("Erro ao processar transação: " + e.getMessage());
        }
    }
}