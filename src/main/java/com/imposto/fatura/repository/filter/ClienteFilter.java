package com.imposto.fatura.repository.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteFilter {
    private String nome;
    private String nif;
    private Integer idEmpresa;
}
