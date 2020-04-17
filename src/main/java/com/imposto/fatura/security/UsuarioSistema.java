package com.imposto.fatura.security;

import com.imposto.fatura.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UsuarioSistema extends User {

    private Usuario usuario;
    public UsuarioSistema(Usuario usu, Collection<? extends GrantedAuthority> authorities) {
        super(usu.getEmail(), usu.getSenha(), authorities);
        usuario=usu;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
