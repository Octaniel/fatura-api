package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_motivo_isencao")
@Getter
@Setter
@EqualsAndHashCode
public class MotivoIsencao {
    @Id
    @Column(name = "id_isencao")
    private Integer id;

    @NotNull(message = "Descrição de motivo de isenção é obrigatorio")
    @Column(name = "desc_motivo_isencao")
    private String descricao;

    @NotNull(message = "Codigo de motivo de isenção é obrigatorio")
    @Column(name = "cod_motivo_isencao")
    private String codigo;
}
