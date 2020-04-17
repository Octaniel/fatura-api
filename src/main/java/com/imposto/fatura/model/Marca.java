package com.imposto.fatura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tb_marca")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Integer id;

    @NotBlank(message = "Nome é obrigatorio")
    @Column(name = "nome_marca")
    private String nome;

    @Column(name = "data_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "data_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "usuario_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "usuario_alter_id")
    private Integer usuarioAlterouId;
}
