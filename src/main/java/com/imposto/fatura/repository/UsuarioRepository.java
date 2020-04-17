package com.imposto.fatura.repository;

import com.imposto.fatura.model.Usuario;
import com.imposto.fatura.repository.usuario.UsuarioRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, UsuarioRepositoryQuery {
    Optional<Usuario> findByEmail(String email);
}
