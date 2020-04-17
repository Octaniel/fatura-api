package com.imposto.fatura.repository.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoProdutoFilter {

    private String descricao;
    private String natureza;
    private Integer idEmpresa;

}
