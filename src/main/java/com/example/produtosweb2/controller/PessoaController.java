package com.example.produtosweb2.controller;

import com.example.produtosweb2.service.PessoaService; // Importa nosso novo Serviço
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/list")
    public ModelAndView list(ModelMap model, @RequestParam(value = "q", required = false) String query) {

        // Passamos a 'query' (que pode ser null ou um texto) para o serviço
        model.addAttribute("pessoas", pessoaService.listarTodasPessoas(query));
        return new ModelAndView("pessoas/list", model);
    }

}