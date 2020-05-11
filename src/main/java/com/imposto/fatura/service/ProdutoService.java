package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Produto;
import com.imposto.fatura.model.Usuario;
import com.imposto.fatura.repository.ProdutoRepository;
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
public class ProdutoService {

    private final ApplicationEventPublisher publisher;

    private final ProdutoRepository produtoRepository;

    private final UsuarioRepository usuarioRepository;

    public ProdutoService(ApplicationEventPublisher publisher, ProdutoRepository produtoRepository, UsuarioRepository usuarioRepository) {
        this.publisher = publisher;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<Produto> salvar(Produto produto, HttpServletResponse httpServletResponse){
        validarCliente(produto,0);
        LocalDateTime localDateTime = LocalDateTime.now();
        produto.setDataAlteracao(localDateTime);
        produto.setDataCriacao(localDateTime);
        produto.setEmPromocao(false);
        if (produto.getModelo()!=null&&produto.getModelo().getId()==null) produto.setModelo(null);
        if (produto.getTaxa()!=null&&produto.getTaxa().getId()==null) produto.setTaxa(null);
        Produto save = produtoRepository.save(produto);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    private void validarCliente(Produto produto, Integer id) {
        produto.setValorInalteravel(produto.getPrecoVenda());
        List<Produto> lista = produtoRepository.findAll();
        lista.forEach(x ->{
            Optional<Usuario> byId = usuarioRepository.findById(produto.getUsuarioCriouId());
            Optional<Usuario> byId1 = usuarioRepository.findById(x.getUsuarioCriouId());
            assert byId.orElse(null) != null;
            assert byId1.orElse(null) != null;
            if (produto.getNatureza().getDescricao()=="produto"&&x.getNatureza().getDescricao()=="produto")
            if(x.getCodigo().equals(produto.getCodigo())
                    &&byId.orElse(null).getEmpresa().equals(byId1.orElse(null).getEmpresa())
                    &&!x.getId().equals(id)){
                throw new UsuarioException("Este codigo já esta sendo utilizado por outro produto");
            }
        });
    }
    public Produto atualizar(Produto produto, Integer id){
        validarCliente(produto,id);
        Optional<Produto> byId = produtoRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(produto,byId.orElse(null),"id","dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return produtoRepository.save(byId.get());
    }
}
