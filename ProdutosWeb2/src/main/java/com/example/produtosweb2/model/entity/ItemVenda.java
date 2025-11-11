package com.example.produtosweb2.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "itemvenda")
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemvenda_seq")
    @SequenceGenerator(name = "itemvenda_seq", sequenceName = "itemvenda_seq", allocationSize = 1)
    private long id;

    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

    public ItemVenda() {
    }

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }

    // método simples de cálculo
    public double total() {
        return produto.getValor() * quantidade;
    }
}
