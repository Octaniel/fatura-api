package com.imposto.fatura.repository;

import com.imposto.fatura.model.MotivoIsencao;
import com.imposto.fatura.model.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivoIsencaoRepository extends JpaRepository<MotivoIsencao, Integer> {
}
