package com.example.fluxbank.security;

import com.example.fluxbank.entity.Usuario;
import com.example.fluxbank.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        Usuario usuario;

        // Detectar se é CPF ou CNPJ
        if (documento.length() == 11) {
            usuario = usuarioRepository.findByCpf(documento)
                    .orElseThrow(() -> new UsernameNotFoundException("CPF não encontrado: " + documento));
        } else if (documento.length() == 14) {
            usuario = usuarioRepository.findByCnpj(documento)
                    .orElseThrow(() -> new UsernameNotFoundException("CNPJ não encontrado: " + documento));
        } else {
            throw new UsernameNotFoundException("Documento inválido: " + documento);
        }

        return User.builder()
                .username(documento)
                .password(usuario.getSenha())
                .disabled(!usuario.getAtivo())
                .accountLocked(usuario.estaBloqueado())
                .authorities(new ArrayList<>())
                .build();
    }
}