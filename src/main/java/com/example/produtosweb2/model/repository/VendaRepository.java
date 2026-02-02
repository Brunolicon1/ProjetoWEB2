package com.example.produtosweb2.model.repository;


import com.example.produtosweb2.model.entity.Produto;
import com.example.produtosweb2.model.entity.Venda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Repository
@Transactional
public class VendaRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Venda venda){
        em.persist(venda);
    }

    public Venda venda(Long id){
        return em.find(Venda.class, id);
    }

    public List<Venda> vendas(){
        Query query = em.createQuery("from Venda ");
        return query.getResultList();
    }

    public void remove(Long id){
        Venda v = em.find(Venda.class, id);
        em.remove(v);
    }
    public List<Venda> buscarPorUsuario(String username) {
        List<Venda> todas = vendas();

        return todas.stream()
                .filter(v -> v.getPessoa() != null &&
                        v.getPessoa().getUsuario() != null &&
                        v.getPessoa().getUsuario().getUsername().equals(username))
                .collect(Collectors.toList());
    }



    public void update(Venda venda){
        em.merge(venda);
    }

    //substituido pelo abaixo
//    public List<Venda> buscarPorData(LocalDate dataFiltro) {
//        // 1. Busca todas as vendas do banco
//        List<Venda> todas = vendas(); // Reaproveita "from Venda"
//
//        // 2. Se não tem filtro, retorna tudo
//        if (dataFiltro == null) {
//            return todas;
//        }
//
//        // 3. Filtra usando Java Streams (compara as datas)
//        return todas.stream()
//                .filter(v -> v.getData().equals(dataFiltro))
//                .collect(Collectors.toList());
//    }

    public List<Venda> filtrarVendas(LocalDate dataFiltro, Long pessoaIdFiltro) {
        List<Venda> todas = vendas();

        return todas.stream()
                .filter(v -> {
                    // Condição 1: Data (se dataFiltro for null, passa direto. Se não, compara)
                    boolean dataOk = (dataFiltro == null) || v.getData().equals(dataFiltro);

                    // Condição 2: Cliente (se pessoaIdFiltro for null, passa direto. Se não, compara IDs)
                    boolean clienteOk = (pessoaIdFiltro == null) ||
                            (v.getPessoa() != null && v.getPessoa().getId().equals(pessoaIdFiltro));

                    // Retorna verdadeiro apenas se AMBAS as condições forem satisfeitas
                    return dataOk && clienteOk;
                })
                .collect(Collectors.toList());
    }
}
