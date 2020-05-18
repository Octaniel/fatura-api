package com.imposto.fatura.model;

import com.imposto.fatura.validation.group.ProdutoGroup;
import com.imposto.fatura.validation.group.ServicoGroup;
import lombok.Getter;

@Getter
public enum TipoOferta {
    PRODUTO("produto", ProdutoGroup.class),
    SERVICO("servico", ServicoGroup.class);

    private final String descricao;
    private final Class<?> group;

    TipoOferta(String descricao, Class<?> group) {
        this.descricao = descricao;
        this.group = group;
    }
}
