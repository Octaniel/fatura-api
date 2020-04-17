package com.imposto.fatura.repository;

import com.imposto.fatura.model.Venda;
import com.imposto.fatura.repository.venda.VendaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Integer>, VendaRepositoryQuery {
    List<Venda> findAllBySerieId(Integer idSerie);
}
