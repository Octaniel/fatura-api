package com.imposto.fatura.repository;

import com.imposto.fatura.model.Fornecedor;
import com.imposto.fatura.repository.fornecedor.FornecedorRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer>, FornecedorRepositoryQuery {
}
