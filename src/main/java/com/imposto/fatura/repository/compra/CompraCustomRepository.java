package com.imposto.fatura.repository.compra;

import com.imposto.fatura.model.Compra;
import com.imposto.fatura.model.IdItemProdutoCompra;
import com.imposto.fatura.model.ItemProdutoCompra;
import com.imposto.fatura.repository.ItemProdutoCompraRepository;
import com.imposto.fatura.repository.filter.CompraFilter;
import com.imposto.fatura.repository.projection.CompraResumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CompraCustomRepository {

    private final EntityManager entityManager;

    private final ItemProdutoCompraRepository itemProdutoCompraRepository;

    public CompraCustomRepository(EntityManager entityManager, ItemProdutoCompraRepository itemProdutoCompraRepository) {
        this.entityManager = entityManager;
        this.itemProdutoCompraRepository = itemProdutoCompraRepository;
    }

    public Page<CompraResumo> listar(CompraFilter compraFilter, Pageable pageable) {
        String query = "select com from Empresa emp, Compra com, Usuario usu where " +
                "usu.empresa.id = emp.id and com.usuarioCriouId = usu.id and emp.id = :idEmpresa ";
        //String condicao = "where";
        if (compraFilter.getDataInicio() != null) query += "and com.data > :dataInicio ";
        if (compraFilter.getDataFim() != null) query += "and com.data < :dataFim";
        if (compraFilter.getNomeProduto() != null) query += "and com.produto.nome like :nomeProduto";
        if (compraFilter.getIdFornecedor() != null) query += "and com.fornecedor.id = :idFornecedor";
        var q = entityManager.createQuery(query, Compra.class);
        if (compraFilter.getIdEmpresa() != null) q.setParameter("idEmpresa", compraFilter.getIdEmpresa());
        if (compraFilter.getDataInicio() != null) q.setParameter("dataInicio", compraFilter.getDataInicio());
        if (compraFilter.getDataFim() != null) q.setParameter("dataFim", compraFilter.getDataFim());
        if (compraFilter.getNomeProduto() != null)
            q.setParameter("nomeProduto", "%" + compraFilter.getNomeProduto() + "%");
        if (compraFilter.getIdFornecedor() != null) q.setParameter("idFornecedor", compraFilter.getIdFornecedor());
        int size = q.getResultList().size();
        adicionarRestricoesDePagina(q, pageable);

        return new PageImpl<>(Objects.requireNonNull(resumir(q.getResultList())), pageable, size);
    }

    private List<CompraResumo> resumir(List<Compra> resultList) {
        List<CompraResumo> compraResumos = new ArrayList<>();
        resultList.forEach(x -> {
            List<CompraResumo.ProdutoMin> produtoMins = new ArrayList<>();
            x.getProdutos().forEach(c -> {
                CompraResumo.ProdutoMin produtoMin = new CompraResumo.ProdutoMin();
                produtoMin.setNomeProduto(c.getNome());
                IdItemProdutoCompra idItemProdutoCompra = new IdItemProdutoCompra();
                idItemProdutoCompra.setCompra(x);
                idItemProdutoCompra.setProduto(c);
                ItemProdutoCompra one = itemProdutoCompraRepository.getOne(idItemProdutoCompra);
                produtoMin.setQuatidadeProduto(one.getQuantidade());
                produtoMins.add(produtoMin);
            });
            CompraResumo compraResumo = new CompraResumo();
            compraResumo.setId(x.getId());
            compraResumo.setData(x.getData());
            compraResumo.setNomeFornecedor(x.getFornecedor().getNome());
            compraResumo.setProdutoMins(produtoMins);
            compraResumo.setValor(x.getValor());
            compraResumo.setDataCriacao(x.getDataCriacao());
            compraResumos.add(compraResumo);
        });
        return compraResumos;
    }

    private void adicionarRestricoesDePagina(TypedQuery<Compra> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }
}
