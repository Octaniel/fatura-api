package com.imposto.fatura.service.exeption;

public class UsuarioException extends RuntimeException {

    public UsuarioException(String erro){
        super(erro);
    }
}
