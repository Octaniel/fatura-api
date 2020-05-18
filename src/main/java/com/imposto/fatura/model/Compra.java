package com.imposto.fatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_compra")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer id;

    @NotNull(message = "Valor da compra é obrigatorio")
    @Column(name = "valor_compra")
    private Double valor;

    @Column(name = "observacao")
    private String observacao;

    @NotNull(message = "Deves selecionar um fornecedor")
    @JoinColumn(name = "tb_fornecedor_id")
    @ManyToOne
    private Fornecedor fornecedor;

    @JsonIgnoreProperties("")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_item_produto_compra",joinColumns = @JoinColumn(name = "compra_id")
            ,inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> produtos;

    @NotNull(message = "Deves inserir a data da compra")
    @Column(name = "data_compra")
    private LocalDate data;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;
}
