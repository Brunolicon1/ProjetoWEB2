package com.example.produtosweb2.service;

import com.example.produtosweb2.model.entity.Pessoa;
import com.example.produtosweb2.model.entity.PessoaFisica;
import com.example.produtosweb2.model.entity.PessoaJuridica;
import com.example.produtosweb2.model.repository.PessoaFisicaRepository;
import com.example.produtosweb2.model.repository.PessoaJuridicaRepository;

import com.example.produtosweb2.model.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepo;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepo;

    public List<Pessoa> listarTodasPessoas(String busca) {

        List<PessoaFisica> fisicas = pessoaFisicaRepo.pessoasFisicas();
        List<PessoaJuridica> juridicas = pessoaJuridicaRepo.pessoasJuridicas();

        List<Pessoa> todasAsPessoas = new ArrayList<>();

        todasAsPessoas.addAll(fisicas);
        todasAsPessoas.addAll(juridicas);

        if (busca == null || busca.isEmpty()) {
        todasAsPessoas.sort(Comparator.comparing(Pessoa::getId));
        return todasAsPessoas;
        }
        return todasAsPessoas.stream()
                .filter(p -> {
                    String nome = p.getNome().toLowerCase();
                    return nome.contains(busca.toLowerCase());
                })
                .sorted(Comparator.comparing(Pessoa::getNome))
                .collect(Collectors.toList());
    }
    public Pessoa buscarPessoaPorId(Long id) {
        if (id == null) return null;

        PessoaFisica pf = pessoaFisicaRepo.pessoaFisica(id);
        if (pf != null) return pf;

        PessoaJuridica pj = pessoaJuridicaRepo.pessoaJuridica(id);
        if (pj != null) return pj;

        return null;
    }

    public List<Pessoa> buscarPorUsuario(String username) {
        return repository.findByUsuarioUsername(username);
    }


}