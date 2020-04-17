package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Fornecedor;
import com.imposto.fatura.model.Usuario;
import com.imposto.fatura.repository.FornecedorRepository;
import com.imposto.fatura.repository.UsuarioRepository;
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
public class FornecedorService {

    private final ApplicationEventPublisher publisher;

    private final FornecedorRepository fornecedorRepository;

    private final UsuarioRepository usuarioRepository;

    public FornecedorService(ApplicationEventPublisher publisher, FornecedorRepository fornecedorRepository, UsuarioRepository usuarioRepository) {
        this.publisher = publisher;
        this.fornecedorRepository = fornecedorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<Fornecedor> salvar(Fornecedor fornecedor, HttpServletResponse httpServletResponse) {
        validar(fornecedor, 0);
        LocalDateTime localDateTime = LocalDateTime.now();
        fornecedor.setDataAlteracao(localDateTime);
        fornecedor.setDataCriacao(localDateTime);
        Fornecedor save = fornecedorRepository.save(fornecedor);
        publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    private void validar(Fornecedor fornecedor, Integer id) {
        List<Fornecedor> all = fornecedorRepository.findAll();
        all.forEach(x -> {
            Optional<Usuario> byId = usuarioRepository.findById(fornecedor.getUsuarioCriouId());
            Optional<Usuario> byId1 = usuarioRepository.findById(x.getUsuarioCriouId());
            assert byId.orElse(null) != null;
            assert byId1.orElse(null) != null;
            if (fornecedor.getNome().equals(x.getNome()) && !x.getId().equals(id) &&byId.orElse(null).getEmpresa().equals(byId1.orElse(null).getEmpresa()))
                throw new UsuarioException("Este nome já esta sendo utilizado por outro fornecedor");
            if (fornecedor.getNif().equals(x.getNif()) && !x.getId().equals(id) &&byId.get().getEmpresa().equals(byId1.orElse(null).getEmpresa()))
                throw new UsuarioException("Este nif já esta sendo utilizado por outro fornecedor");
        });
    }

    public Fornecedor atualizar(Integer id, Fornecedor fornecedor) {
        System.out.println(fornecedor.getUsuarioCriouId());
        validar(fornecedor, id);
        Optional<Fornecedor> byId = fornecedorRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(fornecedor, byId.orElse(null), "id", "dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return fornecedorRepository.save(byId.get());
    }
}
