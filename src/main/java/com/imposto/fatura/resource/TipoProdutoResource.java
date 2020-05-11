package com.imposto.fatura.resource;

import com.imposto.fatura.model.TipoProduto;
import com.imposto.fatura.repository.TipoProdutoRepository;
import com.imposto.fatura.repository.filter.TipoProdutoFilter;
import com.imposto.fatura.repository.tipoProduto.TipoProdutoRepositoryCustom;
import com.imposto.fatura.service.TipoProdutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("tipoProduto")
public class TipoProdutoResource {


    private final TipoProdutoRepositoryCustom tipoProdutoRepositoryCustom;
    private final TipoProdutoService tipoProdutoService;
    private final TipoProdutoRepository tipoProdutoRepository;

    public TipoProdutoResource(TipoProdutoRepositoryCustom tipoProdutoRepositoryCustom, TipoProdutoService tipoProdutoService, TipoProdutoRepository tipoProdutoRepository) {
        this.tipoProdutoRepositoryCustom = tipoProdutoRepositoryCustom;
        this.tipoProdutoService = tipoProdutoService;
        this.tipoProdutoRepository = tipoProdutoRepository;
    }

    @GetMapping("listar")
    public List<TipoProduto> listar(TipoProdutoFilter tipoProdutoFilter){
        return tipoProdutoRepositoryCustom.listar(tipoProdutoFilter);
    }

    @GetMapping
    public Page<TipoProduto> filtrar(TipoProdutoFilter tipoProdutoFilter, Pageable pageable){
        return tipoProdutoRepositoryCustom.filtrar(tipoProdutoFilter,pageable);
    }

    @PostMapping
    public ResponseEntity<TipoProduto> salvar(@RequestBody @Valid TipoProduto tipoProduto, HttpServletResponse httpServletResponse){
        return tipoProdutoService.salvar(tipoProduto,httpServletResponse);
    }

    @PutMapping("/{id}")
    public TipoProduto atualizar(@RequestBody @Valid TipoProduto tipoProduto, @PathVariable Integer id){
        return tipoProdutoService.atualizar(tipoProduto, id);
    }

    @GetMapping("/{id}")
    public TipoProduto atualizar(@PathVariable Integer id){
        return tipoProdutoRepository.findById(id).orElse(null);
    }
}
