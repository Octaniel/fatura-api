package com.imposto.fatura.repository.cliente;

import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.filter.ClienteFilter;
import com.imposto.fatura.repository.projection.ClienteResumo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@Service
public class ClienteRepositoryImpl implements ClienteRepositoryQuery {

    @PersistenceContext
    private EntityManager Manager;

    public Page<ClienteResumo> resumo(ClienteFilter clienteFilter, Pageable pageable) {
        CriteriaBuilder builder=Manager.getCriteriaBuilder();
        CriteriaQuery<ClienteResumo> query=builder.createQuery(ClienteResumo.class);
        Root<Cliente> rootcli=query.from(Cliente.class);
        Root<Empresa> rootemp=query.from(Empresa.class);
        Root<Usuario> rootusu=query.from(Usuario.class);

        query.select(builder.construct(ClienteResumo.class
                ,rootcli.get(Cliente_.id),rootcli.get(Cliente_.nome),rootcli.get(Cliente_.nif)
                ,rootcli.get(Cliente_.dataCriacao),rootcli.get(Cliente_.usuarioCriouId)));

        Predicate[] predicates=criarPredicates(clienteFilter,builder,rootcli, rootusu, rootemp);
        query.where(predicates);

        TypedQuery<ClienteResumo> typedQuery=Manager.createQuery(query);
        int size = typedQuery.getResultList().size();
        adicionarRestricoesDePagina(typedQuery,pageable);
        return new PageImpl<>(typedQuery.getResultList(),pageable,size);
    }

    private void adicionarRestricoesDePagina(TypedQuery<ClienteResumo> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicates(ClienteFilter clienteFilter, CriteriaBuilder builder, Root<Cliente> root, Root<Usuario> rootusu, Root<Empresa> rootemp) {
        List<Predicate> predicates=new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID),rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(root.get(Cliente_.USUARIO_CRIOU_ID),rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(rootemp.get(Empresa_.ID),clienteFilter.getIdEmpresa()));

        if(!StringUtils.isEmpty(clienteFilter.getNome())){
            predicates.add(builder.like(builder.lower(root.get(Cliente_.NOME)),"%"+clienteFilter.getNome().toLowerCase()+"%"));
        }
        if(!StringUtils.isEmpty(clienteFilter.getNif())){
            predicates.add(builder.like(root.get(Cliente_.NIF),"%"+clienteFilter.getNif()+"%"));
        }
        return predicates.toArray(new Predicate[0]);
    }
}
