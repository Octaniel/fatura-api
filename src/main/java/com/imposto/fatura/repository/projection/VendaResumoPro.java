package com.imposto.fatura.repository.projection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.imposto.fatura.model.ItemProduto;
import com.imposto.fatura.model.Produto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class VendaResumoPro {
    private Integer id;
    private String nomeCliente;
    private Double valorTotal;
    private String nifClienteVenda;
    private Double valorImposto;
    private Double valor;
    private LocalDate data;
    private LocalDateTime dataCriacao;
    private Boolean status;
    private String nomeUserCriou;
    private String nomeUserAlterou;
    @JsonIgnoreProperties({"venda", "id"})
    private List<ItemProduto> itemProdutos;
    private LocalDateTime dataAlteracao;
    private Integer numero;
    private String numeroSerie;
    private Integer ano;
    private String numeroAutorizacao;
    private String siglaTipoDocumento;
    private String descricaoTipoDocumento;

    public VendaResumoPro(Integer id, String nomeCliente, Double valorTotal, String nifCliente, Double valorImposto, Double valor, LocalDate data, LocalDateTime dataCriacao, Boolean status, String nomeUserCriou, String nomeUserAlterou, LocalDateTime dataAlteracao, Integer numero, String numeroSerie, Integer ano, String numeroAutorizacao, String siglaTipoDocumento, String descricaoTipoDocumento) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.valorTotal = valorTotal;
        this.nifClienteVenda = nifCliente;
        this.valorImposto = valorImposto;
        this.valor = valor;
        this.data = data;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.nomeUserCriou = nomeUserCriou;
        this.nomeUserAlterou = nomeUserAlterou;
        this.dataAlteracao = dataAlteracao;
        this.numero = numero;
        this.numeroSerie = numeroSerie;
        this.ano = ano;
        this.numeroAutorizacao = numeroAutorizacao;
        this.siglaTipoDocumento = siglaTipoDocumento;
        this.descricaoTipoDocumento = descricaoTipoDocumento;
    }

    public VendaResumoPro() {
    }
}
