package com.example.produtosweb2.service;

import com.example.produtosweb2.model.entity.Pessoa;
import com.example.produtosweb2.model.entity.PessoaFisica;
import com.example.produtosweb2.model.entity.PessoaJuridica;
import com.example.produtosweb2.model.repository.PessoaFisicaRepository;
import com.example.produtosweb2.model.repository.PessoaJuridicaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// responsável pela lógica de negócio

@Service
public class PessoaService {

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepo;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepo;

    public List<Pessoa> listarTodasPessoas() {

        List<PessoaFisica> fisicas = pessoaFisicaRepo.pessoasFisicas();
        List<PessoaJuridica> juridicas = pessoaJuridicaRepo.pessoasJuridicas();

        List<Pessoa> todasAsPessoas = new ArrayList<>();

        todasAsPessoas.addAll(fisicas);
        todasAsPessoas.addAll(juridicas);

        // Ordena a lista combinada pelo id
        todasAsPessoas.sort(Comparator.comparing(Pessoa::getId));

        return todasAsPessoas;
    }
}