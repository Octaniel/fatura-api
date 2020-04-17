package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_tipo_imposto")
@Getter
@Setter
@EqualsAndHashCode
public class TipoImposto {
    @Id
    @Column(name = "id_tipo_imposto")
    private Integer id;

    @Column(name = "desc_tipo_imposto")
    private String descricao;

    @Column(name = "sigla_tipo_imposto")
    private String sigla;
}
