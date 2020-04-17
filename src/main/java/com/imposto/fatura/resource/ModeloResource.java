package com.imposto.fatura.resource;

import com.imposto.fatura.model.Modelo;
import com.imposto.fatura.model.Produto;
import com.imposto.fatura.repository.ModeloRepository;
import com.imposto.fatura.repository.modelo.ModeloRepositoryCustom;
import com.imposto.fatura.service.ModeloService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("modelo")
public class ModeloResource {

    private ModeloRepositoryCustom modeloRepositoryCustom;
    private ModeloService modeloService;
    private ModeloRepository modeloRepository;

    public ModeloResource(ModeloRepositoryCustom modeloRepositoryCustom, ModeloService modeloService, ModeloRepository modeloRepository) {
        this.modeloRepositoryCustom = modeloRepositoryCustom;
        this.modeloService = modeloService;
        this.modeloRepository = modeloRepository;
    }

    @GetMapping("listar")
    public List<Modelo> listar(Integer idEmpresa){
       return modeloRepositoryCustom.listar(idEmpresa);
    }

    @GetMapping
    public Page<Modelo> filtrar(Integer idEmpresa, Pageable pageable){
        return modeloRepositoryCustom.filtrar(idEmpresa,pageable);
    }

    @PostMapping
    public ResponseEntity<Modelo> salvar(@RequestBody @Valid Modelo modelo, HttpServletResponse httpServletResponse){
        return modeloService.salvar(modelo,httpServletResponse);
    }

    @PutMapping("/{id}")
    public Modelo atualizar(@RequestBody @Valid Modelo modelo, @PathVariable Integer id){
        return modeloService.atualizar(modelo, id);
    }

    @GetMapping("/{id}")
    public Modelo findById(@PathVariable Integer id){
        return modeloRepository.findById(id).orElse(null);
    }

    @GetMapping("marca/{id}")
    public List<Modelo> atualizar(@PathVariable Integer id, Integer idEmpresa){
        return modeloRepositoryCustom.listarPorMarca(idEmpresa,id);
    }

}
