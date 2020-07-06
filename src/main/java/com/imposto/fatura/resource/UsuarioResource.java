package com.imposto.fatura.resource;

import com.imposto.fatura.model.Usuario;
import com.imposto.fatura.repository.UsuarioRepository;
import com.imposto.fatura.repository.filter.UsuarioFilter;
import com.imposto.fatura.service.UsuarioService;
import com.imposto.fatura.service.exeption.UsuarioException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioService usuarioService;

    public UsuarioResource(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    /*@GetMapping
    public List<Usuario> listar(){
        return usuarioRepository.findAll();
    }*/

    @PostMapping
    public ResponseEntity<Usuario> salvar(@RequestBody @Valid Usuario usuario, HttpServletResponse httpServletResponse) {
        return usuarioService.salvar(usuario, httpServletResponse);
    }

    @GetMapping
    public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {
        return usuarioRepository.filtrar(usuarioFilter, pageable);
    }

    @GetMapping("/{id}")
    public Usuario listarPorId(@PathVariable Integer id) {
        Optional<Usuario> byId = usuarioRepository.findById(id);
        if (byId.isEmpty()) throw new UsuarioException("Não existe nenhum usuario com este id");
        return byId.get();
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return usuarioService.atualizar(id, usuario);
    }

    @PutMapping("senha/{id}")
    public Usuario atualizarSenha(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return usuarioService.atualizarSenha(id, usuario);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("/{id}")
    public void apagar(@PathVariable Integer id) {
        usuarioRepository.deleteById(id);
    }
}
