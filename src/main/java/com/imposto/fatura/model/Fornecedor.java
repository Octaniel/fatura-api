package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_fornecedor")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Integer id;

    @NotBlank(message = "Nome de Fornecedor é obrigatorio")
    @Column(name = "nome_fornecedor")
    private String nome;

    //@NotNull(message = "NIF do fornecedor é obrigatorio")
    @Column(name = "nif_fornecedor")
    private String nif;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "email_fornecedor")
    private String email;

    @Column(name = "telemovel_fone_fornecedor")
    private String telefone;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;
}
