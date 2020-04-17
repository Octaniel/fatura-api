package com.imposto.fatura.resource;

import com.imposto.fatura.model.Produto;
import com.imposto.fatura.model.Serie;
import com.imposto.fatura.model.TipoDocumento;
import com.imposto.fatura.repository.SerieRepository;
import com.imposto.fatura.repository.TipoDocumentoRepository;
import com.imposto.fatura.repository.serie.SerieRepositoryCustom;
import com.imposto.fatura.service.SerieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("serie")
public class SerieResource {

    private SerieRepository serieRepository;

    private SerieRepositoryCustom serieRepositoryCustom;

    private SerieService serieService;

    private TipoDocumentoRepository tipoDocumentoRepository;

    public SerieResource(SerieRepository serieRepository, SerieRepositoryCustom serieRepositoryCustom, SerieService serieService, TipoDocumentoRepository tipoDocumentoRepository) {
        this.serieRepository = serieRepository;
        this.serieRepositoryCustom = serieRepositoryCustom;
        this.serieService = serieService;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    @GetMapping("listar")
    public List<Serie> listar(Integer idEmpresa){
        return serieRepositoryCustom.resumo(idEmpresa);
    }

    @GetMapping
    public Page<Serie> filtrar(Integer idEmpresa, Pageable pageable){
       return serieRepositoryCustom.filtrar(idEmpresa,pageable);
    }

    @PostMapping
    public ResponseEntity<Serie> salvar(@RequestBody @Valid Serie serie, HttpServletResponse httpServletResponse){
        serie.setPreDefinida(false);
        return serieService.salvar(serie,httpServletResponse);
    }

    @GetMapping("tipoDocumento/listar")
    public List<TipoDocumento> listar(){
        return tipoDocumentoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Serie atualizar(@PathVariable Integer id){
        return serieRepository.findById(id).orElse(null);
    }

    @PutMapping("predefinir/{id}")
    public void atualizarPredifinir(@PathVariable Integer id, Integer idEmpresa){
        serieRepositoryCustom.resumo(idEmpresa).forEach(x->{
            x.setPreDefinida(false);
            serieRepository.save(x);
        });
        Serie one = serieRepository.getOne(id);
        one.setPreDefinida(true);
        serieRepository.save(one);
    }

    @PutMapping("/{id}")
    public Serie atualizar(@RequestBody @Valid Serie serie, @PathVariable Integer id){
        return serieService.atualizar(serie, id);
    }
}
