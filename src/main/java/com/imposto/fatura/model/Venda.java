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
@Table(name = "tb_venda")
@Getter
@Setter
@EqualsAndHashCode
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venda")
    private Integer id;

    @JoinColumn(name = "cliente_id")
    @ManyToOne
    private Cliente cliente;

    @Column(name = "valor_venda")
    private Double valor;

    @Column(name = "valor_imposto_venda")
    private Double valorImposto;

    @Column(name = "valor_total_venda")
    private Double valorTotal;

    @NotNull(message = "Data da venda é obrigatorio")
    @Column(name = "data_venda")
    private LocalDate data;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;

    @Column(name = "status_venda")
    private Boolean status;

    @NotNull(message = "Número da fatura é obrigatorio")
    @Column(name = "numero_fatura")
    private Integer numero;

    @NotNull(message = "Serie da fatura é obrigatorio")
    @JoinColumn(name = "serie_id")
    @ManyToOne
    private Serie serie;

    @Column(name = "nif_cliente")
    private String nifClienteVenda;

    @JsonIgnoreProperties("")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_item_produto",joinColumns = @JoinColumn(name = "venda_id")
            ,inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> produtos;
}
