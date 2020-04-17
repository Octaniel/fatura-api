package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.TipoProduto;
import com.imposto.fatura.repository.TipoProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TipoProdutoService {


    private final ApplicationEventPublisher publisher;

    private TipoProdutoRepository tipoProdutoRepository;

    public TipoProdutoService(ApplicationEventPublisher publisher, TipoProdutoRepository tipoProdutoRepository) {
        this.publisher = publisher;
        this.tipoProdutoRepository = tipoProdutoRepository;
    }

    public ResponseEntity<TipoProduto> salvar(TipoProduto tipoProduto, HttpServletResponse httpServletResponse){
        LocalDateTime localDateTime = LocalDateTime.now();
        tipoProduto.setDataAlteracao(localDateTime);
        tipoProduto.setDataCriacao(localDateTime);
        TipoProduto save = tipoProdutoRepository.save(tipoProduto);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    public TipoProduto atualizar(TipoProduto tipoProduto, Integer id){
        Optional<TipoProduto> byId = tipoProdutoRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(tipoProduto,byId.orElse(null),"id","dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return tipoProdutoRepository.save(byId.get());
    }
}
