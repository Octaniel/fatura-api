package com.imposto.fatura.repository.serie;

import com.imposto.fatura.model.*;
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
public class SerieRepositoryCustom {

    @PersistenceContext
    private EntityManager Manager;

    public List<Serie> resumo(Integer idEmpresa) {
       return getBuilder(idEmpresa).getResultList();
    }

    public Page<Serie> filtrar(Integer idEmpresa, Pageable pageable) {
        TypedQuery<Serie> typedQuery = getBuilder(idEmpresa);
        int size = typedQuery.getResultList().size();
        adicionarRestricoesDePagina(typedQuery,pageable);
        return new PageImpl<>(typedQuery.getResultList(),pageable,size);
    }

    private TypedQuery<Serie> getBuilder(Integer idEmpresa) {
        CriteriaBuilder builder=Manager.getCriteriaBuilder();
        CriteriaQuery<Serie> query=builder.createQuery(Serie.class);
        Root<Serie> rootser=query.from(Serie.class);
        Root<Empresa> rootemp=query.from(Empresa.class);
        Root<Usuario> rootusu=query.from(Usuario.class);

        query.select(rootser);

        Predicate[] predicates=criarPredicates(idEmpresa,builder,rootser, rootusu, rootemp);
        query.where(predicates);

        return Manager.createQuery(query);
    }

    private void adicionarRestricoesDePagina(TypedQuery<Serie> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicates(Integer idEmpresa, CriteriaBuilder builder, Root<Serie> root, Root<Usuario> rootusu, Root<Empresa> rootemp) {
        List<Predicate> predicates=new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID),rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(root.get(Cliente_.USUARIO_CRIOU_ID),rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(rootemp.get(Empresa_.ID),idEmpresa));
        return predicates.toArray(new Predicate[0]);
    }
}
