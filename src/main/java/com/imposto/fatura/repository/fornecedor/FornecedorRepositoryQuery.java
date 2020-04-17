package com.imposto.fatura.repository.fornecedor;

import com.imposto.fatura.model.Fornecedor;
import com.imposto.fatura.repository.filter.FornecedorFilter;
import com.imposto.fatura.repository.projection.FornecedorResumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FornecedorRepositoryQuery {
    Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable);
    Page<FornecedorResumo> resumo(FornecedorFilter fornecedorFilter, Pageable pageable);

    List<FornecedorResumo> resumoListar(FornecedorFilter fornecedorFilter);
}
