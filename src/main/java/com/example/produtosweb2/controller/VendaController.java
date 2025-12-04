package com.example.produtosweb2.controller;

import com.example.produtosweb2.model.entity.ItemVenda;
import com.example.produtosweb2.model.entity.Pessoa;
import com.example.produtosweb2.model.entity.Produto;
import com.example.produtosweb2.model.entity.Venda;
import com.example.produtosweb2.model.repository.ProdutoRepository;
import com.example.produtosweb2.model.repository.VendaRepository;
import com.example.produtosweb2.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("vendas")
public class VendaController {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    PessoaService pessoaService;

    // tenho que mudar para que tanto o dropdown quanto a lista de vendas sejam preenchidos pela mesma lista
    //criar metodo em venda que retorna o objeto venda (get venda) que vai retornar o objeto que ta na lista
    //permitindo que eu consiga retornar a pessoa. assim posso retornar as pessoas nos dois itens
    // Venda venda = repository.venda(id); (possivel solucao)
    @GetMapping("/list")
    public ModelAndView list(ModelMap model,
                             @RequestParam(value = "data", required = false) LocalDate data,
                             //@RequestParam(value = "q", required = false) String query,
                             @RequestParam(value = "pessoaId", required = false) Long pessoaId) {
        //envia a lista que preenche o list
        model.addAttribute("vendas", repository.filtrarVendas(data, pessoaId));

        model.addAttribute("dataSelecionada", data);
        model.addAttribute("pessoaIdSelecionada", pessoaId);
        // Envia a lista de clientes para o Dropdown de filtro
        model.addAttribute("clientes", pessoaService.listarTodasPessoas(null));

        return new ModelAndView("vendas/list", model);
    }

    //ainda nao funcional
    @GetMapping("/form")
    public ModelAndView form(Venda venda) {
        List<Pessoa> todasAsPessoas = pessoaService.listarTodasPessoas(null);
        String action = (venda.getId() == 0) ? "/vendas/save" : "/vendas/update";
        ModelAndView mv = new ModelAndView("vendas/form");
        mv.addObject("formAction", action);
        mv.addObject("venda", venda);
        mv.addObject("pessoas", todasAsPessoas);
        mv.addObject("produtos",produtoRepository.produtos());
        return mv;
    }

    //ainda nao funcional
    @PostMapping("/adicionar-item")
    public ModelAndView adicionarItem(Venda venda,
                                      @RequestParam("produtoId") Long produtoId,
                                      @RequestParam("quantidadeItem") int quantidade) {

        Venda vendaSalva;


        if(venda.getId() == 0) {
            // Se a venda é nova, salvamos para gerar o ID
            repository.save(venda);
            vendaSalva = venda;
        } else {
            // Se já existe, buscamos do banco para trazer a lista de itens atualizada.
            // Isso é crucial para não perder os itens que já foram adicionados antes.
            vendaSalva = repository.venda(venda.getId());

            // Atualizamos os dados do cabeçalho (caso o usuário tenha trocado o cliente ou data)
            vendaSalva.setData(venda.getData());
            vendaSalva.setPessoa(venda.getPessoa());
        }

        // LÓGICA DO ITEM
        Produto produto = produtoRepository.produto(produtoId);

        if(produto != null) {
            // Cria o item
            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(quantidade);

            // Amarração Bidirecional (Fundamental para o Cascade funcionar)
            item.setVenda(vendaSalva);          // O Item conhece o Pai
            vendaSalva.getItens().add(item);    // O Pai conhece o Item (adiciona na lista)

            // SALVAR TUDO
            // Ao atualizar a Venda, o JPA percebe o novo item na lista e o salva automaticamente
            repository.update(vendaSalva);
        }

        // Redireciona para a edição da venda (recarrega a página mostrando o novo item)
        return new ModelAndView("redirect:/vendas/edit/" + vendaSalva.getId());
    }

    @PostMapping("/save")
    public ModelAndView save(Venda venda) {
        repository.save(venda);
        return new ModelAndView("redirect:/vendas/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/vendas/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        Venda venda = repository.venda(id);
        model.addAttribute("venda", venda);
        model.addAttribute("formAction", "/vendas/update");
        return new ModelAndView("vendas/form", model);
    }

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
