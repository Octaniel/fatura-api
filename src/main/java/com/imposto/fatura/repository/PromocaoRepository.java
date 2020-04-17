package com.imposto.fatura.repository;

import com.imposto.fatura.model.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromocaoRepository extends JpaRepository<Promocao, Integer> {
}
