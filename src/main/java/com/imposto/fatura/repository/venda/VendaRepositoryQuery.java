package com.imposto.fatura.repository.venda;

import com.imposto.fatura.model.Venda;
import com.imposto.fatura.repository.filter.DocumentoFilter;
import com.imposto.fatura.repository.filter.VendaFilter;
import com.imposto.fatura.repository.projection.VendaResumo;
import com.imposto.fatura.repository.projection.VendaResumoPro;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendaRepositoryQuery {
    Page<Venda> filtrar(String nomeCliente, Pageable pageable);
    List<Venda> paraDoc(DocumentoFilter documentoFilter);
    Page<VendaResumo> resumo(VendaFilter vendaFilter, Pageable pageable);
    VendaResumoPro resumoPro(Integer id);
}
