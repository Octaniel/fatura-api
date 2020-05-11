package com.imposto.fatura.resource;

import com.imposto.fatura.model.Modelo;
import com.imposto.fatura.model.Promocao;
import com.imposto.fatura.repository.PromocaoRepository;
import com.imposto.fatura.service.PromocaoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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

    @PostMapping
    public ResponseEntity<Promocao> salvar(@RequestBody @Valid Promocao promocao, HttpServletResponse httpServletResponse){
        return promocaoService.salvar(promocao,httpServletResponse);
    }

    @PutMapping("/{id}")
    public Promocao atualizar(@RequestBody @Valid Promocao promocao, @PathVariable Integer id){
        return promocaoService.atualizar(promocao, id);
    }
}
