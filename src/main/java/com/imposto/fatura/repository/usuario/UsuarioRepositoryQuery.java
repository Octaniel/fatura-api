package com.imposto.fatura.repository.usuario;

import com.imposto.fatura.model.Usuario;
import com.imposto.fatura.repository.filter.UsuarioFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioRepositoryQuery {
    Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable);
}
