package com.imposto.fatura.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vendadto {
    private Integer qnt;
    private String produto;
    private Double unit;
    private Double subt;
    private String taxa;

    public Vendadto(Integer qnt, String produto, Double unit, Double subt, String taxa) {
        this.qnt = qnt;
        this.produto = produto;
        this.unit = unit;
        this.subt = subt;
        this.taxa = taxa;
    }
}
