package com.imposto.fatura.resource;

import com.imposto.fatura.model.Marca;
import com.imposto.fatura.repository.MarcaRepository;
import com.imposto.fatura.repository.filter.MarcaFilter;
import com.imposto.fatura.repository.marca.MarcaRepositoryCustom;
import com.imposto.fatura.service.MarcaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("marca")
public class MarcaResouce {

    private final MarcaRepositoryCustom marcaRepositoryCustom;
    private final MarcaService marcaService;
    private final MarcaRepository marcaRepository;

    public MarcaResouce(MarcaRepositoryCustom marcaRepositoryCustom, MarcaService marcaService, MarcaRepository marcaRepository) {
        this.marcaRepositoryCustom = marcaRepositoryCustom;
        this.marcaService = marcaService;
        this.marcaRepository = marcaRepository;
    }

    @GetMapping("listar")
    public List<Marca> listar(MarcaFilter marcaFilter) {
        return marcaRepositoryCustom.listar(marcaFilter);
    }

    @GetMapping
    public Page<Marca> filtrar(MarcaFilter marcaFilter, Pageable pageable) {
        return marcaRepositoryCustom.filtrar(marcaFilter, pageable);
    }

    @PostMapping
    public ResponseEntity<Marca> salvar(@RequestBody @Valid Marca marca, HttpServletResponse httpServletResponse) {
        return marcaService.salvar(marca, httpServletResponse);
    }

    @PutMapping("/{id}")
    public Marca atualizar(@RequestBody @Valid Marca marca, @PathVariable Integer id) {
        return marcaService.atualizar(marca, id);
    }

    @GetMapping("/{id}")
    public Marca listarPorId(@PathVariable Integer id) {
        return marcaRepository.findById(id).orElse(null);
    }
}
