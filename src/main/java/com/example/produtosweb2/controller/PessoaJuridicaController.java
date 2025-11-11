package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.PessoaJuridica;
import com.example.produtosweb2.model.repository.PessoaJuridicaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("pessoajuridica")
public class PessoaJuridicaController {

    @Autowired
    PessoaJuridicaRepository Repository;

//    @GetMapping("/list")
//    public ModelAndView list(ModelMap model) {
//        model.addAttribute("pessoas", Repository.pessoasJuridicas());
//        return new ModelAndView("pessoas/list", model);
//    }

    @GetMapping("/form")
    public ModelAndView form(PessoaJuridica pessoaJuridica) {
        String action = (pessoaJuridica.getId() == null) ? "/pessoajuridica/save" : "/pessoajuridica/update";
        ModelAndView mv = new ModelAndView("pessoas/form");
        mv.addObject("formAction", action);
        mv.addObject("pessoa", pessoaJuridica);
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save(PessoaJuridica pessoaJuridica){
        Repository.save(pessoaJuridica);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id){
        Repository.remove(id);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        PessoaJuridica pessoaJuridica = Repository.pessoaJuridica(id);
        model.addAttribute("pessoa", pessoaJuridica);
        model.addAttribute("formAction", "/pessoajuridica/update");
        return new ModelAndView("pessoas/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(PessoaJuridica pessoaJuridica) {
        Repository.update(pessoaJuridica);
        return new ModelAndView("redirect:/pessoas/list");
    }
}