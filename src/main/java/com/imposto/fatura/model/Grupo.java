package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_grupo")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Integer id;

    @Column(name = "nome_grupo")
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_grupo_permissao",joinColumns = @JoinColumn(name = "grupo_id")
            ,inverseJoinColumns = @JoinColumn(name = "permissao_id"))
    private List<Permisao> permisoes;
}
