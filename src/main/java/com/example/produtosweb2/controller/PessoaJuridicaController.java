package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.PessoaJuridica;
import com.example.produtosweb2.model.entity.Role;
import com.example.produtosweb2.model.entity.Usuario;
import com.example.produtosweb2.model.repository.PessoaJuridicaRepository;
import com.example.produtosweb2.model.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/form")
    public ModelAndView form(PessoaJuridica pessoaJuridica) {
        if (pessoaJuridica.getUsuario() == null) {
            pessoaJuridica.setUsuario(new Usuario());
        }
        ModelAndView mv = new ModelAndView("pessoas/form");
        mv.addObject("pessoa", pessoaJuridica);
        mv.addObject("formAction", "/pessoajuridica/save");
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("pessoa") @Valid PessoaJuridica pessoaJuridica, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("formAction", "/pessoajuridica/save");
            return mv;
        }

        if (pessoaJuridica.getId() == null && pessoaJuridica.getUsuario() != null) {
            Usuario usuario = pessoaJuridica.getUsuario();

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            Role roleUser = roleRepository.buscarPorNome("ROLE_USER");
            if (roleUser != null) {
                usuario.getRoles().add(roleUser);
            }
        }

        repository.save(pessoaJuridica);
        return new ModelAndView("redirect:/login?success");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        PessoaJuridica pessoaJuridica = repository.pessoaJuridica(id);
        if (pessoaJuridica.getUsuario() == null) {
            pessoaJuridica.setUsuario(new Usuario());
        }
        model.addAttribute("pessoa", pessoaJuridica);
        model.addAttribute("formAction", "/pessoajuridica/update");
        return new ModelAndView("pessoas/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(@ModelAttribute("pessoa") @Valid PessoaJuridica pessoaJuridica, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("formAction", "/pessoajuridica/update");
            return mv;
        }
        repository.update(pessoaJuridica);
        return new ModelAndView("redirect:/pessoas/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/pessoas/list");
    }
}