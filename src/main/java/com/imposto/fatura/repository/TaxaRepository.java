package com.imposto.fatura.repository;

import com.imposto.fatura.model.Taxa;
import com.imposto.fatura.model.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxaRepository extends JpaRepository<Taxa, Integer> {
}
