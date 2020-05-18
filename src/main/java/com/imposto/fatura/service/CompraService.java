package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.CompraRepository;
import com.imposto.fatura.repository.ItemProdutoCompraRepository;
import com.imposto.fatura.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CompraService {

    private final ApplicationEventPublisher publisher;

    private final CompraRepository compraRepository;

    private final ProdutoRepository produtoRepository;

    private final ItemProdutoCompraRepository itemProdutoCompraRepository;

    public CompraService(ApplicationEventPublisher publisher, CompraRepository compraRepository, ProdutoRepository produtoRepository, ItemProdutoCompraRepository itemProdutoCompraRepository) {
        this.publisher = publisher;
        this.compraRepository = compraRepository;
        this.produtoRepository = produtoRepository;
        this.itemProdutoCompraRepository = itemProdutoCompraRepository;
    }

    /*public ResponseEntity<Compra> salvar1(Compra compra, HttpServletResponse httpServletResponse){
        LocalDateTime localDateTime = LocalDateTime.now();
        Produto one = produtoRepository.getOne(compra.getProduto().getId());
        one.setStock(one.getStock()+compra.getQuantidade());
        compra.setDataAlteracao(localDateTime);
        compra.setDataCriacao(localDateTime);
        produtoRepository.save(one);
        Compra save = compraRepository.save(compra);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }*/

    public ResponseEntity<Compra> salvar(Compra compra, HttpServletResponse httpServletResponse) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Produto> produtos = compra.getProdutos();
        compra.setProdutos(null);
        compra.setDataAlteracao(localDateTime);
        compra.setDataCriacao(localDateTime);
        AtomicReference<Double> valor = new AtomicReference<>(0d);
        AtomicReference<Double> valorImposto = new AtomicReference<>(0d);
        AtomicReference<Double> valorTotal = new AtomicReference<>(0d);
        Compra save = compraRepository.save(compra);
        produtos.forEach(x -> {
            Produto one = produtoRepository.getOne(x.getId());
            IdItemProdutoCompra idItemproduto = new IdItemProdutoCompra();
            ItemProdutoCompra itemProduto = new ItemProdutoCompra();
            idItemproduto.setProduto(x);
            idItemproduto.setCompra(compra);
            itemProduto.setId(idItemproduto);
            itemProduto.setValor(one.getPrecoVenda());
            itemProduto.setValorImposto(one.getPrecoVenda() * one.getTaxa().getValor() / 100);
            itemProduto.setValorTotal(one.getPrecoVenda() * x.getQuantidadeItem());
            valor.updateAndGet(v -> v + itemProduto.getValor());
            valorImposto.updateAndGet(v -> v + itemProduto.getValorImposto());
            valorTotal.updateAndGet(v -> v + itemProduto.getValorTotal());
            itemProduto.setQuantidade(x.getQuantidadeItem());
            one.setStock(one.getStock()+x.getQuantidadeItem());
            produtoRepository.save(one);
            itemProdutoCompraRepository.save(itemProduto);
        });
        save.setValor(valor.get());
        Compra save1 = compraRepository.save(save);
        publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, save1.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save1);
    }

    public Compra atualizar(Compra compra, Integer id){
        Optional<Compra> byId = compraRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(compra,byId.orElse(null),"id","dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return compraRepository.save(byId.get());
    }
}
