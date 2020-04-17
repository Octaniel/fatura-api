package com.imposto.fatura.resource;

import com.imposto.fatura.model.MotivoIsencao;
import com.imposto.fatura.model.Produto;
import com.imposto.fatura.model.Promocao;
import com.imposto.fatura.model.Taxa;
import com.imposto.fatura.repository.MotivoIsencaoRepository;
import com.imposto.fatura.repository.ProdutoRepository;
import com.imposto.fatura.repository.TaxaRepository;
import com.imposto.fatura.repository.filter.ProdutoFilter;
import com.imposto.fatura.service.ProdutoService;
import com.imposto.fatura.service.PromocaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/produto")

public class ProdutoResource {

    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;
    private final PromocaoService promocaoService;
    private final TaxaRepository taxaRepository;
    private final MotivoIsencaoRepository motivoIsencaoRepository;

    public ProdutoResource(ProdutoRepository produtoRepository, ProdutoService produtoService, PromocaoService promocaoService, TaxaRepository taxaRepository, MotivoIsencaoRepository motivoIsencaoRepository) {
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
        this.promocaoService = promocaoService;
        this.taxaRepository = taxaRepository;
        this.motivoIsencaoRepository = motivoIsencaoRepository;
    }

    @GetMapping("teste")
    public List<Produto> listar(){
        return produtoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody @Valid Produto produto, HttpServletResponse httpServletResponse){
       return produtoService.salvar(produto,httpServletResponse);
    }

    @GetMapping
    public Page<?> filtrar(ProdutoFilter produtoFilter, Pageable pageable){
        return produtoRepository.resumo(produtoFilter,pageable);
    }

    @GetMapping("taxa")
    public List<Taxa> listarTaxa(){
        return taxaRepository.findAll();
    }

    @GetMapping("motivoIsencao")
    public List<MotivoIsencao> listarMotivo(){
        return motivoIsencaoRepository.findAll();
    }

    @PutMapping("/{id}")
    public Produto atualizar(@RequestBody @Valid Produto produto, @PathVariable Integer id){
        return produtoService.atualizar(produto, id);
    }

    @GetMapping("/{id}")
    public Produto atualizar(@PathVariable Integer id){
        return produtoRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void remover(@PathVariable Integer id){
        Produto one = produtoRepository.getOne(id);
        one.setStatus(false);
        produtoRepository.save(one);
    }

    @PutMapping("/promocao/{idProduto}")
    public ResponseEntity<Promocao> porEmPromomocao(@RequestBody @Valid Promocao promocao, @PathVariable Integer idProduto, HttpServletResponse httpServletResponse){
        return promocaoService.salvar(promocao,idProduto,httpServletResponse);
    }
}
