package com.imposto.fatura.repository.projection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoResumo {
    private Integer id;
    private String nome;
    private String modelo;
    private String descricaoTipoProduto;
    private Double precoVenda;
    private Boolean status;
    private String unidade;
    private Integer quantidade;
    private String marca;
    private String codigo;
    private String descTaxa;
    private Integer valorTaxa;
    private Double valorInalteravel;
    private Boolean emPromocao;

    public ProdutoResumo(Integer id, String nome, String modelo, String descricaoTipoProduto, Double precoVenda, Boolean status, String unidade, Integer quantidade, String marca, String codigo, String descTaxa, Integer valorTaxa, Double valorInalteravel, Boolean emPromocao) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
        this.descricaoTipoProduto = descricaoTipoProduto;
        this.precoVenda = precoVenda;
        this.status = status;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.marca = marca;
        this.codigo = codigo;
        this.descTaxa = descTaxa;
        this.valorTaxa = valorTaxa;
        this.valorInalteravel = valorInalteravel;
        this.emPromocao = emPromocao;
    }

    public ProdutoResumo() {
    }
}
