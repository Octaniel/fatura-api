package com.imposto.fatura.model;

import com.imposto.fatura.validation.OfertaGroupSequenceProvider;
import com.imposto.fatura.validation.group.ProdutoGroup;
import com.imposto.fatura.validation.group.ServicoGroup;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_produto")
@GroupSequenceProvider(OfertaGroupSequenceProvider.class)
@Getter
@Setter
@EqualsAndHashCode
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Integer id;

    @NotBlank(message = "Nome de produto é obrigatorio")
    @Column(name = "nome_produto")
    private String nome;

    @NotNull(message = "Quantidade é obrigatorio")
    @Column(name = "quantidade_produto")
    private Integer stock;

    @NotNull(message = "Tens que selecionar um tipo de produto")
    @JoinColumn(name = "tipo_produto_id")
    @ManyToOne
    private TipoProduto tipoProduto;

    @NotNull(groups = ServicoGroup.class, message = "Deves selecionar uma taxa")
    @JoinColumn(name = "taxa_id")
    @ManyToOne
    private Taxa taxa;

    @NotNull(groups = ServicoGroup.class, message = "Deves selecionar um motivo de isenção")
    @JoinColumn(name = "motivo_isencao_id")
    @ManyToOne
    private MotivoIsencao motivoIsencao;

    @NotNull(message = "Preço de venda de produto é obrigatorio")
    @Column(name = "preco_venda_produto")
    private Double precoVenda;

    @Column(name = "desc_produto")
    private String descricao;

    @NotNull(message = "Status de produto é obrigatorio")
    @Column(name = "status_produto")
    private Boolean status;

    @NotNull(message = "Natureza é obrigatorio")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "natureza_produto")
    private TipoOferta natureza;

    @Column(name = "unidade_produto")
    private String unidade;

    @Column(name = "validade_produto")
    private LocalDate validade;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;

    @Transient
    private Integer quantidadeItem;

    @ManyToOne
    @JoinColumn(name = "id_promocao")
    private Promocao promocao;

    @Column(name = "empromocao")
    private Boolean emPromocao;

    @JoinColumn(name = "modelo_produto_id")
    @ManyToOne
    private Modelo modelo;

    @NotNull(groups = ProdutoGroup.class, message = "Codigo é obrigatorio")
    @Column(name = "codigo")
    private String codigo;

    @NotNull(message = "Stock Minimo é obrigatorio")
    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @NotNull(message = "Quantidade Maxima da Venda é obrigatorio")
    @Column(name = "quantidade_maxima_venda")
    private Integer quantidadeMaximaVenda;
}
