package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_tipo_produto")
@Getter
@Setter
@EqualsAndHashCode
public class TipoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_produto")
    private Integer id;

    @NotBlank(message = "Desrição é obrigatorio")
    @Column(name = "desc_tipo_produto")
    private String descricao;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;

    @NotNull(message = "Natureza  é obrigatorio tipo")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "natureza")
    private TipoOferta natureza;
}
