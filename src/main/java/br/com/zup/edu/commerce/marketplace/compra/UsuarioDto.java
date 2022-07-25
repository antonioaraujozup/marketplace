package br.com.zup.edu.commerce.marketplace.compra;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UsuarioDto {

    private String nome;
    private String cpf;
    private String endereco;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    public UsuarioDto() {
    }

    public UsuarioDto(Usuario usuario) {
        this.nome = usuario.getNome();
        this.cpf = usuario.getCpf();
        this.endereco = usuario.getEndereco();
        this.email = usuario.getEmail();
        this.dataNascimento = usuario.getDataNascimento();
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

}
