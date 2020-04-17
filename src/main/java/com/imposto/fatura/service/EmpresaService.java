package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Empresa;
import com.imposto.fatura.repository.EmpresaRepository;
import com.imposto.fatura.service.exeption.EmpresaException;
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
public class EmpresaService {

    private final ApplicationEventPublisher publisher;

    private final EmpresaRepository empresaRepository;

    public EmpresaService(ApplicationEventPublisher publisher, EmpresaRepository empresaRepository) {
        this.publisher = publisher;
        this.empresaRepository = empresaRepository;
    }

    public ResponseEntity<Empresa> salvar(Empresa empresa, HttpServletResponse httpServletResponse){
        validarEmpresa(empresa, 0);
        LocalDateTime localDateTime = LocalDateTime.now();
        empresa.setDataAlteracoa(localDateTime);
        empresa.setDataCriacao(localDateTime);
        Empresa save = empresaRepository.save(empresa);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    private void validarEmpresa(Empresa empresa, Integer id) {
        if(empresa.getNif().toString().length()!=9) throw new EmpresaException("NIF deve ter 9 digitos");
        List<Empresa> lista = empresaRepository.findAll();
        lista.forEach(x ->{
            if(x.getNif().equals(empresa.getNif())&&!x.getId().equals(id)){
                    throw new EmpresaException("NIF existente");
            }
            if(x.getEmail().equals(empresa.getEmail())&&!x.getId().equals(id)){
                throw new EmpresaException("Este email já esta sendo utilizado por outra empresa");
            }
            if(x.getNome().equals(empresa.getNome())&&!x.getId().equals(id)){
                throw new EmpresaException("Este nome já esta sendo utilizado por outra empresa");
            }
            if(x.getNumeroCertificacao().equals(empresa.getNumeroCertificacao())&&!x.getId().equals(id)){
                throw new EmpresaException("Este número certificação já esta sendo utilizado por outra empresa");
            }
        });
    }
    public Empresa atualizar(Integer id, Empresa empresa){
        Optional<Empresa> byId = empresaRepository.findById(id);
        validarEmpresa(empresa, id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(empresa,byId.orElse(null),"id","dataCriacao");
        byId.get().setDataAlteracoa(LocalDateTime.now());
        return empresaRepository.save(byId.get());
    }
}
