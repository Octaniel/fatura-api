package com.imposto.fatura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tb_modelo")
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Integer id;

    @NotBlank(message = "Nome é obrigatorio")
    @Column(name = "nome_modelo")
    private String nome;

    @NotNull(message = "Tens que selecionar uma marca")
    @JoinColumn(name = "id_marca_modelo")
    @ManyToOne
    private Marca marca;

    @Column(name = "data_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "data_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "usuario_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "usuario_alter_id")
    private Integer usuarioAlterouId;

}
