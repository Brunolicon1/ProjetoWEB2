package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.PessoaJuridica;
import com.example.produtosweb2.model.repository.PessoaJuridicaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("pessoajuridica")
public class PessoaJuridicaController {

    @Autowired
    private PessoaJuridicaRepository repository;

    @GetMapping("/form")
    public ModelAndView form(PessoaJuridica pessoaJuridica) {
        ModelAndView mv = new ModelAndView("pessoas/form");
        mv.addObject("pessoa", pessoaJuridica);
        mv.addObject("formAction", "/pessoajuridica/save");
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("pessoa") @Valid PessoaJuridica pessoaJuridica, BindingResult result){

        if(result.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("formAction", "/pessoajuridica/save");
            // O @ModelAttribute("pessoa") envia os dados preenchidos e os erros de volta
            return mv;
        }

        repository.save(pessoaJuridica);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        PessoaJuridica pessoaJuridica = repository.pessoaJuridica(id);
        model.addAttribute("pessoa", pessoaJuridica);
        model.addAttribute("formAction", "/pessoajuridica/update");
        return new ModelAndView("pessoas/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(@ModelAttribute("pessoa") @Valid PessoaJuridica pessoaJuridica, BindingResult result) {

        if(result.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("formAction", "/pessoajuridica/update");
            return mv;
        }

        repository.update(pessoaJuridica);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id){
        repository.remove(id);
        return new ModelAndView("redirect:/pessoas/list");
    }
}