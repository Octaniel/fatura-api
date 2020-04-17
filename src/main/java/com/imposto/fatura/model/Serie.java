package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_serie")
@Getter
@Setter
@EqualsAndHashCode
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_serie")
    private Integer id;

    @NotNull(message = "Tens que dizer o tipo do documento")
    @JoinColumn(name = "tipo_documento_id")
    @ManyToOne
    private TipoDocumento tipoDocumento;

    @NotNull(message = "O numero da serie é obrigatorio")
    @Size(max = 5, min = 5,message ="O número da serie deve ter apenas 5 digitos" )
    @Column(name = "num_serie")
    private String numero;

    @NotNull(message = "O numero de autorização é obrigatorio")
    @Column(name = "num_autorizacao")
    private String numeroAutorizacao;

    @Column(name = "dt_cria_serie")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter_serie")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "pre_definida")
    private Boolean preDefinida;
}
