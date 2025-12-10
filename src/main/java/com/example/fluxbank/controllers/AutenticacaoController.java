package com.example.fluxbank.controllers;

import com.example.fluxbank.dto.request.CadastroRequest;
import com.example.fluxbank.dto.request.LoginRequest;
import com.example.fluxbank.dto.request.RecuperarSenhaRequest;
import com.example.fluxbank.dto.request.RedefinirSenhaRequest;
import com.example.fluxbank.dto.response.AuthResponse;
import com.example.fluxbank.services.AutenticacaoService;
import com.example.fluxbank.services.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;
    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public AutenticacaoController(AutenticacaoService autenticacaoService,
                                  RecuperacaoSenhaService recuperacaoSenhaService) {
        this.autenticacaoService = autenticacaoService;
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<AuthResponse> cadastrar(@Valid @RequestBody CadastroRequest request) {
        AuthResponse response = autenticacaoService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = autenticacaoService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@Valid @RequestBody RecuperarSenhaRequest request) {
        recuperacaoSenhaService.solicitarRecuperacao(request);
        return ResponseEntity.ok("Email de recuperação enviado com sucesso");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@Valid @RequestBody RedefinirSenhaRequest request) {
        recuperacaoSenhaService.redefinirSenha(request);
        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
}