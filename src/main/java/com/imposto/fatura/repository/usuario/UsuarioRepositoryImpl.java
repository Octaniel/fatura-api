package com.imposto.fatura.repository.usuario;

import com.imposto.fatura.model.Usuario;
import com.imposto.fatura.model.Usuario_;
import com.imposto.fatura.repository.filter.UsuarioFilter;
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
public class UsuarioRepositoryImpl implements UsuarioRepositoryQuery {

    @PersistenceContext
    private EntityManager Manager;

    @Override
    public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {
        CriteriaBuilder Builder=Manager.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria=Builder.createQuery(Usuario.class);
        Root<Usuario> root=criteria.from(Usuario.class);

        Predicate[] predicates=criarPredicates(usuarioFilter,Builder,root);
        criteria.where(predicates);

        TypedQuery<Usuario> query=Manager.createQuery(criteria);
        adicionarRestricoesDePagina(query,pageable);
        return new PageImpl<>(query.getResultList(),pageable,total(usuarioFilter));
    }

    private Long total(UsuarioFilter usuarioFilter) {
        CriteriaBuilder Builder=Manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria=Builder.createQuery(Long.class);
        Root<Usuario> root=criteria.from(Usuario.class);

        Predicate[] predicates=criarPredicates(usuarioFilter,Builder,root);
        criteria.where(predicates);
        criteria.select(Builder.count(root));
        return Manager.createQuery(criteria).getSingleResult();
    }

    private void adicionarRestricoesDePagina(TypedQuery<Usuario> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicates(UsuarioFilter usuarioFilter, CriteriaBuilder builder, Root<Usuario> root) {
        List<Predicate> predicates=new ArrayList<>();
        if(!StringUtils.isEmpty(usuarioFilter.getNome())){
            predicates.add(builder.like(builder.lower(root.get(Usuario_.NOME)),"%"+usuarioFilter.getNome().toLowerCase()+"%"));
        }
        if(!StringUtils.isEmpty(usuarioFilter.getEmail())){
            predicates.add(builder.like(builder.lower(root.get(Usuario_.EMAIL)),"%"+usuarioFilter.getEmail().toLowerCase()+"%"));
        }
        return predicates.toArray(new Predicate[0]);
    }
}
