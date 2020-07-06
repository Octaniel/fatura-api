package com.imposto.fatura.resource;

import com.imposto.fatura.model.Compra;
import com.imposto.fatura.model.Empresa;
import com.imposto.fatura.repository.CompraRepository;
import com.imposto.fatura.repository.compra.CompraCustomRepository;
import com.imposto.fatura.repository.filter.CompraFilter;
import com.imposto.fatura.repository.projection.CompraResumo;
import com.imposto.fatura.service.CompraService;
import com.imposto.fatura.service.exeption.EmpresaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compra")
public class CompraResource {

    private final CompraRepository compraRepository;

    private final CompraService compraService;

    private final CompraCustomRepository compraCustomRepository;

    public CompraResource(CompraRepository compraRepository, CompraService compraService, CompraCustomRepository compraCustomRepository) {
        this.compraRepository = compraRepository;
        this.compraService = compraService;
        this.compraCustomRepository = compraCustomRepository;
    }

    @GetMapping("test")
    public List<Compra> listarTeste() {
        return compraRepository.findAll();
    }

    @GetMapping
    public Page<CompraResumo> listar(CompraFilter compraFilter, Pageable pageable) {
        return compraCustomRepository.listar(compraFilter, pageable);
    }

    @GetMapping("/{id}")
    public Compra listarUma(@PathVariable Integer id) {
        Optional<Compra> byId = compraRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EmpresaException("Não existe nenhuma compra com este id");
        }
        return byId.get();
    }

    @PostMapping
    public ResponseEntity<Compra> save(@Valid @RequestBody Compra compra, HttpServletResponse httpServletResponse) {
        return compraService.salvar(compra, httpServletResponse);
    }

    @PutMapping("/{id}")
    public Compra atualizar(@Valid @RequestBody Compra compra, @PathVariable Integer id) {
        return compraService.atualizar(compra, id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("/{id}")
    public void apagar(@PathVariable Integer id) {
        compraRepository.deleteById(id);
    }
}
