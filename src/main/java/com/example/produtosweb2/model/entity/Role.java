package com.example.produtosweb2.model.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "tb_role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ex: ROLE_ADMIN, ROLE_USER
    @Column(unique = true, nullable = false)
    private String nome;

    // Construtor padrão
    public Role() {}

    // O Spring Security usa esse método para saber o nome da permissão
    @Override
    public String getAuthority() {
        return nome;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}