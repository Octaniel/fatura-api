package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_permissao")
@Getter
@Setter
@EqualsAndHashCode
public class Permisao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permissao")
    private Integer id;

    @Column(name = "nome_permissao")
    private String nome;
}
