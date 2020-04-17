package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_promocao")
@Getter
@Setter
@EqualsAndHashCode
public class Promocao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocao")
    private Integer id;

    @NotNull(message = "Data inicio é obrigatorio")
    @Column(name = "data_inicio_promocao")
    private LocalDate dataInicio;

    @NotNull(message = "Data Fim é obrigatorio")
    @Column(name = "data_fim_promocao")
    private LocalDate dataFim;

    @NotNull(message = "Valor é obrigatorio")
    @Column(name = "valor_promocao")
    private Double valor;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;
}
