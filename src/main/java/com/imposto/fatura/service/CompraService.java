package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Compra;
import com.imposto.fatura.model.Produto;
import com.imposto.fatura.repository.CompraRepository;
import com.imposto.fatura.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CompraService {

    private final ApplicationEventPublisher publisher;

    private final CompraRepository compraRepository;

    private final ProdutoRepository produtoRepository;

    public CompraService(ApplicationEventPublisher publisher, CompraRepository compraRepository, ProdutoRepository produtoRepository) {
        this.publisher = publisher;
        this.compraRepository = compraRepository;
        this.produtoRepository = produtoRepository;
    }

    public ResponseEntity<Compra> salvar(Compra compra, HttpServletResponse httpServletResponse){
        LocalDateTime localDateTime = LocalDateTime.now();
        Produto one = produtoRepository.getOne(compra.getProduto().getId());
        one.setStock(one.getStock()+compra.getQuantidade());
        compra.setDataAlteracao(localDateTime);
        compra.setDataCriacao(localDateTime);
        produtoRepository.save(one);
        Compra save = compraRepository.save(compra);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    public Compra atualizar(Compra compra, Integer id){
        Optional<Compra> byId = compraRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(compra,byId.orElse(null),"id","dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return compraRepository.save(byId.get());
    }
}
