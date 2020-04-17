package com.imposto.fatura.repository.projection;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VendaResumo {
    private Integer id;
    private String nomeCliente;
    private Double valorTotal;
    private String nifClienteVenda;
    private Double valorImposto;
    private Double valor;
    private LocalDate data;
    private LocalDateTime dataCriacao;
    private Boolean status;
    private Integer numero;
    private String numeroSerie;
    private Integer ano;
    private String siglaTipoDocumento;
    private String descricaoTipoDocumento;


    public VendaResumo(Integer id, String nomeCliente, Double valorTotal, String nifClienteVenda, Double valorImposto, Double valor, LocalDate data, LocalDateTime dataCriacao, Boolean status, Integer numero, String numeroSerie, Integer ano, String siglaTipoDocumento, String descricaoTipoDocumento) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.valorTotal = valorTotal;
        this.nifClienteVenda = nifClienteVenda;
        this.valorImposto = valorImposto;
        this.valor = valor;
        this.data = data;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.numero = numero;
        this.numeroSerie = numeroSerie;
        this.ano = ano;
        this.siglaTipoDocumento = siglaTipoDocumento;
        this.descricaoTipoDocumento = descricaoTipoDocumento;
    }
}
