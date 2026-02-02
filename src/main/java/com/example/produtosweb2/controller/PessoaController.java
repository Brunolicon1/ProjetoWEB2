package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.Pessoa;
import com.example.produtosweb2.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/list")
    public ModelAndView list(ModelMap model, Authentication authentication) {
        List<Pessoa> listaExibida;

        // Caso 1: Usuário Logado
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                // ADM: vê todos
                listaExibida = pessoaService.listarTodasPessoas(null);
            } else {
                // USER: vê apenas o seu (busca pelo username)
                listaExibida = pessoaService.buscarPorUsuario(authentication.getName());
            }
        }
        // Caso 2: Visitante
        else {
            // Envia lista vazia: o HTML mostrará o card "Usuário não logado"
            listaExibida = new ArrayList<>();
        }

        model.addAttribute("pessoas", listaExibida);
        return new ModelAndView("pessoas/list", model);
    }
}