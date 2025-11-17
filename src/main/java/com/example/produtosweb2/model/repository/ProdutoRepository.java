package com.example.produtosweb2.model.repository;


import com.example.produtosweb2.model.entity.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Queue;

@Repository
@Transactional
public class ProdutoRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Produto produto){
        em.persist(produto);
    }

    public Produto produto(Long id){
        return em.find(Produto.class, id);
    }

    public List<Produto> produtos(){
        Query query = em.createQuery("from Produto");
        return query.getResultList();
    }

    public void remove(Long id){
        Produto p = em.find(Produto.class, id);
        em.remove(p);
    }

    public void update(Produto produto){
        em.merge(produto);
    }

    // Dentro de ProdutoRepository.java

    public List<Produto> buscarPorDescricao(String descricao) {
        // JPQL para buscar produtos onde a descrição contém o texto (ignorando maiúsculas/minúsculas)
        String jpql = "from Produto p where lower(p.descricao) like :descricao";

        return em.createQuery(jpql, Produto.class)
                .setParameter("descricao", "%" + descricao.toLowerCase() + "%")
                .getResultList();
    }


}
