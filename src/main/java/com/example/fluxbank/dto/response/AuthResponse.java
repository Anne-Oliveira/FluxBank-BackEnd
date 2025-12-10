package com.example.fluxbank.dto.response;

public class AuthResponse {
    private String token;
    private String tipo;
    private Long expiraEm;
    private UsuarioResponse usuario;

    public AuthResponse() {}
    public AuthResponse(String token, String tipo, Long expiraEm, UsuarioResponse usuario) {
        this.token = token;
        this.tipo = tipo;
        this.expiraEm = expiraEm;
        this.usuario = usuario;
    }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Long getExpiraEm() { return expiraEm; }
    public void setExpiraEm(Long expiraEm) { this.expiraEm = expiraEm; }
    public UsuarioResponse getUsuario() { return usuario; }
    public void setUsuario(UsuarioResponse usuario) { this.usuario = usuario; }
}