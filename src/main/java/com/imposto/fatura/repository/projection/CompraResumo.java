package com.imposto.fatura.repository.projection;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CompraResumo {
    private Integer id;
    private String nomeFornecedor;
    private List<ProdutoMin> produtoMins;
    private Double valor;
    private LocalDate data;
    private LocalDateTime dataCriacao;

    @Getter
    @Setter
    public static class ProdutoMin {
        String nomeProduto;
        int quatidadeProduto;
    }
}
