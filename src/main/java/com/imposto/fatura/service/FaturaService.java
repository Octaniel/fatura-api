package com.imposto.fatura.service;

import com.imposto.fatura.model.ItemProduto;
import com.imposto.fatura.model.Venda;
import com.imposto.fatura.repository.ItemProdutoRepository;
import com.imposto.fatura.repository.VendaRepository;
import com.imposto.fatura.repository.filter.DocumentoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FaturaService {

    private VendaRepository vendaRepository;
    private ItemProdutoRepository itemProdutoRepository;

    public FaturaService(VendaRepository vendaRepository, ItemProdutoRepository itemProdutoRepository) {
        this.vendaRepository = vendaRepository;
        this.itemProdutoRepository = itemProdutoRepository;
    }

    public byte[] paraDoc(DocumentoFilter documentoFilter) {
        List<Venda> faturas = vendaRepository.paraDoc(documentoFilter);
        JSONArray jsonArray = new JSONArray();
        final FileWriter[] fileWriter = new FileWriter[5];
        faturas.forEach(x -> {
            JSONObject jsonObject = new JSONObject();
            JSONArray tbItensDocumentoGerados = new JSONArray();

            x.getProdutos().forEach(y -> {
                JSONObject tbItensDocumentoGeradosob = new JSONObject();
                ItemProduto byVendaIdAAndProdutoId = itemProdutoRepository.findByIdVendaIdAndIdProdutoId(x.getId(), y.getId());
                try {
                    tbItensDocumentoGeradosob.put("codigoIsencao", y.getMotivoIsencao() == null ? "" : y.getMotivoIsencao().getCodigo());
                    tbItensDocumentoGeradosob.put("quantItens", byVendaIdAAndProdutoId.getQuantidade());
                    tbItensDocumentoGeradosob.put("descItens", y.getDescricao());
                    tbItensDocumentoGeradosob.put("valorItens", byVendaIdAAndProdutoId.getValorTotal());
                    tbItensDocumentoGeradosob.put("valorTaxaAplicavel", y.getTaxa().getValor());
                    tbItensDocumentoGerados.put(tbItensDocumentoGeradosob);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            try {
                jsonObject.put("numDocumento", x.getNumero());
                jsonObject.put("dtEmissaoDocumento", x.getData());
                jsonObject.put("numSerieDocumento", x.getSerie().getNumero());
                jsonObject.put("tbItensDocumentoGerados", tbItensDocumentoGerados);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        try {
            File file = ResourceUtils.getFile("classpath:docs/saida.json");
            fileWriter[0] = new FileWriter(file);
            fileWriter[0].write(jsonArray.toString());
            fileWriter[0].close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bFile = null;
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:docs/saida.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert file != null;
            bFile = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bFile;
    }

}

