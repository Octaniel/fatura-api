package com.imposto.fatura.repository.projection;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class EmpresaResumo {

    private Integer id;

    private String nome;

    private String numeroCertificacao;

    private String nif;

    private String email;

    private String endereco;

    private String telefone;

    private LocalDate dataCertificacao;

    private LocalDateTime dataCriacao;

    public EmpresaResumo(Integer id, String nome, String numeroCertificacao, String nif, String email, String endereco, String telefone, LocalDate dataCertificacao, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.numeroCertificacao = numeroCertificacao;
        this.nif = nif;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
        this.dataCertificacao = dataCertificacao;
        this.dataCriacao = dataCriacao;
    }
}
