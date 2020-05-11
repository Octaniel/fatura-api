package com.imposto.fatura.resource;

import com.imposto.fatura.model.Empresa;
import com.imposto.fatura.repository.EmpresaRepository;
import com.imposto.fatura.repository.empresa.EmpresaRepositoryimpl;
import com.imposto.fatura.repository.filter.EmpresaFilter;
import com.imposto.fatura.repository.projection.EmpresaResumo;
import com.imposto.fatura.service.EmpresaService;
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
@RequestMapping("/empresa")
public class EmpresaResource {

    private final EmpresaRepository empresaRepository;

    private final EmpresaRepositoryimpl empresaRepositoryimpl;

    private final EmpresaService empresaService;

    public EmpresaResource(EmpresaRepository empresaRepository, EmpresaRepositoryimpl empresaRepositoryimpl, EmpresaService empresaService) {
        this.empresaRepository = empresaRepository;
        this.empresaRepositoryimpl = empresaRepositoryimpl;
        this.empresaService = empresaService;
    }

    @GetMapping("listar")
    public List<Empresa> listar(){
        return empresaRepository.findAll();
    }

    @GetMapping
    public Page<EmpresaResumo> listarTabela(EmpresaFilter empresaFilter, Pageable pageable){
        return empresaRepositoryimpl.listarTabela(empresaFilter,pageable);
    }

    @GetMapping("/{id}")
    public Empresa listarUma(@PathVariable Integer id){
        Optional<Empresa> byId = empresaRepository.findById(id);
        if(byId.isEmpty()){
            throw new EmpresaException("Não existe nenhuma empresa com este id");
        }
        return byId.get();
    }

    @PostMapping
    public ResponseEntity<Empresa> salvar(@Valid @RequestBody Empresa empresa, HttpServletResponse httpServletResponse){
        return empresaService.salvar(empresa,httpServletResponse);
    }

    @PutMapping("/{id}")
    public Empresa atualizar(@PathVariable Integer id,@Valid @RequestBody Empresa empresa){
        return empresaService.atualizar(id,empresa);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Integer id){
        empresaRepository.deleteById(id);
    }

}
