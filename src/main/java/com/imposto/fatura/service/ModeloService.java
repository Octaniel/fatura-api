package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Modelo;
import com.imposto.fatura.model.Produto;
import com.imposto.fatura.repository.ModeloRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ModeloService {
    private final ApplicationEventPublisher publisher;

    private ModeloRepository modeloRepository;

    public ModeloService(ApplicationEventPublisher publisher, ModeloRepository modeloRepository) {
        this.publisher = publisher;
        this.modeloRepository = modeloRepository;
    }

    public ResponseEntity<Modelo> salvar(Modelo modelo, HttpServletResponse httpServletResponse) {
        LocalDateTime localDateTime = LocalDateTime.now();
        modelo.setDataAlteracao(localDateTime);
        modelo.setDataCriacao(localDateTime);
        Modelo save = modeloRepository.save(modelo);
        publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    public Modelo atualizar(Modelo modelo, Integer id) {
        Optional<Modelo> byId = modeloRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(modelo, byId.orElse(null), "id", "dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return modeloRepository.save(byId.get());
    }

}
