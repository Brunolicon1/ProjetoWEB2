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

    // 1. ADICIONAR ITEM (Lógica alterada)
    // 1. ADICIONAR ITEM (Agora mantém na tela de produtos)
    @PostMapping("/adicionar-item")
    public ModelAndView adicionarItem(@RequestParam("produtoId") Long produtoId) {

        // Verifica se já existe
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

        // MUDANÇA: Redireciona de volta para o FORM (Catálogo) em vez do Carrinho
        return new ModelAndView("redirect:/vendas/form");
    }

    // 2. MÉTODOS PARA SALVAR O ESTADO DO CARRINHO (Novos)
    // Estes métodos servem para atualizar a sessão assim que o usuário muda o select,
    // evitando perder o dado se a página recarregar.

    @PostMapping("/set-pessoa")
    @ResponseBody // Responde apenas OK, sem recarregar a página visualmente (se usar JS) ou redireciona
    public ModelAndView setPessoa(@RequestParam("pessoaId") Long pessoaId) {
        Pessoa pessoa = pessoaService.buscarPessoaPorId(pessoaId);
        venda.setPessoa(pessoa); // Salva na sessão IMEDIATAMENTE
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @PostMapping("/set-data")
    public ModelAndView setData(@RequestParam("data") LocalDate data) {
        venda.setData(data); // Salva na sessão IMEDIATAMENTE
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    // 2. ATUALIZAR QUANTIDADE (Novo Método)
    // Chamado quando mudas o número no input do carrinho
    @PostMapping("/atualizar-item")
    public ModelAndView atualizarItem(@RequestParam("produtoId") Long produtoId,
                                      @RequestParam("quantidade") Integer quantidade) {

        // Procura o item na lista da memória e atualiza a quantidade
        for (ItemVenda item : venda.getItens()) {
            if (item.getProduto().getId() == (long) produtoId) {
                // Validação simples para não permitir 0 ou negativo
                if (quantidade > 0) {
                    item.setQuantidade(quantidade);
                }
                break;
            }
        }
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    // 3. REMOVER ITEM DO CARRINHO (Novo Método)
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

    // 1. Método EDIT (Carrega do Banco para a Memória)
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        // Busca a venda antiga no banco
        Venda vendaBanco = repository.venda(id);

        // --- AQUI ESTÁ O TRUQUE ---
        // Atualizamos o objeto 'venda' da SESSÃO com os dados do banco.
        // O ID deixa de ser 0 e passa a ser o ID da venda antiga (ex: 5).
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

        // Redireciona para o carrinho, que agora vai mostrar os dados da venda 5
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    // 2. Método SAVE (Serve tanto para Criar como para Atualizar)
    @PostMapping("/save")
    public ModelAndView save(@RequestParam("pessoaId") Long pessoaId,
                             @RequestParam("data") LocalDate data,
                             HttpSession session) {

        // 1. Buscar a Pessoa no banco (para garantir que está atualizada)
        Pessoa pessoa = pessoaService.buscarPessoaPorId(pessoaId);

        Venda vendaParaSalvar;

        // 2. Lógica Inteligente: Criar Novo ou Editar Existente
        if (venda.getId() == 0) {
            // CRIAR: Instanciamos uma Venda "limpa", zerada.
            vendaParaSalvar = new Venda();
        } else {
            // EDITAR: Buscamos a venda original no banco para atualizar
            vendaParaSalvar = repository.venda(venda.getId());

            // Se for edição, limpamos os itens antigos do banco para substituir pelos novos do carrinho
            vendaParaSalvar.getItens().clear();
        }

        // 3. Preencher os dados do cabeçalho
        vendaParaSalvar.setPessoa(pessoa);
        vendaParaSalvar.setData(data);

        // 4. Copiar os itens do Carrinho (Sessão) para a Venda de Salvamento (Banco)
        // Isso resolve o problema de "Detached Entity" nos itens também
        for (ItemVenda itemSessao : venda.getItens()) {
            ItemVenda itemBanco = new ItemVenda();

            // Copiamos os dados
            itemBanco.setProduto(itemSessao.getProduto());
            itemBanco.setQuantidade(itemSessao.getQuantidade());

            // Amarração Bidirecional (Pai <-> Filho)
            itemBanco.setVenda(vendaParaSalvar);
            vendaParaSalvar.getItens().add(itemBanco);
        }

        // 5. Salvar a venda "limpa"
        repository.save(vendaParaSalvar);

        // 6. Resetar o carrinho da sessão para uma nova venda
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