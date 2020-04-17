package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_taxa")
@Getter
@Setter
@EqualsAndHashCode
public class Taxa {
    @Id
    @Column(name = "id_taxa")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_imposto_id")
    private TipoImposto tipoImposto;

    @Column(name = "desc_taxa")
    private String descricao;

    @Column(name = "valor_taxa")
    private Integer valor;
}
