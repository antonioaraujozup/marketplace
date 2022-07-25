package br.com.zup.edu.commerce.marketplace.clients;

import br.com.zup.edu.commerce.marketplace.compra.Usuario;

import java.time.LocalDate;

public class UsuarioResponse {

    private String nome;
    private String cpf;
    private String email;
    private String endereco;
    private LocalDate dataNascimento;

    public UsuarioResponse() {
    }

    public UsuarioResponse(String nome, String cpf, String email, String endereco, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return "UsuarioResponse{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", endereco='" + endereco + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }

    public Usuario toUsuario() {
        return new Usuario(nome,cpf,endereco,email,dataNascimento);
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

}
