package com.imposto.fatura.model;

import com.imposto.fatura.validation.group.ProdutoGroup;
import com.imposto.fatura.validation.group.ServicoGroup;

public enum TipoOferta {
    PRODUTO("produto", ProdutoGroup.class),
    SERVICO("servico", ServicoGroup.class);

    private String descricao;
    private Class<?> group;

    TipoOferta(String descricao, Class<?> group) {
        this.descricao = descricao;
        this.group = group;
    }

    public String getDescricao() {
        return descricao;
    }

    public Class<?> getGroup() {
        return group;
    }
}
