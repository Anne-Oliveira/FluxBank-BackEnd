package com.example.fluxbank.dto.response;

import com.example.fluxbank.entity.Usuario;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioResponse {
    private Long id;
    private String nomeCompleto;
    private String cpf;
    private String cnpj;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private Usuario.TipoUsuario tipoUsuario;
    private List<ContaResponse> contas;

    public UsuarioResponse() {}

    public UsuarioResponse(Long id, String nomeCompleto, String cpf, String cnpj, String email, String telefone, LocalDate dataNascimento, Usuario.TipoUsuario tipoUsuario, List<ContaResponse> contas) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.tipoUsuario = tipoUsuario;
        this.contas = contas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public Usuario.TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(Usuario.TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public List<ContaResponse> getContas() { return contas; }
    public void setContas(List<ContaResponse> contas) { this.contas = contas; }

    public static UsuarioResponse fromUsuario(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNomeCompleto(usuario.getNomeCompleto());
        response.setCpf(usuario.getCpf());
        response.setCnpj(usuario.getCnpj());
        response.setEmail(usuario.getEmail());
        response.setTelefone(usuario.getTelefone());
        response.setDataNascimento(usuario.getDataNascimento());
        response.setTipoUsuario(usuario.getTipoUsuario());
        if (usuario.getContas() != null) {
            response.setContas(usuario.getContas().stream()
                    .map(ContaResponse::fromConta)
                    .collect(Collectors.toList()));
        }

        return response;
    }
}