package com.imposto.fatura.resource;

import com.imposto.fatura.model.Promocao;
import com.imposto.fatura.repository.PromocaoRepository;
import com.imposto.fatura.service.PromocaoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/promocao")
public class PromocaoResource {

    private final PromocaoRepository promocaoRepository;

    private final PromocaoService promocaoService;

    public PromocaoResource(PromocaoRepository promocaoRepository, PromocaoService promocaoService) {
        this.promocaoRepository = promocaoRepository;
        this.promocaoService = promocaoService;
    }

    @GetMapping
    public List<Promocao> listar(){
        return promocaoRepository.findAll();
    }

    @GetMapping("/arquivo")
    public ResponseEntity<byte[]> arquivo(){
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(promocaoService.test());
    }


}
