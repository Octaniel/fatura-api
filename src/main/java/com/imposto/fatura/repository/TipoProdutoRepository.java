package com.imposto.fatura.repository;

import com.imposto.fatura.model.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoProdutoRepository extends JpaRepository<TipoProduto, Integer> {
}
