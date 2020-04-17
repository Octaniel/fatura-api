package com.imposto.fatura.repository.projection;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteResumo {

    private Integer id;
    private String nome;
    private String nif;
    private LocalDateTime dataCriacao;
    private Integer usuarioCriouId;

    public ClienteResumo(Integer id, String nome, String nif, LocalDateTime dataRegistro, Integer usuarioCriouId) {
        this.id = id;
        this.nome = nome;
        this.nif = nif;
        this.dataCriacao = dataRegistro;
        this.usuarioCriouId = usuarioCriouId;
    }
}
