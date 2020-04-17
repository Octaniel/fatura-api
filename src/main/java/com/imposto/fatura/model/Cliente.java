package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_cliente")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    @NotBlank(message = "Nome é Obrigatorio")
    @Column(name = "nome_cliente")
    private String nome;

    @Column(name = "nif_cliente")
    private String nif;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;

}
