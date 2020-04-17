package com.imposto.fatura.resource;

import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.ItemProdutoRepository;
import com.imposto.fatura.repository.VendaRepository;
import com.imposto.fatura.repository.filter.DocumentoFilter;
import com.imposto.fatura.repository.filter.VendaFilter;
import com.imposto.fatura.repository.projection.VendaResumo;
import com.imposto.fatura.repository.projection.VendaResumoPro;
import com.imposto.fatura.service.FaturaService;
import com.imposto.fatura.service.VendaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/venda")
public class VendaResource {

    private final VendaRepository vendaRepository;

    private final VendaService vendaService;

    private ItemProdutoRepository itemProdutoRepository;

    private FaturaService faturaService;

    public VendaResource(VendaRepository vendaRepository, VendaService vendaService, ItemProdutoRepository itemProdutoRepository, FaturaService faturaService) {
        this.vendaRepository = vendaRepository;
        this.vendaService = vendaService;
        this.itemProdutoRepository = itemProdutoRepository;
        this.faturaService = faturaService;
    }

    @PostMapping
    public ResponseEntity<Venda> salvar(@Valid @RequestBody Venda venda, HttpServletResponse httpServletResponse){
        return vendaService.salvar(venda, httpServletResponse);
    }

    @GetMapping
    public Page<VendaResumo> filtrar(VendaFilter vendaFilter, Pageable pageable){
        return vendaRepository.resumo(vendaFilter,pageable);
    }

    @GetMapping("ult/{idSerie}")
    public Venda ultimaVenda(@PathVariable Integer idSerie){
        List<Venda> allBySerieNumero = vendaRepository.findAllBySerieId(idSerie);
        if (allBySerieNumero.size()>0)
       return allBySerieNumero.get(allBySerieNumero.size()-1);
        else return new Venda();
    }

    @GetMapping("fatura/ficheiro")
    public ResponseEntity<byte[]> paraDoc(DocumentoFilter documentoFilter){
        byte[] bytes = faturaService.paraDoc(documentoFilter);
        System.out.println(bytes[0]);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(bytes);
    }

    @GetMapping("/{id}")
    public VendaResumoPro listarPorId(@PathVariable Integer id){
        List<ItemProduto> itemProdutos = new ArrayList<>();
        VendaResumoPro vendaResumoPro = vendaRepository.resumoPro(id);
        IdItemproduto idItemproduto = new IdItemproduto();
        Venda one = vendaRepository.getOne(id);
        one.getProdutos().forEach(x->{
            idItemproduto.setProduto(x);
            idItemproduto.setVenda(one);
            Optional<ItemProduto> byId = itemProdutoRepository.findById(idItemproduto);
            itemProdutos.add(byId.orElse(null));
        });
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        vendaResumoPro.setItemProdutos(itemProdutos);
        return vendaResumoPro;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("/{id}") 
    public void remover(@PathVariable Integer id){
        vendaRepository.deleteById(id);
    }


}
