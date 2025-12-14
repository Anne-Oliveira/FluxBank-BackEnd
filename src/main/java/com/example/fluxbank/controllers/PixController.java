package com.example.fluxbank.controllers;

import com.example.fluxbank.dto.request.PixRequest;
import com.example.fluxbank.dto.request.VerificarTransacaoRequest;
import com.example.fluxbank.dto.response.ContaInfoResponse;
import com.example.fluxbank.dto.response.TransacaoResponse;
import com.example.fluxbank.entity.Conta;
import com.example.fluxbank.entity.Transacao;
import com.example.fluxbank.entity.Usuario;
import com.example.fluxbank.repository.ContaRepository;
import com.example.fluxbank.repository.TransacaoRepository;
import com.example.fluxbank.repository.UsuarioRepository;
import com.example.fluxbank.services.PixService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pix")
@CrossOrigin(origins = "*")
public class PixController {

    private final PixService pixService;
    private final UsuarioRepository usuarioRepository;
    private final TransacaoRepository transacaoRepository;

    public PixController(PixService pixService,
                         UsuarioRepository usuarioRepository,
                         TransacaoRepository transacaoRepository) {
        this.pixService = pixService;
        this.usuarioRepository = usuarioRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @GetMapping("/buscar-conta")
    public ResponseEntity<ContaInfoResponse> buscarContaPorChavePix(
            @RequestParam String chavePix,
            Authentication authentication) {

        ContaInfoResponse response = pixService.buscarContaPorChavePix(chavePix);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contatos-recentes")
    public ResponseEntity<List<ContaInfoResponse>> buscarContatosRecentes(Authentication authentication) {
        try {
            String documento = authentication.getName();

            Usuario usuario = usuarioRepository.findByCpf(documento)
                    .orElseGet(() -> usuarioRepository.findByCnpj(documento)
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));

            if (usuario.getContas() == null || usuario.getContas().isEmpty()) {
                return ResponseEntity.ok(List.of());
            }

            Conta contaOrigem = usuario.getContas().get(0);

            List<Transacao> transacoes = transacaoRepository
                    .findTop10ByContaOrigemAndTipoTransacaoOrderByIdDesc(
                            contaOrigem,
                            Transacao.TipoTransacao.PIX_ENVIADO
                    );

            List<ContaInfoResponse> contatos = transacoes.stream()
                    .map(Transacao::getContaDestino)
                    .distinct()
                    .map(ContaInfoResponse::fromConta)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(contatos);

        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<TransacaoResponse> transferir(
            @Valid @RequestBody PixRequest request,
            Authentication authentication) {

        TransacaoResponse response = pixService.iniciarPix(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verificar")
    public ResponseEntity<TransacaoResponse> verificar(
            @Valid @RequestBody VerificarTransacaoRequest request,
            Authentication authentication) {

        TransacaoResponse response = pixService.verificarEProcessar(request);
        return ResponseEntity.ok(response);
    }
}