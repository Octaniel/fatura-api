package com.imposto.fatura.repository.tipoProduto;

import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.filter.MarcaFilter;
import com.imposto.fatura.repository.filter.TipoProdutoFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TipoProdutoRepositoryCustom {


    @PersistenceContext
    private EntityManager Manager;

    public Page<TipoProduto> filtrar(TipoProdutoFilter tipoProdutoFilter, Pageable pageable) {
        TypedQuery<TipoProduto> typedQuery= getBuilder(tipoProdutoFilter);
        int size = typedQuery.getResultList().size();
        adicionarRestricoesDePaginaResumo(typedQuery,pageable);
        return new PageImpl<>(typedQuery.getResultList(),pageable,size);
    }


    public List<TipoProduto> listar(TipoProdutoFilter tipoProdutoFilter) {
        return getBuilder(tipoProdutoFilter).getResultList();
    }


    private TypedQuery<TipoProduto> getBuilder(TipoProdutoFilter tipoProdutoFilter) {
        CriteriaBuilder builder=Manager.getCriteriaBuilder();
        CriteriaQuery<TipoProduto> query=builder.createQuery(TipoProduto.class);
        Root<TipoProduto> roottpr=query.from(TipoProduto.class);
        Root<Empresa> rootemp=query.from(Empresa.class);
        Root<Usuario> rootusu=query.from(Usuario.class);

        query.select(roottpr);

        Predicate[] predicates=criarPredicatesResumo(tipoProdutoFilter,builder,roottpr,rootemp,rootusu);
        query.where(predicates);

        return Manager.createQuery(query);
    }

    private void adicionarRestricoesDePaginaResumo(TypedQuery<TipoProduto> typedQuery, Pageable pageable) {
        typedQuery.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicatesResumo(TipoProdutoFilter tipoProdutoFilter, CriteriaBuilder builder, Root<TipoProduto> roottpr, Root<Empresa> rootemp, Root<Usuario> rootusu) {
        System.out.println(tipoProdutoFilter.getNatureza());
        TipoOferta tipoOferta = TipoOferta.valueOf(tipoProdutoFilter.getNatureza());
        List<Predicate> predicates=new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID),rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(roottpr.get(TipoProduto_.USUARIO_CRIOU_ID),rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(roottpr.get(TipoProduto_.NATUREZA),tipoOferta));
        predicates.add(builder.equal(rootemp.get(Empresa_.ID),tipoProdutoFilter.getIdEmpresa()));


        if(!StringUtils.isEmpty(tipoProdutoFilter.getDescricao())){
            predicates.add(builder.like(builder.lower(roottpr.get(TipoProduto_.descricao)),"%"+tipoProdutoFilter.getDescricao().toLowerCase()+"%"));
        }

        return predicates.toArray(new Predicate[0]);
    }
}
