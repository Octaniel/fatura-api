package com.imposto.fatura.repository.produto;

import com.imposto.fatura.model.Produto;
import com.imposto.fatura.repository.filter.ProdutoFilter;
import com.imposto.fatura.repository.projection.ProdutoResumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoRepositoryQuery {
    Page<?> resumo(ProdutoFilter produtoFilter, Pageable pageable);
}
