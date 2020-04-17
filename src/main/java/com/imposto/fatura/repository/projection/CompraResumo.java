package com.imposto.fatura.repository.projection;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CompraResumo {
    private Integer id;
    private String nomeFornecedor;
    private String nomeProduto;
    private Integer quantidade;
    private Double valor;
    private LocalDate data;
    private LocalDateTime dataCriacao;
}
