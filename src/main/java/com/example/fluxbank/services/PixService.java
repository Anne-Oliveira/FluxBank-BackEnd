package com.example.fluxbank.services;

import com.example.fluxbank.dto.request.PixRequest;
import com.example.fluxbank.dto.request.VerificarTransacaoRequest;
import com.example.fluxbank.dto.response.TransacaoResponse;
import com.example.fluxbank.entity.Conta;
import com.example.fluxbank.entity.Transacao;
import com.example.fluxbank.repository.ContaRepository;
import com.example.fluxbank.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PixService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final EmailService emailService;

    public PixService(ContaRepository contaRepository,
                      TransacaoRepository transacaoRepository,
                      EmailService emailService) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.emailService = emailService;
    }

    @Transactional
    public TransacaoResponse iniciarPix(PixRequest request) {
        // Validar conta origem
        Conta contaOrigem = contaRepository.findById(request.getContaOrigemId())
                .orElseThrow(() -> new RuntimeException("Conta origem não encontrada"));

        if (!contaOrigem.getAtiva()) {
            throw new RuntimeException("Conta origem não está ativa");
        }

        // Buscar conta destino pela chave Pix
        java.util.Optional<Conta> contaDestinoOpt = contaRepository.findByChavePix(request.getChavePix());
        if (contaDestinoOpt.isEmpty()) {
            throw new RuntimeException("Chave Pix não encontrada");
        }
        Conta contaDestino = contaDestinoOpt.get();
        if (!contaDestino.getAtiva()) {
            throw new RuntimeException("Conta destino não está ativa");
        }

        // Validar se não é transferência para mesma conta
        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new RuntimeException("Não é possível transferir para a mesma conta");
        }

        // Validar saldo
        if (contaOrigem.getSaldo().compareTo(request.getValor()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Criar transação
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

        // Se requer verificação
        if (request.getRequerVerificacao()) {
            String codigo = gerarCodigoVerificacao();
            transacao.setCodigoVerificacao(codigo);
            transacao.setCodigoExpiracao(LocalDateTime.now().plusMinutes(5));
            transacao.setStatusTransacao(Transacao.StatusTransacao.AGUARDANDO_CODIGO);
            transacao.setVerificada(false);

            Transacao transacaoSalva = transacaoRepository.save(transacao);

            // Enviar código por email
            emailService.enviarCodigoVerificacao(
                    contaOrigem.getUsuario().getEmail(),
                    codigo,
                    contaOrigem.getUsuario().getNomeCompleto()
            );

            return TransacaoResponse.fromTransacao(transacaoSalva, contaOrigem.getId());
        } else {
            // Processar imediatamente
            transacao.setStatusTransacao(Transacao.StatusTransacao.PROCESSANDO);
            transacao.setVerificada(true);
            Transacao transacaoSalva = transacaoRepository.save(transacao);

            processarTransacao(transacaoSalva);

            return TransacaoResponse.fromTransacao(transacaoSalva, contaOrigem.getId());
        }
    }

    @Transactional
    public TransacaoResponse verificarEProcessar(VerificarTransacaoRequest request) {
        // Buscar transação
        Transacao transacao = transacaoRepository.findById(request.getTransacaoId())
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        // Verificar se já foi processada
        if (transacao.getStatusTransacao() == Transacao.StatusTransacao.CONCLUIDA) {
            throw new RuntimeException("Transação já foi processada");
        }

        // Verificar se está aguardando código
        if (transacao.getStatusTransacao() != Transacao.StatusTransacao.AGUARDANDO_CODIGO) {
            throw new RuntimeException("Transação não está aguardando verificação");
        }

        // Validar código
        if (!transacao.codigoValido(request.getCodigoVerificacao())) {
            throw new RuntimeException("Código de verificação inválido ou expirado");
        }

        // Atualizar transação
        transacao.setVerificada(true);
        transacao.setStatusTransacao(Transacao.StatusTransacao.PROCESSANDO);
        transacaoRepository.save(transacao);

        // Processar transação
        processarTransacao(transacao);

        return TransacaoResponse.fromTransacao(transacao, transacao.getContaOrigem().getId());
    }

    private void processarTransacao(Transacao transacao) {
        try {
            Conta contaOrigem = transacao.getContaOrigem();
            Conta contaDestino = transacao.getContaDestino();
            BigDecimal valor = transacao.getValor();

            // Validar saldo novamente
            if (contaOrigem.getSaldo().compareTo(valor) < 0) {
                transacao.setStatusTransacao(Transacao.StatusTransacao.FALHOU);
                transacaoRepository.save(transacao);
                throw new RuntimeException("Saldo insuficiente");
            }

            // Debitar da conta origem
            contaOrigem.debitar(valor);
            contaRepository.save(contaOrigem);

            // Creditar na conta destino
            contaDestino.creditar(valor);
            contaRepository.save(contaDestino);

            // Atualizar status da transação
            transacao.setStatusTransacao(Transacao.StatusTransacao.CONCLUIDA);
            transacao.setProcessadaEm(LocalDateTime.now());
            transacaoRepository.save(transacao);

        } catch (Exception e) {
            transacao.setStatusTransacao(Transacao.StatusTransacao.FALHOU);
            transacaoRepository.save(transacao);
            throw new RuntimeException("Erro ao processar transação: " + e.getMessage());
        }
    }

    private String gerarCodigoVerificacao() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}