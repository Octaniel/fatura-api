package com.imposto.fatura.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Faturadto {
    private String prodserv;
    private Double preco;

    public Faturadto(String servico, Double preco) {
        this.prodserv = servico;
        this.preco = preco;
    }
}
