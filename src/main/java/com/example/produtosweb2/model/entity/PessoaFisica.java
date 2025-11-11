package com.example.produtosweb2.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

@Entity
public class PessoaFisica extends Pessoa implements Serializable {

    private String cpf;

    private String nome;

    public String getCpf() {
        return cpf;
    }

    @Override
    public String getDocumento() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PessoaFisica() {
        super(); // Chama o construtor da classe-mãe
    }

}
