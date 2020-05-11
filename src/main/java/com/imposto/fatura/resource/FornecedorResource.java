package com.imposto.fatura.resource;

import com.imposto.fatura.model.Fornecedor;
import com.imposto.fatura.repository.FornecedorRepository;
import com.imposto.fatura.repository.filter.FornecedorFilter;
import com.imposto.fatura.repository.projection.FornecedorResumo;
import com.imposto.fatura.service.FornecedorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorResource {

    private final FornecedorService fornecedorService;

    private final FornecedorRepository fornecedorRepository;

    public FornecedorResource(FornecedorService fornecedorService, FornecedorRepository fornecedorRepository) {
        this.fornecedorService = fornecedorService;
        this.fornecedorRepository = fornecedorRepository;
    }

    @GetMapping("list")
    public List<Fornecedor> lista(){
        return fornecedorRepository.findAll();
    }

    @GetMapping("listar")
    public List<FornecedorResumo> listar(FornecedorFilter fornecedorFilter){
        return fornecedorRepository.resumoListar(fornecedorFilter);
    }

    @PostMapping
    public ResponseEntity<Fornecedor> salvar (@RequestBody @Valid Fornecedor fornecedor, HttpServletResponse httpServletResponse){
        return fornecedorService.salvar(fornecedor,httpServletResponse);
    }

    @GetMapping
    public Page<FornecedorResumo> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable){
        return fornecedorRepository.resumo(fornecedorFilter,pageable);
    }

    @GetMapping("/{id}")
    public Fornecedor filtrar(@PathVariable Integer id){
        return fornecedorRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Fornecedor atualizar(@PathVariable Integer id, @RequestBody @Valid Fornecedor fornecedor){
        return fornecedorService.atualizar(id,fornecedor);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Integer id){
        fornecedorRepository.deleteById(id);
    }
}
