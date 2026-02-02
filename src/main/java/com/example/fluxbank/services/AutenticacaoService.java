package com.example.fluxbank.services;

import com.example.fluxbank.dto.request.CadastroRequest;
import com.example.fluxbank.dto.request.LoginRequest;
import com.example.fluxbank.dto.response.AuthResponse;
import com.example.fluxbank.dto.response.UsuarioResponse;
import com.example.fluxbank.entity.Conta;
import com.example.fluxbank.entity.Usuario;
import com.example.fluxbank.repository.ContaRepository;
import com.example.fluxbank.repository.UsuarioRepository;
import com.example.fluxbank.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AutenticacaoService(UsuarioRepository usuarioRepository,
                               ContaRepository contaRepository,
                               PasswordEncoder passwordEncoder,
                               JwtTokenProvider jwtTokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse cadastrar(CadastroRequest request) {
        if (!request.senha.equals(request.confirmarSenha)) {
            throw new RuntimeException("Senhas não conferem");
        }

        if (request.senhaTransacao == null || request.senhaTransacao.isBlank()) {
            throw new RuntimeException("Senha de transação é obrigatória");
        }

        if (usuarioRepository.existsByEmail(request.email)) {
            throw new RuntimeException("Este email já cadastrado");
        }

        if (request.tipoUsuario == Usuario.TipoUsuario.PF) {
            if (request.cpf == null || request.cpf.isBlank()) {
                throw new RuntimeException("CPF é obrigatório para Pessoa Física");
            }
            if (usuarioRepository.existsByCpf(request.cpf)) {
                throw new RuntimeException("CPF já cadastrado");
            }
        } else {
            if (request.cnpj == null || request.cnpj.isBlank()) {
                throw new RuntimeException("CNPJ é obrigatório para Pessoa Jurídica");
            }
            if (usuarioRepository.existsByCnpj(request.cnpj)) {
                throw new RuntimeException("CNPJ já cadastrado");
            }
        }

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(request.nomeCompleto);
        usuario.setCpf(request.cpf);
        usuario.setCnpj(request.cnpj);
        usuario.setEmail(request.email);
        usuario.setSenha(passwordEncoder.encode(request.senha));
        usuario.setTelefone(request.telefone);
        usuario.setDataNascimento(request.dataNascimento);
        usuario.setTipoUsuario(request.tipoUsuario);
        usuario.setAtivo(true);
        usuario.setEmailVerificado(false);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Conta conta = new Conta();
        conta.setNumeroConta(gerarNumeroContaUnico());
        conta.setAgencia(Conta.gerarAgencia());
        conta.setTipoConta(request.tipoUsuario == Usuario.TipoUsuario.PF ?
                Conta.TipoConta.CORRENTE : Conta.TipoConta.EMPRESARIAL);
        conta.setUsuario(usuarioSalvo);
        conta.setAtiva(true);

        conta.setChavePix(request.tipoUsuario == Usuario.TipoUsuario.PF ?
                request.cpf : request.cnpj);

        conta.setSenhaTransacao(passwordEncoder.encode(request.senhaTransacao));

        contaRepository.save(conta);

        String documento = request.tipoUsuario == Usuario.TipoUsuario.PF ?
                request.cpf : request.cnpj;
        String token = jwtTokenProvider.gerarToken(documento);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setTipo("Bearer");
        response.setExpiraEm(jwtTokenProvider.getExpiracao());
        response.setUsuario(UsuarioResponse.fromUsuario(usuarioSalvo));

        return response;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String documento = request.documento;
        Usuario usuario;

        if (documento.length() == 11) {
            usuario = usuarioRepository.findByCpf(documento)
                    .orElseThrow(() -> new RuntimeException("CPF não encontrado"));
        } else if (documento.length() == 14) {
            usuario = usuarioRepository.findByCnpj(documento)
                    .orElseThrow(() -> new RuntimeException("CNPJ não encontrado"));
        } else {
            throw new RuntimeException("Documento inválido. Use CPF (11 dígitos) ou CNPJ (14 dígitos)");
        }

        if (usuario.estaBloqueado()) {
            throw new RuntimeException("Usuário bloqueado temporariamente. Tente novamente mais tarde.");
        }

        if (!passwordEncoder.matches(request.senha, usuario.getSenha())) {
            usuario.incrementarTentativasLogin();
            usuarioRepository.save(usuario);

            if (usuario.estaBloqueado()) {
                throw new RuntimeException("Muitas tentativas de login. Conta bloqueada por 30 minutos.");
            }

            throw new RuntimeException("Senha incorreta");
        }

        usuario.resetarTentativasLogin();
        usuarioRepository.save(usuario);

        String token = jwtTokenProvider.gerarToken(documento);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setTipo("Bearer");
        response.setExpiraEm(jwtTokenProvider.getExpiracao());
        response.setUsuario(UsuarioResponse.fromUsuario(usuario));

        return response;
    }

    private String gerarNumeroContaUnico() {
        String numero;
        do {
            numero = Conta.gerarNumeroConta();
        } while (contaRepository.existsByNumeroConta(numero));
        return numero;
    }
}