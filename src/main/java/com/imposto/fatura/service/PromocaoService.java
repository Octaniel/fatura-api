package com.imposto.fatura.service;

import com.imposto.fatura.event.RecursoCriadoEvent;
import com.imposto.fatura.model.Produto;
import com.imposto.fatura.model.Promocao;
import com.imposto.fatura.repository.ProdutoRepository;
import com.imposto.fatura.repository.PromocaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PromocaoService {

    private final ApplicationEventPublisher publisher;

    private final PromocaoRepository promocaoRepository;

    private final ProdutoRepository produtoRepository;

    public PromocaoService(ApplicationEventPublisher publisher, PromocaoRepository promocaoRepository, ProdutoRepository produtoRepository) {
        this.publisher = publisher;
        this.promocaoRepository = promocaoRepository;
        this.produtoRepository = produtoRepository;
    }

    public ResponseEntity<Promocao> salvar(Promocao promocao, Integer idProduto, HttpServletResponse httpServletResponse){
        LocalDateTime localDateTime = LocalDateTime.now();
        promocao.setDataAlteracao(localDateTime);
        promocao.setDataCriacao(localDateTime);
        Promocao save = promocaoRepository.save(promocao);
        Produto one = produtoRepository.getOne(idProduto);
        one.setEmPromocao(true);
        one.setPromocao(save);
        produtoRepository.save(one);
        publisher.publishEvent(new RecursoCriadoEvent(this,httpServletResponse,save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void tirarPromocao(){
        List<Produto> all = produtoRepository.findAll();
        all.forEach(x -> {
            if(x.getEmPromocao()){
                if (x.getPromocao().getDataFim().equals(LocalDate.now())){
                    x.setEmPromocao(false);
                    x.setPromocao(null);
                    produtoRepository.save(x);
                }
            }
        });
    }

    public Promocao atualizar(Promocao promocao, Integer id){
        Optional<Promocao> byId = promocaoRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(promocao,byId.orElse(null),"id","dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return promocaoRepository.save(byId.get());
    }

    //@Scheduled(cron = "0 38 9 * * *")
    public byte[] test() {
        JSONObject jsonObject = new JSONObject();
        FileWriter fileWriter;
        try {
            jsonObject.put("numDocumento", 1);
            jsonObject.put("dtEmissaoDocumento", 2019-5-22);
            jsonObject.put("numSerieDocumento", "FT0000419");
            jsonObject.put("quantItens", 5);
            fileWriter = new FileWriter("C:\\Users\\ChrisMovel\\Documents\\exemplar\\saida.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
        byte[] bFile = null;
        String filePath = "C:\\Users\\ChrisMovel\\Documents\\exemplar\\saida.json";
        try {
            bFile = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bFile;
    }
}
