package com.example.fluxbank.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Documento é obrigatório")
    public String documento;

    @NotBlank(message = "Senha é obrigatória")
    public String senha;
}