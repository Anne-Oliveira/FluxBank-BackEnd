package com.example.fluxbank.dto.request;

import com.example.fluxbank.entity.Usuario.TipoUsuario;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CadastroRequest {

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 100, message = "Nome completo deve ter entre 3 e 100 caracteres")
    public String nomeCompleto;

    public String cpf;

    public String cnpj;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    public String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    public String senha;

    @NotBlank(message = "Confirmação de senha é obrigatória")
    public String confirmarSenha;

    public String telefone;

    public LocalDate dataNascimento;

    @NotNull(message = "Tipo de usuário é obrigatório")
    public TipoUsuario tipoUsuario;
}