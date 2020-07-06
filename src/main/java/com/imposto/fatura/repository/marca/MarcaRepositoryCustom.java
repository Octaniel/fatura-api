package com.imposto.fatura.repository.marca;

import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.filter.MarcaFilter;
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
public class MarcaRepositoryCustom {

    @PersistenceContext
    private EntityManager Manager;

    public Page<Marca> filtrar(MarcaFilter marcaFilter, Pageable pageable) {
        TypedQuery<Marca> typedQuery = getBuilder(marcaFilter);
        int size = typedQuery.getResultList().size();
        adicionarRestricoesDePaginaResumo(typedQuery, pageable);
        return new PageImpl<>(typedQuery.getResultList(), pageable, size);
    }


    public List<Marca> listar(MarcaFilter marcaFilter) {
        return getBuilder(marcaFilter).getResultList();
    }


    private TypedQuery<Marca> getBuilder(MarcaFilter marcaFilter) {
        CriteriaBuilder builder = Manager.getCriteriaBuilder();
        CriteriaQuery<Marca> query = builder.createQuery(Marca.class);
        Root<Marca> rootmar = query.from(Marca.class);
        Root<Empresa> rootemp = query.from(Empresa.class);
        Root<Usuario> rootusu = query.from(Usuario.class);

        query.select(rootmar);

        Predicate[] predicates = criarPredicatesResumo(marcaFilter, builder, rootmar, rootemp, rootusu);
        query.where(predicates);

        return Manager.createQuery(query);
    }

    private void adicionarRestricoesDePaginaResumo(TypedQuery<Marca> typedQuery, Pageable pageable) {
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicatesResumo(MarcaFilter marcaFilter, CriteriaBuilder builder, Root<Marca> rootmar, Root<Empresa> rootemp, Root<Usuario> rootusu) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID), rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(rootmar.get(Modelo_.USUARIO_CRIOU_ID), rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(rootemp.get(Empresa_.ID), marcaFilter.getIdEmpresa()));

        if (!StringUtils.isEmpty(marcaFilter.getNome())) {
            predicates.add(builder.like(builder.lower(rootmar.get(Marca_.NOME)), "%" + marcaFilter.getNome().toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[0]);
    }
}
