package com.imposto.fatura.repository.modelo;

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
public class ModeloRepositoryCustom {

    @PersistenceContext
    private EntityManager Manager;

    public Page<Modelo> filtrar(Integer idEmpresa, Pageable pageable) {
        TypedQuery<Modelo> typedQuery= getBuilder(idEmpresa, null);
        int size = typedQuery.getResultList().size();
        adicionarRestricoesDePaginaResumo(typedQuery,pageable);
        return new PageImpl<>(typedQuery.getResultList(),pageable,size);
    }

    public List<Modelo> listarPorMarca(Integer idEmpresa, Integer idMarca) {
        TypedQuery<Modelo> typedQuery= getBuilder(idEmpresa, idMarca);
        return typedQuery.getResultList();
    }


    public List<Modelo> listar(Integer idEmpresa) {
        return getBuilder(idEmpresa, null).getResultList();
    }


    private TypedQuery<Modelo> getBuilder(Integer idEmpresa, Integer idMarca) {
        CriteriaBuilder builder=Manager.getCriteriaBuilder();
        CriteriaQuery<Modelo> query=builder.createQuery(Modelo.class);
        Root<Modelo> rootmod=query.from(Modelo.class);
        Root<Empresa> rootemp=query.from(Empresa.class);
        Root<Usuario> rootusu=query.from(Usuario.class);
            query.select(rootmod);
        Predicate[] predicates=criarPredicatesResumo(idEmpresa,builder,rootmod,rootemp,rootusu, idMarca);
        query.where(predicates);

        return Manager.createQuery(query);
    }

    private void adicionarRestricoesDePaginaResumo(TypedQuery<Modelo> typedQuery, Pageable pageable) {
        typedQuery.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicatesResumo(Integer idEmpresa, CriteriaBuilder builder, Root<Modelo> rootmod, Root<Empresa> rootemp, Root<Usuario> rootusu, Integer idMarca) {
        List<Predicate> predicates=new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID),rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(rootmod.get(Modelo_.USUARIO_CRIOU_ID),rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(rootemp.get(Empresa_.ID),idEmpresa));

        if (idMarca!=null){
            predicates.add(builder.equal(rootmod.get(Modelo_.MARCA).get(Marca_.ID),idMarca));
        }

        return predicates.toArray(new Predicate[0]);
    }

}
