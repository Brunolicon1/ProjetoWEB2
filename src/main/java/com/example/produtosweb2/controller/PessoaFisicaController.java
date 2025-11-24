package com.example.produtosweb2.controller;


import com.example.produtosweb2.model.entity.PessoaFisica;
import com.example.produtosweb2.model.repository.PessoaFisicaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("pessoafisica")
public class PessoaFisicaController {

    @Autowired
    PessoaFisicaRepository Repository;

    //como unifiquei no service nao preciso
//    @GetMapping("/list")
//    public ModelAndView list(ModelMap model) {
//        model.addAttribute("pessoas", Repository.pessoasFisicas());
//        return new ModelAndView("pessoas/list", model);
//    }

    @GetMapping("/form")
    public ModelAndView form(PessoaFisica pessoaFisica) {
        String action = (pessoaFisica.getId() == null) ? "/pessoafisica/save" : "/pessoafisica/update";
        ModelAndView mv = new ModelAndView("pessoas/form");
        mv.addObject("formAction", action);
        mv.addObject("pessoa", pessoaFisica);
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save(PessoaFisica pessoaFisica) {
        Repository.save(pessoaFisica);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Repository.remove(id);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        PessoaFisica pessoaFisica = Repository.pessoaFisica(id);
        model.addAttribute("pessoa", pessoaFisica);
        model.addAttribute("formAction", "/pessoafisica/update");
        return new ModelAndView("pessoas/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(PessoaFisica pessoaFisica) {
        Repository.update(pessoaFisica);
        return new ModelAndView("redirect:/pessoas/list");
    }
}