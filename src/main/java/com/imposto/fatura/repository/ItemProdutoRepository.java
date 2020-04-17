package com.imposto.fatura.repository;

import com.imposto.fatura.model.IdItemproduto;
import com.imposto.fatura.model.ItemProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemProdutoRepository extends JpaRepository<ItemProduto, IdItemproduto> {
    ItemProduto findByIdVendaIdAndIdProdutoId(Integer idVenda, Integer idProduto);
}
