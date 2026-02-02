package com.example.produtosweb2.model.repository;

import com.example.produtosweb2.model.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {
    @PersistenceContext
    private EntityManager em;

    public Role buscarPorNome(String nome) {
        try {
            return em.createQuery("from Role where nome = :n", Role.class)
                    .setParameter("n", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Se não existir, podemos criar ou retornar null
            return null;
        }
    }
}
