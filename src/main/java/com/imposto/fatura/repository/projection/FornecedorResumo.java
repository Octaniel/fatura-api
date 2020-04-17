package com.imposto.fatura.repository.projection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorResumo {

    private Integer id;
    private String nome;
    private String nif;
    private String endereco;
    private String email;
    private String telefone;

    public FornecedorResumo(Integer id, String nome, String nif, String endereco, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.nif = nif;
        this.endereco = endereco;
        this.email = email;
        this.telefone = telefone;
    }
}
