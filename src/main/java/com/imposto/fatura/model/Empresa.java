package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_empresa")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Empresa {

    @Id
    @Column(name = "id_empresa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome é obrigatorio")
    @Size(min = 3, max = 100, message = "O nome deve ter no minimo 3 e no maximo 100 carateres")
    @Column(name = "nome_empresa")
    private String nome;

    @NotBlank(message = "Número de Certificação é obrigatorio")
    @Size(min = 10, message = "Número de certificação deve ter no minimo 10 carateres")
    @Column(name = "num_certificacao_empresa")
    private String numeroCertificacao;

    @Column(name = "nif_empresa")
    @NotNull(message = "NIF é obrigatorio")
    private String nif;

    @Email(message = "Email invalido")
    @Column(name = "email_empresa")
    private String email;

    @Column(name = "endereco_empresa")
    private String endereco;

    @Column(name = "telefone_empresa")
    private String telefone;

    @NotNull(message = "Data certificação é obrigatorio")
    @Column(name = "data_certificacao_empresa")
    private LocalDate dataCertificacao;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracoa;
}
