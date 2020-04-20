package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Empresa;
import com.imposto.fatura.model.Usuario;
import com.imposto.fatura.repository.UsuarioRepository;
import com.imposto.fatura.service.exeption.UsuarioException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {

    private final ApplicationEventPublisher publisher;

    private final JavaMailSender mailSender;

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(ApplicationEventPublisher publisher, JavaMailSender mailSender, UsuarioRepository usuarioRepository) {
        this.publisher = publisher;
        this.mailSender = mailSender;
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<Usuario> salvar(Usuario usuario, HttpServletResponse httpServletResponse){
        validar(usuario,0);
        LocalDateTime localDateTime = LocalDateTime.now();
        usuario.setDataAlteracao(localDateTime);
        Usuario one = usuarioRepository.getOne(usuario.getUsuarioCriouId());
        Empresa empresa = new Empresa();
        empresa.setId(one.getEmpresa().getId());
        usuario.setEmpresa(empresa);
        usuario.setDataCriacao(localDateTime);

        String senhaTemporaria= criarSenha();
        PasswordEncoder encoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();
        usuario.setSenha(encoder.encode(senhaTemporaria));
        usuario.setSenhaTemporaria(senhaTemporaria);
        usuario.setIsFirst(true);
        Usuario save = usuarioRepository.save(usuario);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        //String s = enviarEmail(usuario.getEmail(), senhaTemporaria);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }
        private void validar(Usuario usuario, Integer id){
            if(!usuario.getSenha().equals(usuario.getConfirmacaoSenha())) throw new UsuarioException("A Senha e a confirmação de senha são diferentes");
            List<Usuario> all = usuarioRepository.findAll();
            all.forEach(x->{
                if(usuario.getNome().equals(x.getNome())&&!x.getId().equals(id)) throw new UsuarioException("Este nome já esta sendo utilizado por outro utilizador");
                if(usuario.getEmail().equals(x.getEmail())&&!x.getId().equals(id)) throw new UsuarioException("Este email já esta sendo utilizado por outro utilizador");
            });
        }

    private String enviarEmail(String email, String senhaTemporaria){
        try {
            MimeMessage mail = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper( mail );
            helper.setTo( email );
            helper.setSubject( "Senha temporaria de sistema de faturação" );
            helper.setText("<p>Senha Temporaria:"+senhaTemporaria+"</p>", true);
            mailSender.send(mail);

            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar e-mail";
        }
    }

    private String criarSenha(){
        RandomString random = new RandomString();
        Random random1 = new Random();
        String s = random.nextString();
        String senha = (random1.nextInt(9)+1)+""+s.charAt(1)+""+(random1.nextInt(9)+1)+""+s.charAt(3);
        return senha;
    }

    public Usuario atualizar(Integer id, Usuario usuario){
        validar(usuario,id);
        Optional<Usuario> byId = usuarioRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(usuario,byId.orElse(null),"id","dataCriacao","senha","senhaTemporaria","usuarioCriouId","empresa");
        byId.get().setDataAlteracao(LocalDateTime.now());
        //PasswordEncoder encoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //byId.get().setSenha(encoder.encode(byId.get().getSenha()));
        return usuarioRepository.save(byId.get());
    }

    public Usuario atualizarSenha(Integer id, Usuario usuario){
        if(!usuario.getSenha().equals(usuario.getConfirmacaoSenha())){
            throw new UsuarioException("Senha e confirmação de senha não são iguais");
        }
        Optional<Usuario> byId = usuarioRepository.findById(id);
        assert byId.orElse(null) != null;
        byId.orElse(null).setDataAlteracao(LocalDateTime.now());
        PasswordEncoder encoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();
        byId.get().setSenha(encoder.encode(usuario.getSenha()));
        byId.get().setIsFirst(false);
        return usuarioRepository.save(byId.get());
    }
}
