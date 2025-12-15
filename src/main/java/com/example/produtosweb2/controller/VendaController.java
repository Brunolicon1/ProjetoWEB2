package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.ItemVenda;
import com.example.produtosweb2.model.entity.Pessoa;
import com.example.produtosweb2.model.entity.Produto;
import com.example.produtosweb2.model.entity.Venda;
import com.example.produtosweb2.model.repository.ProdutoRepository;
import com.example.produtosweb2.model.repository.VendaRepository;
import com.example.produtosweb2.service.PessoaService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@RequestMapping("/vendas")
@Scope("request")
@Transactional
public class VendaController {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private Venda venda;

    @GetMapping("/list")
    public ModelAndView list(ModelMap model,
                             @RequestParam(value = "data", required = false) LocalDate data,
                             @RequestParam(value = "pessoaId", required = false) Long pessoaId) {
        model.addAttribute("vendas", repository.filtrarVendas(data, pessoaId));
        model.addAttribute("dataSelecionada", data);
        model.addAttribute("pessoaIdSelecionada", pessoaId);
        model.addAttribute("clientes", pessoaService.listarTodasPessoas(null));
        return new ModelAndView("vendas/list", model);
    }

    @GetMapping("/form")
    public ModelAndView form() {
        ModelAndView mv = new ModelAndView("vendas/form");
        mv.addObject("venda", venda);
        mv.addObject("produtos", produtoRepository.produtos());
        return mv;
    }

    @PostMapping("/adicionar-item")
    public ModelAndView adicionarItem(@RequestParam("produtoId") Long produtoId) {

        boolean existe = venda.getItens().stream()
                .anyMatch(item -> item.getProduto().getId() == (long) produtoId);

        if (!existe) {
            Produto produto = produtoRepository.produto(produtoId);
            if (produto != null) {
                ItemVenda novoItem = new ItemVenda();
                novoItem.setProduto(produto);
                novoItem.setQuantidade(1);
                novoItem.setVenda(venda);
                venda.getItens().add(novoItem);
            }
        }

        return new ModelAndView("redirect:/vendas/form");
    }


    @PostMapping("/set-pessoa")
    @ResponseBody // Responde apenas OK, sem recarregar a página visualmente
    public ModelAndView setPessoa(@RequestParam("pessoaId") Long pessoaId) {
        Pessoa pessoa = pessoaService.buscarPessoaPorId(pessoaId);
        venda.setPessoa(pessoa);
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @PostMapping("/set-data")
    public ModelAndView setData(@RequestParam("data") LocalDate data) {
        venda.setData(data);
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @PostMapping("/atualizar-item")
    public ModelAndView atualizarItem(@RequestParam("produtoId") Long produtoId,
                                      @RequestParam("quantidade") Integer quantidade) {

        for (ItemVenda item : venda.getItens()) {
            if (item.getProduto().getId() == (long) produtoId) {
                if (quantidade > 0) {
                    item.setQuantidade(quantidade);
                }
                break;
            }
        }
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @GetMapping("/remover-item/{id}")
    public ModelAndView removerItemCarrinho(@PathVariable("id") Long produtoId) {

        // Remove da lista em memória usando lambda (remove se o ID bater)
        venda.getItens().removeIf(item -> item.getProduto().getId() == (long) produtoId);

        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @GetMapping("/carrinho")
    public ModelAndView carrinho() {
        ModelAndView mv = new ModelAndView("vendas/carrinho");
        mv.addObject("venda", venda);
        mv.addObject("pessoas", pessoaService.listarTodasPessoas(null));
        return mv;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Venda vendaBanco = repository.venda(id);

        venda.setId(vendaBanco.getId());
        venda.setData(vendaBanco.getData());
        venda.setPessoa(vendaBanco.getPessoa());

        // Limpa os itens da memória e carrega os do banco
        venda.getItens().clear();
        venda.getItens().addAll(vendaBanco.getItens());

        // Refaz a amarração (O Pai conhece o Filho)
        for (ItemVenda item : venda.getItens()) {
            item.setVenda(venda);
        }

        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @PostMapping("/save")
    public ModelAndView save(@RequestParam("pessoaId") Long pessoaId,
                             @RequestParam("data") LocalDate data,
                             HttpSession session) {

        Pessoa pessoa = pessoaService.buscarPessoaPorId(pessoaId);

        Venda vendaParaSalvar;

        if (venda.getId() == 0) {
            vendaParaSalvar = new Venda();
        } else {
            vendaParaSalvar = repository.venda(venda.getId());
            vendaParaSalvar.getItens().clear();
        }

        vendaParaSalvar.setPessoa(pessoa);
        vendaParaSalvar.setData(data);

        for (ItemVenda itemSessao : venda.getItens()) {
            ItemVenda itemBanco = new ItemVenda();

            itemBanco.setProduto(itemSessao.getProduto());
            itemBanco.setQuantidade(itemSessao.getQuantidade());

            itemBanco.setVenda(vendaParaSalvar);
            vendaParaSalvar.getItens().add(itemBanco);
        }

        repository.save(vendaParaSalvar);

        //Reseta o carrinho da sessão para uma nova venda
        session.setAttribute("venda", new Venda());

        return new ModelAndView("redirect:/vendas/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/vendas/list");
    }

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable("id") Long id, ModelMap model) {
        Venda venda = repository.venda(id);
        model.addAttribute("venda", venda);
        return new ModelAndView("vendas/details", model);
    }
}