package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.PessoaFisica;
import com.example.produtosweb2.model.entity.Role;
import com.example.produtosweb2.model.entity.Usuario;
import com.example.produtosweb2.model.repository.PessoaFisicaRepository;
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
@RequestMapping("pessoafisica")
public class PessoaFisicaController {

    @Autowired
    private PessoaFisicaRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Você precisará configurar esse Bean no SecurityConfig

    @Autowired
    private RoleRepository roleRepository;


    @GetMapping("/form")
    public ModelAndView form(PessoaFisica pessoaFisica) {
        if (pessoaFisica.getUsuario() == null) {
            pessoaFisica.setUsuario(new Usuario());
        }
        ModelAndView mv = new ModelAndView("pessoas/form");
        mv.addObject("pessoa", pessoaFisica);
        mv.addObject("formAction", "/pessoafisica/save");
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("pessoa") @Valid PessoaFisica pessoaFisica, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("formAction", "/pessoafisica/save");
            return mv;
        }

        if (pessoaFisica.getUsuario() != null && pessoaFisica.getId() == null) {
            Usuario usuario = pessoaFisica.getUsuario();

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            Role rolePadrao = roleRepository.buscarPorNome("ROLE_USER");

            if (rolePadrao != null) {
                usuario.getRoles().add(rolePadrao);
            }

            usuario.setUsername(usuario.getUsername());
        }

        repository.save(pessoaFisica);

        return new ModelAndView("redirect:/login?success");
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