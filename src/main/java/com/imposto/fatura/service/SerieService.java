package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Produto;
import com.imposto.fatura.model.Serie;
import com.imposto.fatura.repository.SerieRepository;
import com.imposto.fatura.service.exeption.UsuarioException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SerieService {
    private final SerieRepository serieRepository;
    private final ApplicationEventPublisher publisher;

    public SerieService(SerieRepository serieRepository, ApplicationEventPublisher publisher) {
        this.serieRepository = serieRepository;
        this.publisher = publisher;
    }

    public ResponseEntity<Serie> salvar(Serie serie, HttpServletResponse httpServletResponse){
        validar(serie, 0);
        LocalDateTime localDateTime = LocalDateTime.now();
        serie.setDataAlteracao(localDateTime);
        serie.setDataCriacao(localDateTime);
        Serie save = serieRepository.save(serie);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    private void validar(Serie serie, Integer id) {
        List<Serie> all = serieRepository.findAll();
        String unicoNova=serie.getTipoDocumento().getSigla()+serie.getNumero()+serie.getAno();
        System.out.println(unicoNova);
        all.forEach(x->{
            String unicoBase=x.getTipoDocumento().getSigla()+x.getNumero()+x.getAno();
            System.out.println(unicoBase);
            if (unicoBase.equals(unicoNova)&&x.getId()!=serie.getId()) throw new UsuarioException("Serie existente");
            if (x.getNumeroAutorizacao().equals(serie.getNumeroAutorizacao())&&x.getId()!=serie.getId()) throw new UsuarioException("Por favor confirme o seu número de autorização");
        });
    }

    public Serie atualizar(Serie serie, Integer id){
        validar(serie, id);
        Optional<Serie> byId = serieRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(serie,byId.orElse(null),"id","dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return serieRepository.save(byId.get());
    }
}
