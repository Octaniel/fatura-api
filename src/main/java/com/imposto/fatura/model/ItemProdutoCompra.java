package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_item_produto_compra")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ItemProdutoCompra {

    @EmbeddedId
    private IdItemProdutoCompra id;

    @NotNull(message = "quantidade do item é obrigatorio")
    @Column(name = "quantidade")
    private Integer quantidade;

    @NotNull(message = "Valor do item é obrigatorio")
    @Column(name = "valor")
    private Double valor;

    @NotNull(message = "Valor de Imposto do item é obrigatorio")
    @Column(name = "valor_imposto")
    private Double valorImposto;

    @NotNull(message = "Valor total do item é obrigatorio")
    @Column(name = "valor_total")
    private Double valorTotal;
}
