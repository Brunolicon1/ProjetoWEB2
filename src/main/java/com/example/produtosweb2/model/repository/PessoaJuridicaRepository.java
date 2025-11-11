package com.example.produtosweb2.model.repository;

import com.example.produtosweb2.model.entity.PessoaJuridica;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class PessoaJuridicaRepository {

    @PersistenceContext
    private EntityManager em;


    public void save(PessoaJuridica pessoaJuridica) {
        em.persist(pessoaJuridica);
    }


    public PessoaJuridica pessoaJuridica(Long id) {
        return em.find(PessoaJuridica.class, id);
    }


    public List<PessoaJuridica> pessoasJuridicas() {
        Query query = em.createQuery("from PessoaJuridica");
        return query.getResultList();
    }


    public void remove(Long id) {
        PessoaJuridica p = em.find(PessoaJuridica.class, id);
        em.remove(p);
    }

    public void update(PessoaJuridica pessoaJuridica) {
        em.merge(pessoaJuridica);
    }
}