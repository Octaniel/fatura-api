package com.imposto.fatura.repository;

import com.imposto.fatura.model.IdItemProdutoCompra;
import com.imposto.fatura.model.ItemProduto;
import com.imposto.fatura.model.ItemProdutoCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemProdutoCompraRepository extends JpaRepository<ItemProdutoCompra, IdItemProdutoCompra> {
}
