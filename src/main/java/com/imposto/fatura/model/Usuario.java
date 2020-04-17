package com.imposto.fatura.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@EqualsAndHashCode
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @NotBlank(message = "O nome do usuario é obrigatorio")
    @Column(name = "nome_usuario")
    private String nome;

    @NotBlank(message = "O email do usuario é obrigatorio")
    @Email(message = "Email esta invalido")
    @Column(name = "email_usuario")
    private String email;

   // @NotBlank(message = "Senha é obrigatorio")
    @Column(name = "senha_usuario")
    private String senha;

    //@NotNull(message = "Empresa é obrigatorio")
    @JoinColumn(name = "empresa_id")
    @ManyToOne
    private Empresa empresa;

    @Transient
    private String confirmacaoSenha;

    @Size(min = 1,message = "Pelo menos um grupo deve ser selecionado para o usuario")
    @NotNull(message = "Pelo menos um grupo deve ser selecionado para o usuario")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_usuario_grupo",joinColumns = @JoinColumn(name = "usuario_id")
            ,inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private List<Grupo> grupos;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Column(name = "user_cria_id")
    private Integer usuarioCriouId;

    @Column(name = "user_alter_id")
    private Integer usuarioAlterouId;

    @Column(name = "senha_temporaria_usuario")
    private String senhaTemporaria;

    @Column(name = "is_first")
    private Boolean isFirst;
}
