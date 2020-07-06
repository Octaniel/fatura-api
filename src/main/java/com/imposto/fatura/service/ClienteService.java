package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Cliente;
import com.imposto.fatura.model.Usuario;
import com.imposto.fatura.repository.ClienteRepository;
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
public class ClienteService {

    private final ApplicationEventPublisher publisher;

    private final ClienteRepository clienteRepository;

    private final UsuarioRepository usuarioRepository;

    public ClienteService(ApplicationEventPublisher publisher, ClienteRepository clienteRepository, UsuarioRepository usuarioRepository) {
        this.publisher = publisher;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<Cliente> salvar(Cliente cliente, HttpServletResponse httpServletResponse) {
        validarCliente(cliente, 0);
        LocalDateTime localDateTime = LocalDateTime.now();
        cliente.setDataAlteracao(localDateTime);
        cliente.setDataCriacao(localDateTime);
        Cliente save = clienteRepository.save(cliente);
        publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    private void validarCliente(Cliente cliente, Integer id) {
        List<Cliente> lista = clienteRepository.findAll();
        lista.forEach(x -> {
            Optional<Usuario> byId = usuarioRepository.findById(cliente.getUsuarioCriouId());
            Optional<Usuario> byId1 = usuarioRepository.findById(x.getUsuarioCriouId());
            assert byId.orElse(null) != null;
            assert byId1.orElse(null) != null;
            if (x.getNif().equals(cliente.getNif())
                    && byId.orElse(null).getEmpresa().equals(byId1.orElse(null).getEmpresa())
                    && !x.getId().equals(id)) {
                throw new UsuarioException("Este nif já esta sendo utilizado por outro cliente");
            }
        });
    }

    public Cliente atualizar(Integer id, Cliente cliente) {
        Optional<Cliente> byId = clienteRepository.findById(id);
        validarCliente(cliente, id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(cliente, byId.orElse(null), "id", "dataCriacao", "usuarioCriouId");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return clienteRepository.save(byId.get());
    }
}
