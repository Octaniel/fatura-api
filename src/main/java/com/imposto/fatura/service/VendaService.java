package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.ItemProdutoRepository;
import com.imposto.fatura.repository.ProdutoRepository;
import com.imposto.fatura.repository.UsuarioRepository;
import com.imposto.fatura.repository.VendaRepository;
import com.imposto.fatura.service.exeption.UsuarioException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class VendaService {

    private final ProdutoRepository produtoRepository;

    private final ApplicationEventPublisher publisher;

    private final VendaRepository vendaRepository;

    private final ItemProdutoRepository itemProdutoRepository;


    public VendaService(ProdutoRepository produtoRepository, ApplicationEventPublisher publisher, VendaRepository vendaRepository, ItemProdutoRepository itemProdutoRepository) {
        this.produtoRepository = produtoRepository;
        this.publisher = publisher;
        this.vendaRepository = vendaRepository;
        this.itemProdutoRepository = itemProdutoRepository;
    }

    public ResponseEntity<Venda> salvar(Venda venda, HttpServletResponse httpServletResponse) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Produto> produtos = venda.getProdutos();
        venda.setProdutos(null);
        venda.setDataAlteracao(localDateTime);
        venda.setDataCriacao(localDateTime);
        AtomicReference<Double> valor = new AtomicReference<>(0d);
        AtomicReference<Double> valorImposto = new AtomicReference<>(0d);
        AtomicReference<Double> valorTotal = new AtomicReference<>(0d);
        produtos.forEach(x1 -> {
            Produto one = produtoRepository.getOne(x1.getId());
            if (one.getStock() < x1.getQuantidadeItem() && one.getNatureza().equals(TipoOferta.PRODUTO))
                throw new UsuarioException("Só existe " + one.getStock() + " de " + one.getNome() + " para venda");
        });
        Venda save = vendaRepository.save(venda);
        produtos.forEach(x -> {
            Produto one = produtoRepository.getOne(x.getId());
            IdItemproduto idItemproduto = new IdItemproduto();
            ItemProduto itemProduto = new ItemProduto();
            itemProduto.setProduto(x);
            itemProduto.setVenda(venda);
            idItemproduto.setProduto(x);
            idItemproduto.setVenda(venda);
            itemProduto.setId(idItemproduto);
            itemProduto.setValor(one.getPrecoVenda());
            itemProduto.setValorImposto(one.getPrecoVenda() * one.getTaxa().getValor() / 100);
            itemProduto.setValorTotal(one.getPrecoVenda() * x.getQuantidadeItem());
            valor.updateAndGet(v -> v + itemProduto.getValor());
            valorImposto.updateAndGet(v -> v + itemProduto.getValorImposto());
            valorTotal.updateAndGet(v -> v + itemProduto.getValorTotal());
            itemProduto.setQuantidade(x.getQuantidadeItem());
            if (one.getNatureza().equals(TipoOferta.PRODUTO)) one.setStock(one.getStock() - x.getQuantidadeItem());
            produtoRepository.save(one);
            itemProdutoRepository.save(itemProduto);
        });
        save.setValor(valor.get());
        save.setValorImposto(valorImposto.get());
        save.setValorTotal(valorTotal.get());
        Venda save1 = vendaRepository.save(save);
        publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, save1.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save1);
    }

}
