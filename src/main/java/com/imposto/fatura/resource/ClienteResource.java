package com.imposto.fatura.resource;

import com.imposto.fatura.model.Cliente;
import com.imposto.fatura.model.Modelo;
import com.imposto.fatura.repository.ClienteRepository;
import com.imposto.fatura.repository.filter.ClienteFilter;
import com.imposto.fatura.repository.projection.ClienteResumo;
import com.imposto.fatura.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteResource {

    private final ClienteRepository clienteRepository;

    private final ClienteService clienteService;

    public ClienteResource(ClienteRepository clienteRepository, ClienteService clienteService) {
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
    }

    @GetMapping("listar")
    public List<Cliente> listar(){
       return clienteRepository.findAll();
    }

    @GetMapping
    public Page<ClienteResumo> listarTabela(ClienteFilter clienteFilter, Pageable pageable){
        return clienteRepository.resumo(clienteFilter,pageable);
    }
    @GetMapping("/{id}")
    public Cliente atualizar(@PathVariable Integer id){
        return clienteRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Cliente> salvar(@Valid @RequestBody Cliente cliente, HttpServletResponse httpServletResponse){
        return clienteService.salvar(cliente,httpServletResponse);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Integer id,@Valid @RequestBody Cliente cliente){
        return clienteService.atualizar(id,cliente);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Integer id){
        clienteRepository.deleteById(id);
    }

}
