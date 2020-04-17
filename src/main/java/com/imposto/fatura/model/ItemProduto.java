package com.imposto.fatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_item_produto")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ItemProduto {

    @EmbeddedId
    private IdItemproduto id;

    @NotNull(message = "Deves selecionar um produto")
    @ManyToOne
    @JoinColumn(name = "produto_id", insertable = false, updatable = false)
    private Produto produto;

    @JsonIgnoreProperties("itemProdutos")
    @NotNull(message = "Deves efetuar a venda")
    @ManyToOne
    @JoinColumn(name = "venda_id", insertable = false, updatable = false)
    private Venda venda;

    @NotNull(message = "quantidade do item é obrigatorio")
    @Column(name = "quantidade_item")
    private Integer quantidade;

    @NotNull(message = "Valor do item é obrigatorio")
    @Column(name = "valor_item")
    private Double valor;

    @NotNull(message = "Valor de Imposto do item é obrigatorio")
    @Column(name = "valor_imposto_item")
    private Double valorImposto;

    @NotNull(message = "Valor total do item é obrigatorio")
    @Column(name = "valor_total_item")
    private Double valorTotal;
}
