package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.Pessoa;
import com.example.produtosweb2.model.entity.Venda;
import com.example.produtosweb2.model.repository.VendaRepository;
import com.example.produtosweb2.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("vendas")
public class VendaController {

    @Autowired
    private VendaRepository repository;

    @Autowired
    PessoaService pessoaService;

    // Lista todas as vendas
    @GetMapping("/list")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());
        return new ModelAndView("vendas/list", model);
    }

    // Formulário de nova venda ou edição
//    @GetMapping("/form")
//    public ModelAndView form(Venda venda) {
//        String action = (venda.getId() == 0) ? "/vendas/save" : "/vendas/update";
//        ModelAndView mv = new ModelAndView("vendas/form");
//        mv.addObject("formAction", action);
//        mv.addObject("venda", venda);
//        return mv;
//    }

    @GetMapping("/form")
    public ModelAndView form(Venda venda) {
        // Busca a lista unificada de clientes
        List<Pessoa> todasAsPessoas = pessoaService.listarTodasPessoas();
        String action = (venda.getId() == 0) ? "/vendas/save" : "/vendas/update";
        ModelAndView mv = new ModelAndView("vendas/form");
        mv.addObject("formAction", action);
        mv.addObject("venda", venda);
        mv.addObject("pessoas", todasAsPessoas);
        return mv;
    }

    // Salva nova venda
    @PostMapping("/save")
    public ModelAndView save(Venda venda) {
        repository.save(venda);
        return new ModelAndView("redirect:/vendas/list");
    }

    // Remove venda
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/vendas/list");
    }

    // Edita venda
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        Venda venda = repository.venda(id);
        model.addAttribute("venda", venda);
        model.addAttribute("formAction", "/vendas/update");
        return new ModelAndView("vendas/form", model);
    }

    // Atualiza venda
    @PostMapping("/update")
    public ModelAndView update(Venda venda) {
        repository.update(venda);
        return new ModelAndView("redirect:/vendas/list");
    }

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable("id") Long id, ModelMap model) {
        Venda venda = repository.venda(id);
        model.addAttribute("venda", venda);
        return new ModelAndView("vendas/details", model);
    }

}
