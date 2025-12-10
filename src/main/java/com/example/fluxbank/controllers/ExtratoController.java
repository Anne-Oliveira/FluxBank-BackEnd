package com.example.fluxbank.controllers;

import com.example.fluxbank.dto.response.ExtratoResponse;
import com.example.fluxbank.dto.response.TransacaoResponse;
import com.example.fluxbank.services.ExtratoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/extrato")
@CrossOrigin(origins = "*")
public class ExtratoController {

    private final ExtratoService extratoService;

    public ExtratoController(ExtratoService extratoService) {
        this.extratoService = extratoService;
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoResponse>> listarTransacoes(
            @PathVariable Long contaId,
            Authentication authentication) {

        List<TransacaoResponse> transacoes = extratoService.listarTransacoes(contaId);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/conta/{contaId}/periodo")
    public ResponseEntity<ExtratoResponse> gerarExtratoPeriodo(
            @PathVariable Long contaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            Authentication authentication) {

        ExtratoResponse extrato = extratoService.gerarExtrato(contaId, inicio, fim);
        return ResponseEntity.ok(extrato);
    }

    @GetMapping("/conta/{contaId}/ultimos-30-dias")
    public ResponseEntity<ExtratoResponse> gerarExtratoUltimos30Dias(
            @PathVariable Long contaId,
            Authentication authentication) {

        ExtratoResponse extrato = extratoService.gerarExtratoUltimos30Dias(contaId);
        return ResponseEntity.ok(extrato);
    }
}