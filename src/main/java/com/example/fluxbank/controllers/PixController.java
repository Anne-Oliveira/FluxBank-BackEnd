package com.example.fluxbank.controllers;

import com.example.fluxbank.dto.request.PixRequest;
import com.example.fluxbank.dto.request.VerificarTransacaoRequest;
import com.example.fluxbank.dto.response.TransacaoResponse;
import com.example.fluxbank.services.PixService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pix")
@CrossOrigin(origins = "*")
public class PixController {

    private final PixService pixService;

    public PixController(PixService pixService) {
        this.pixService = pixService;
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