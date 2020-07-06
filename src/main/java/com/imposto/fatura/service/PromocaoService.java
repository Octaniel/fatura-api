package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Produto;
import com.imposto.fatura.model.Promocao;
import com.imposto.fatura.repository.ProdutoRepository;
import com.imposto.fatura.repository.PromocaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PromocaoService {

    private final ApplicationEventPublisher publisher;

    private final PromocaoRepository promocaoRepository;

    private final ProdutoRepository produtoRepository;

    public PromocaoService(ApplicationEventPublisher publisher, PromocaoRepository promocaoRepository, ProdutoRepository produtoRepository) {
        this.publisher = publisher;
        this.promocaoRepository = promocaoRepository;
        this.produtoRepository = produtoRepository;
    }

    public ResponseEntity<Promocao> salvar(Promocao promocao, HttpServletResponse httpServletResponse) {
        LocalDateTime localDateTime = LocalDateTime.now();
        promocao.setDataAlteracao(localDateTime);
        promocao.setDataCriacao(localDateTime);
        Produto one = produtoRepository.getOne(promocao.getProduto().getId());
        Promocao save = promocaoRepository.save(promocao);
        one.setPrecoVenda(save.getValor());
        one.setEmPromocao(true);
        produtoRepository.save(one);
        publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void tirarPromocao() {
        List<Promocao> all = promocaoRepository.findAll();
        all.forEach(x -> {
            if (x.getDataFim().equals(LocalDate.now())) {
                Produto one = x.getProduto();
                one.setEmPromocao(false);
                produtoRepository.save(one);
            }
        });
    }

    public Promocao atualizar(Promocao promocao, Integer id) {
        Produto one = produtoRepository.getOne(promocao.getProduto().getId());
        if (!promocao.getStatus()) {
            one.setEmPromocao(false);
            one.setPrecoVenda(one.getValorInalteravel());
        } else {
            one.setPrecoVenda(promocao.getValor());
        }
        produtoRepository.save(one);
        Optional<Promocao> byId = promocaoRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(promocao, byId.orElse(null), "id", "dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return promocaoRepository.save(byId.get());
    }
}
