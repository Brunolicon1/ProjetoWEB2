package com.example.produtosweb2.model.entity;

import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public class PessoaJuridica  extends Pessoa implements Serializable {

    private String razaoSocial;

    private String cnpj;

    public String getrazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }


    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String getNome() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    @Override
    public String getDocumento() {
        return cnpj;
    }

    public PessoaJuridica() {
        super(); // Chama o construtor da classe-mãe
    }
}
