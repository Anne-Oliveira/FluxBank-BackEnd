package com.example.fluxbank.repository;

import com.example.fluxbank.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByCnpj(String cnpj);

    Optional<Usuario> findByTokenRecuperacaoSenha(String token);

    Boolean existsByEmail(String email);

    Boolean existsByCpf(String cpf);

    Boolean existsByCnpj(String cnpj);

    Optional<Usuario> findByEmailAndBloqueadoAteIsNotNullAndBloqueadoAteBefore(String email, LocalDateTime dataHora
    );

}
