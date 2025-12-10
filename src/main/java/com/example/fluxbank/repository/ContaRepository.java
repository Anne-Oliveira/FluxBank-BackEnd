package com.example.fluxbank.repository;

import com.example.fluxbank.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumeroConta(String numeroConta);

    Optional<Conta> findByChavePix(String chavePix);

    List<Conta> findByUsuarioId(Long usuarioId);

    List<Conta> findByUsuarioIdAndAtiva(Long usuarioId, Boolean ativa);

    Boolean existsByNumeroConta(String numeroConta);

    Boolean existsByChavePix(String chavePix);
}
