package br.com.zup.edu.commerce.marketplace.compra;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class Usuario {

    @Column(nullable = false, name = "nome_comprador")
    private String nome;

    @Column(nullable = false, name = "cpf_comprador")
    private String cpf;

    @Column(nullable = false, name = "endereco_comprador")
    private String endereco;

    @Column(nullable = false, name = "email_comprador")
    private String email;

    @Column(nullable = false, name = "data_nascimento_comprador")
    private LocalDate dataNascimento;

    /**
     * @deprecated Construtor para uso exclusivo do Hibernate.
     */
    @Deprecated
    public Usuario() {
    }

    public Usuario(String nome, String cpf, String endereco, String email, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

}
