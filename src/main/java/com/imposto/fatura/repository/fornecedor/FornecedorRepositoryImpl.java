package com.imposto.fatura.repository.fornecedor;

import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.filter.FornecedorFilter;
import com.imposto.fatura.repository.projection.FornecedorResumo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class FornecedorRepositoryImpl implements FornecedorRepositoryQuery {

    @PersistenceContext
    private EntityManager Manager;

    @Override
    public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {
        CriteriaBuilder Builder = Manager.getCriteriaBuilder();
        CriteriaQuery<Fornecedor> criteria = Builder.createQuery(Fornecedor.class);
        Root<Fornecedor> root = criteria.from(Fornecedor.class);

        Predicate[] predicates = criarPredicates(fornecedorFilter, Builder, root);
        criteria.where(predicates);

        TypedQuery<Fornecedor> query = Manager.createQuery(criteria);
        adicionarRestricoesDePagina(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(fornecedorFilter));
    }

    @Override
    public Page<FornecedorResumo> resumo(FornecedorFilter fornecedorFilter, Pageable pageable) {
        TypedQuery<FornecedorResumo> typedQuery = getFornecedorResumoTypedQuery(fornecedorFilter);
        int size = typedQuery.getResultList().size();
        adicionarRestricoesDePaginaResumo(typedQuery, pageable);
        return new PageImpl<>(typedQuery.getResultList(), pageable, size);
    }

    private TypedQuery<FornecedorResumo> getFornecedorResumoTypedQuery(FornecedorFilter fornecedorFilter) {
        CriteriaBuilder builder = Manager.getCriteriaBuilder();
        CriteriaQuery<FornecedorResumo> query = builder.createQuery(FornecedorResumo.class);
        Root<Fornecedor> rootfor = query.from(Fornecedor.class);
        Root<Empresa> rootemp = query.from(Empresa.class);
        Root<Usuario> rootusu = query.from(Usuario.class);

        query.select(builder.construct(FornecedorResumo.class
                , rootfor.get(Fornecedor_.id), rootfor.get(Fornecedor_.nome), rootfor.get(Fornecedor_.nif),
                rootfor.get(Fornecedor_.endereco), rootfor.get(Fornecedor_.email), rootfor.get(Fornecedor_.telefone)));

        Predicate[] predicates = criarPredicatesResumo(fornecedorFilter, builder, rootfor, rootusu, rootemp);
        query.where(predicates);

        return Manager.createQuery(query);
    }

    @Override
    public List<FornecedorResumo> resumoListar(FornecedorFilter fornecedorFilter) {
        return getFornecedorResumoTypedQuery(fornecedorFilter).getResultList();
    }

    private Predicate[] criarPredicatesResumo(FornecedorFilter fornecedorFilter, CriteriaBuilder builder, Root<Fornecedor> rootfor, Root<Usuario> rootusu, Root<Empresa> rootemp) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID), rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(rootfor.get(Fornecedor_.USUARIO_CRIOU_ID), rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(rootemp.get(Empresa_.ID), fornecedorFilter.getIdEmpresa()));

        return getPredicates(fornecedorFilter, builder, rootfor, predicates);
    }

    private Predicate[] getPredicates(FornecedorFilter fornecedorFilter, CriteriaBuilder builder, Root<Fornecedor> rootfor, List<Predicate> predicates) {
        if (!StringUtils.isEmpty(fornecedorFilter.getNome())) {
            predicates.add(builder.like(builder.lower(rootfor.get(Fornecedor_.NOME)), "%" + fornecedorFilter.getNome().toLowerCase() + "%"));
        }
        if (fornecedorFilter.getNif() != null) {
            predicates.add(builder.like(rootfor.get(Fornecedor_.NIF), "%" + fornecedorFilter.getNif() + "%"));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginaResumo(TypedQuery<FornecedorResumo> typedQuery, Pageable pageable) {
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
    }

    private Long total(FornecedorFilter fornecedorFilter) {
        CriteriaBuilder Builder = Manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = Builder.createQuery(Long.class);
        Root<Fornecedor> root = criteria.from(Fornecedor.class);

        Predicate[] predicates = criarPredicates(fornecedorFilter, Builder, root);
        criteria.where(predicates);
        criteria.select(Builder.count(root));
        return Manager.createQuery(criteria).getSingleResult();
    }

    private void adicionarRestricoesDePagina(TypedQuery<Fornecedor> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicates(FornecedorFilter fornecedorFilter, CriteriaBuilder builder, Root<Fornecedor> root) {
        List<Predicate> predicates = new ArrayList<>();
        return getPredicates(fornecedorFilter, builder, root, predicates);
    }
}
