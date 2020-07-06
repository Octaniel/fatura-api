package com.imposto.fatura.resource;

import com.imposto.fatura.model.Grupo;
import com.imposto.fatura.repository.GrupoRepository;
import com.imposto.fatura.service.GrupoService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/grupo")
public class GrupoResource {

    private final GrupoService grupoService;

    private final GrupoRepository grupoRepository;

    public GrupoResource(GrupoService grupoService, GrupoRepository grupoRepository) {
        this.grupoService = grupoService;
        this.grupoRepository = grupoRepository;
    }


    @PutMapping("/{id}")
    public Grupo atualizar(@PathVariable Integer id, @RequestBody @Valid Grupo grupo) {
        return grupoService.atualizar(id, grupo);
    }

    @GetMapping
    public List<Grupo> listar() {
        return grupoRepository.findAll();
    }
}
