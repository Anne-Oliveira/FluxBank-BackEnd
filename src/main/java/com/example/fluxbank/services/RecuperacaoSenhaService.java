package com.example.fluxbank.services;

import com.example.fluxbank.dto.request.RecuperarSenhaRequest;
import com.example.fluxbank.dto.request.RedefinirSenhaRequest;
import com.example.fluxbank.entity.Usuario;
import com.example.fluxbank.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RecuperacaoSenhaService(UsuarioRepository usuarioRepository,
                                   PasswordEncoder passwordEncoder,
                                   EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void solicitarRecuperacao(RecuperarSenhaRequest request) {
        // Buscar usuário por email
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        // Gerar token único
        String token = UUID.randomUUID().toString();

        // Definir expiração (1 hora)
        LocalDateTime expiracao = LocalDateTime.now().plusHours(1);

        // Salvar token no usuário
        usuario.setTokenRecuperacaoSenha(token);
        usuario.setTokenExpiracao(expiracao);
        usuarioRepository.save(usuario);

        // Enviar email com token
        emailService.enviarTokenRecuperacaoSenha(
                usuario.getEmail(),
                token,
                usuario.getNomeCompleto()
        );
    }

    @Transactional
    public void redefinirSenha(RedefinirSenhaRequest request) {
        // Validar senhas
        if (!request.getNovaSenha().equals(request.getConfirmacaoSenha())) {
            throw new RuntimeException("Senhas não conferem");
        }

        // Buscar usuário pelo token
        Usuario usuario = usuarioRepository.findByTokenRecuperacaoSenha(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        // Verificar se token está expirado
        if (usuario.getTokenExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado. Solicite uma nova recuperação de senha.");
        }

        // Atualizar senha
        usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));

        // Limpar token
        usuario.setTokenRecuperacaoSenha(null);
        usuario.setTokenExpiracao(null);

        // Resetar tentativas de login
        usuario.resetarTentativasLogin();

        usuarioRepository.save(usuario);
    }
}