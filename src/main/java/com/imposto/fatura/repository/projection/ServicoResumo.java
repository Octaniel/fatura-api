package com.imposto.fatura.repository.projection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoResumo {
    private Integer id;
    private String nome;
    private String descTipoServico;
    private Double precoVenda;
    private Boolean status;
    private String descTaxa;
    private String descMotivoIsencao;
    private String codigoMotivoIsencao;
    private Integer valorTaxa;

    public ServicoResumo(Integer id, String nome, String descTipoServico, Double precoVenda, Boolean status, String descTaxa, String descMotivoIsencao, String codigoMotivoIsencao, Integer valorTaxa) {
        this.id = id;
        this.nome = nome;
        this.descTipoServico = descTipoServico;
        this.precoVenda = precoVenda;
        this.status = status;
        this.descTaxa = descTaxa;
        this.descMotivoIsencao = descMotivoIsencao;
        this.codigoMotivoIsencao = codigoMotivoIsencao;
        this.valorTaxa = valorTaxa;
    }
}
