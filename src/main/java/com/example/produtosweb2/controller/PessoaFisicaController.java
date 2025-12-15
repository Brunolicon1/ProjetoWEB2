package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.PessoaFisica;
import com.example.produtosweb2.model.repository.PessoaFisicaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("pessoafisica")
public class PessoaFisicaController {

    @Autowired
    private PessoaFisicaRepository repository;

    @GetMapping("/form")
    public ModelAndView form(PessoaFisica pessoaFisica) {
        ModelAndView mv = new ModelAndView("pessoas/form");
        mv.addObject("pessoa", pessoaFisica);
        mv.addObject("formAction", "/pessoafisica/save");
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("pessoa") @Valid PessoaFisica pessoaFisica, BindingResult result) {
        // Se houver erro de validação
        if(result.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("formAction", "/pessoafisica/save");
            // O @ModelAttribute("pessoa") já garante que o objeto volte para a tela
            return mv;
        }

        repository.save(pessoaFisica);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        PessoaFisica pessoaFisica = repository.pessoaFisica(id);
        model.addAttribute("pessoa", pessoaFisica);
        model.addAttribute("formAction", "/pessoafisica/update");
        return new ModelAndView("pessoas/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(@ModelAttribute("pessoa") @Valid PessoaFisica pessoaFisica, BindingResult result) {
        // Validação no Update
        if(result.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("formAction", "/pessoafisica/update");
            return mv;
        }

        repository.update(pessoaFisica);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/pessoas/list");
    }
}