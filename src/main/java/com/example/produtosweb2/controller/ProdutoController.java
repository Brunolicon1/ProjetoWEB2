package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.Produto;
import com.example.produtosweb2.model.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    ProdutoRepository Repository;

    //criar um metodo de busca unico, assim com o de pessoa
    @GetMapping("/list")
    public ModelAndView list(ModelMap model, @RequestParam(value = "q", required = false) String query) {

        if (query != null && !query.isEmpty()) {
            model.addAttribute("produtos", Repository.buscarPorDescricao(query));
        } else {
            model.addAttribute("produtos", Repository.produtos());
        }

        return new ModelAndView("produtos/list", model);
    }

    @GetMapping("/form")
    public ModelAndView form(Produto produto) {
        String action = (produto.getId() == 0) ? "/produtos/save" : "/produtos/update";
        ModelAndView mv = new ModelAndView("produtos/form");
        mv.addObject("formAction", action);
        mv.addObject("produto", produto);
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save(Produto produto) {
        Repository.save(produto);
        return new ModelAndView("redirect:/produtos/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Repository.remove(id);
        return new ModelAndView("redirect:/produtos/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        Produto produto = Repository.produto(id);
        model.addAttribute("produto", produto);
        model.addAttribute("formAction", "/produtos/update");
        return new ModelAndView("produtos/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(Produto produto) {
        Repository.update(produto);
        return new ModelAndView("redirect:/produtos/list");
    }

}
