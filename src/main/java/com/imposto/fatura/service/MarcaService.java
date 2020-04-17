package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Marca;
import com.imposto.fatura.model.Modelo;
import com.imposto.fatura.repository.MarcaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MarcaService {

    private final ApplicationEventPublisher publisher;

    private MarcaRepository marcaRepository;

    public MarcaService(ApplicationEventPublisher publisher, MarcaRepository marcaRepository) {
        this.publisher = publisher;
        this.marcaRepository = marcaRepository;
    }

    public ResponseEntity<Marca> salvar(Marca marca, HttpServletResponse httpServletResponse){
        LocalDateTime localDateTime = LocalDateTime.now();
        marca.setDataAlteracao(localDateTime);
        marca.setDataCriacao(localDateTime);
        Marca save = marcaRepository.save(marca);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    public Marca atualizar(Marca marca, Integer id){
        Optional<Marca> byId = marcaRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(marca,byId.orElse(null),"id","dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return marcaRepository.save(byId.get());
    }
}
