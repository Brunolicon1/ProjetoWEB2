package com.example.produtosweb2.model.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.io.Serializable;

@Entity
public class PessoaJuridica  extends Pessoa implements Serializable {

    @NotBlank(message = "A Razão Social é obrigatória.")
    @Size(min = 3, message = "A Razão Social deve ter no mínimo 3 letras.")
    private String razaoSocial;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @Size(min = 14, max = 18, message = "O CNPJ deve ter entre 14 e 18 caracteres.")
    @CNPJ
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
