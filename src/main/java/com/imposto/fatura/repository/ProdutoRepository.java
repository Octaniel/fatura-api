package com.imposto.fatura.repository;

import com.imposto.fatura.model.Produto;
import com.imposto.fatura.repository.produto.ProdutoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>, ProdutoRepositoryQuery {
    List<Produto> findAllByStatusEqualsAndStockGreaterThan(Boolean status, Integer quantidade);
}
