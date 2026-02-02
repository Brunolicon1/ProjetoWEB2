package com.example.produtosweb2.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "tb_pessoa")
public abstract class Pessoa {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_seq")
    @SequenceGenerator(name = "pessoa_seq", sequenceName = "pessoa_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Digite um e-mail válido (ex: nome@dominio.com).")
    private String email;

    @OneToMany(mappedBy = "pessoa")
    private List<Venda> vendas = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL) // Cascade faz com que ao salvar a Pessoa, o Usuario também seja salvo
    @JoinColumn(name = "id_usuario") // Nome da coluna no banco de dados
    private Usuario usuario;

    // Getter e Setter
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public abstract String getNome();

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    public boolean tipoObjeto(String objeto){
        return this.getClass().getSimpleName().toLowerCase().equals(objeto.toLowerCase());
    }

    public String nomeClasse(){
        return this.getClass().getSimpleName().toLowerCase();
    }

    public abstract String getDocumento();

    public Pessoa() {
    }


}
