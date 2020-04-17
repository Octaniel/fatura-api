package com.imposto.fatura.repository.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoFilter {

    private String nome;
    private String modelo;
    private Integer tipoProduto;
    private String unidade;
    private String natureza;
    private String codigo;
    private Boolean status;
    private Integer idEmpresa;
}
