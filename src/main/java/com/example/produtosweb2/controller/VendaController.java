package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.ItemVenda;
import com.example.produtosweb2.model.entity.Pessoa;
import com.example.produtosweb2.model.entity.Produto;
import com.example.produtosweb2.model.entity.Venda;
import com.example.produtosweb2.model.repository.ProdutoRepository;
import com.example.produtosweb2.model.repository.VendaRepository;
import com.example.produtosweb2.service.PessoaService; // Se não tiveres Service, usa o Repository de Pessoa
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/vendas")
@Scope("request")
@Transactional
public class VendaController {

    @Autowired
    private VendaRepository repository; // Banco de Dados

    @Autowired
    private ProdutoRepository produtoRepository; // Banco de Dados

    @Autowired
    private PessoaService pessoaService; // Banco de Dados

    @Autowired
    private Venda venda; // MEMÓRIA (Sessão - O Carrinho Atual)

    // 1. LISTAGEM (Histórico de Vendas) - Usa Repository
    @GetMapping("/list")
    public ModelAndView list(ModelMap model,
                             @RequestParam(value = "data", required = false) LocalDate data,
                             @RequestParam(value = "pessoaId", required = false) Long pessoaId) {

        // Usa o repository para buscar no banco
        model.addAttribute("vendas", repository.filtrarVendas(data, pessoaId));

        model.addAttribute("dataSelecionada", data);
        model.addAttribute("pessoaIdSelecionada", pessoaId);
        model.addAttribute("clientes", pessoaService.listarTodasPessoas(null));

        return new ModelAndView("vendas/list", model);
    }

    // 2. CATÁLOGO DE PRODUTOS (Antigo Form) - Usa Venda da Sessão
    @GetMapping("/form")
    public ModelAndView form() {
        ModelAndView mv = new ModelAndView("vendas/form");
        mv.addObject("venda", venda); // Passa o carrinho atual para vermos a contagem de itens
        mv.addObject("produtos", produtoRepository.produtos()); // Lista produtos para comprar
        return mv;
    }

    // 3. ADICIONAR AO CARRINHO - Manipula apenas a Memória
    @PostMapping("/adicionar-item")
    public ModelAndView adicionarItem(@RequestParam("produtoId") Long produtoId) {

        // Busca o produto no banco apenas para ter os dados
        Produto produto = produtoRepository.produto(produtoId);

        if (produto != null) {
            // Verifica se já existe no carrinho (memória)
            ItemVenda itemExistente = venda.getItens().stream()
                    .filter(i -> i.getProduto().getId() == produtoId) // Cuidado: Long vs long
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

    // 4. TELA DO CARRINHO - Mostra o resumo antes de salvar
    @GetMapping("/carrinho")
    public ModelAndView carrinho() {
        ModelAndView mv = new ModelAndView("vendas/carrinho");
        mv.addObject("venda", venda); // O objeto da sessão com os itens
        mv.addObject("pessoas", pessoaService.listarTodasPessoas(null)); // Para o Select de Clientes
        return mv;
    }


    // 6. EDITAR UMA VENDA ANTIGA
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        // Busca do banco
        Venda vendaBanco = repository.venda(id);

        // Copia os dados do banco para a Sessão
        venda.setId(vendaBanco.getId());
        venda.setData(vendaBanco.getData());
        venda.setPessoa(vendaBanco.getPessoa());
        venda.setItens(vendaBanco.getItens());

        // Redireciona para o carrinho para o usuário ver os itens e editar
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    // 7. REMOVER (Do Banco)
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/vendas/list");
    }

    // 5. SALVAR FINAL
    @PostMapping("/save")
    public ModelAndView save(@RequestParam("pessoaId") Long pessoaId,
                             @RequestParam("data") LocalDate data) {
        // Nota: Removi o @ModelAttribute("venda").
        // Usamos diretamente o "this.venda" que já está injetado na classe.

        // 1. Busca a Pessoa
        Pessoa pessoa = pessoaService.buscarPessoaPorId(pessoaId); // Ajusta o nome do método conforme o teu Service

        // 2. Atualiza os dados na Venda da Sessão (MANUALMENTE)
        // Isso evita que o Spring mude o ID para 0 acidentalmente
        this.venda.setPessoa(pessoa);
        this.venda.setData(data);

        // 3. Salvar
        // Se a venda já tem ID (edição), o save faz update. Se é 0, faz insert.
        repository.save(this.venda);

        // 4. Limpar a sessão
        // CUIDADO: Fazer isso aqui dentro com @Transactional na classe pode ser perigoso.
        // O ideal é limpar os itens, mas criar uma "nova instância" lógica.
        limparSessao();

        return new ModelAndView("redirect:/vendas/list");
    }

    // Método auxiliar para resetar o carrinho
    private void limparSessao() {
        // Em vez de alterar o ID do objeto atual (que o Hibernate está a vigiar),
        // limpamos apenas o conteúdo para a próxima utilização.

        venda.setId(0); // Resetamos o ID para a próxima ser uma NOVA venda
        venda.setItens(new ArrayList<>()); // Esvazia o carrinho
        venda.setPessoa(null);
        venda.setData(LocalDate.now());
    }
}