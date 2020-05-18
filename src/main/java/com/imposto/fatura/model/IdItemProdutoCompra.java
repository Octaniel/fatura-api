package com.imposto.fatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class IdItemProdutoCompra implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Deves selecionar um produto")
    @JoinColumn(name = "produto_id", insertable = false, updatable = false)
    private Produto produto;

    @JsonIgnoreProperties("itemProdutos")
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Deves efetuar a venda")
    @JoinColumn(name = "compra_id", insertable = false, updatable = false)
    private Compra compra;
}
