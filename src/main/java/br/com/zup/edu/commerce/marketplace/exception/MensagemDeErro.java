package br.com.zup.edu.commerce.marketplace.exception;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class MensagemDeErro {

    private final List<String> mensagens = new ArrayList<>();

    public void adicionar(FieldError fieldError) {
        String mensagem = String.format("O campo %s %s", fieldError.getField(), fieldError.getDefaultMessage());
        mensagens.add(mensagem);
    }

    public void adicionar(String mensagem) {
        mensagens.add(mensagem);
    }

    public List<String> getMensagens() {
        return mensagens;
    }

}
