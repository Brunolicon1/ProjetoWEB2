package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.ItemVenda;
import com.example.produtosweb2.model.entity.Pessoa;
import com.example.produtosweb2.model.entity.Produto;
import com.example.produtosweb2.model.entity.Venda;
import com.example.produtosweb2.model.repository.ProdutoRepository;
import com.example.produtosweb2.model.repository.VendaRepository;
import com.example.produtosweb2.service.PessoaService;
import jakarta.servlet.http.HttpSession; // <--- Importante
import jakarta.transaction.Transactional; // <--- Volta o Transactional
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@RequestMapping("/vendas")
@Scope("request") // Igual ao professor
@Transactional    // Igual ao professor (agora vai funcionar!)
public class VendaController {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private Venda venda; // Instância da sessão

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
        Produto produto = produtoRepository.produto(produtoId);
        if (produto != null) {
            ItemVenda itemExistente = venda.getItens().stream()
                    .filter(i -> i.getProduto().getId() == (long) produtoId)
                    .findFirst()
                    .orElse(null);

            if (itemExistente != null) {
                itemExistente.setQuantidade(itemExistente.getQuantidade() + 1);
            } else {
                ItemVenda novoItem = new ItemVenda();
                novoItem.setProduto(produto);
                novoItem.setQuantidade(1);
                novoItem.setVenda(venda);
                venda.getItens().add(novoItem);
            }
        }
        return new ModelAndView("redirect:/vendas/form");
    }

    @GetMapping("/carrinho")
    public ModelAndView carrinho() {
        ModelAndView mv = new ModelAndView("vendas/carrinho");
        mv.addObject("venda", venda);
        mv.addObject("pessoas", pessoaService.listarTodasPessoas(null));
        return mv;
    }

    // --- AQUI ESTÁ A MUDANÇA PARA FICAR IGUAL AO PROFESSOR ---
    @PostMapping("/save")
    public ModelAndView save(@RequestParam("pessoaId") Long pessoaId,
                             @RequestParam("data") LocalDate data,
                             HttpSession session) {

        System.out.println("--- INICIANDO SAVE ---");
        System.out.println("ID Cliente recebido: " + pessoaId);
        System.out.println("Data recebida: " + data);
        System.out.println("Itens no carrinho antes de salvar: " + venda.getItens().size());

        // 1. Busca e valida a Pessoa
        Pessoa pessoa = pessoaService.buscarPessoaPorId(pessoaId); // Ajuste conforme seu service (ex: repository.pessoa(id))

        if (pessoa == null) {
            System.out.println("ERRO: Pessoa não encontrada!");
            return new ModelAndView("redirect:/vendas/carrinho");
        }

        // 2. Popula a Venda da Sessão
        venda.setPessoa(pessoa);
        venda.setData(data);

        // 3. Garante que todos os itens sabem quem é o pai (Venda)
        // Isso é CRUCIAL para evitar chaves estrangeiras nulas nos itens
        for (ItemVenda item : venda.getItens()) {
            item.setVenda(venda);
        }

        // 4. Salva
        repository.save(venda);
        System.out.println("Venda salva com ID: " + venda.getId());

        // 5. Limpa a sessão (Substitui por novo objeto)
        session.setAttribute("venda", new Venda());

        return new ModelAndView("redirect:/vendas/list");
    }

    // ... edits, removes e details iguais ...

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Venda vendaBanco = repository.venda(id);

        // Carregamos os dados para a memória
        venda.setId(vendaBanco.getId());
        venda.setData(vendaBanco.getData());
        venda.setPessoa(vendaBanco.getPessoa());

        venda.getItens().clear();
        venda.getItens().addAll(vendaBanco.getItens());

        // Refazer vinculo
        for(ItemVenda i : venda.getItens()) i.setVenda(venda);

        return new ModelAndView("redirect:/vendas/carrinho");
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