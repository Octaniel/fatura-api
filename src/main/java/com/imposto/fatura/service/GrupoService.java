package com.imposto.fatura.service;

import com.imposto.fatura.model.Grupo;
import com.imposto.fatura.repository.GrupoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public Grupo atualizar(Integer id, Grupo grupo) {
        Optional<Grupo> byId = grupoRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(grupo, byId.orElse(null), "id");
        return grupoRepository.save(byId.get());
    }
}
