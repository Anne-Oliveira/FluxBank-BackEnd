package com.example.fluxbank.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    /**
     * MOCK: Em produção, integrar com SendGrid, AWS SES, etc.
     * Por enquanto, apenas loga no console para desenvolvimento.
     */

    public void enviarCodigoVerificacao(String email, String codigo, String nomeUsuario) {
        System.out.println("========================================");
        System.out.println("📧 EMAIL: Código de Verificação Pix");
        System.out.println("========================================");
        System.out.println("Para: " + email);
        System.out.println("Nome: " + nomeUsuario);
        System.out.println("Código: " + codigo);
        System.out.println("Válido por: 5 minutos");
        System.out.println("========================================\n");

        // TODO: Implementar envio real de email
        // Exemplo com SendGrid:
        // sendGridService.send(email, "Código de Verificação", codigo);
    }

    public void enviarTokenRecuperacaoSenha(String email, String token, String nomeUsuario) {
        System.out.println("========================================");
        System.out.println("📧 EMAIL: Recuperação de Senha");
        System.out.println("========================================");
        System.out.println("Para: " + email);
        System.out.println("Nome: " + nomeUsuario);
        System.out.println("Token: " + token);
        System.out.println("Válido por: 1 hora");
        System.out.println("========================================\n");

        // TODO: Implementar envio real de email
    }

    public void enviarConfirmacaoCadastro(String email, String nomeUsuario) {
        System.out.println("========================================");
        System.out.println("📧 EMAIL: Confirmação de Cadastro");
        System.out.println("========================================");
        System.out.println("Para: " + email);
        System.out.println("Nome: " + nomeUsuario);
        System.out.println("Mensagem: Bem-vindo ao FluxBank!");
        System.out.println("========================================\n");

        // TODO: Implementar envio real de email
    }
}