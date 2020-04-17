package com.imposto.fatura.repository.empresa;

import com.imposto.fatura.model.Empresa;
import com.imposto.fatura.model.Empresa_;
import com.imposto.fatura.repository.filter.EmpresaFilter;
import com.imposto.fatura.repository.projection.EmpresaResumo;
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
public class EmpresaRepositoryimpl implements EmpresaRepositoryQuery{

    @PersistenceContext
    private EntityManager Manager;

    //@Override
    public Page<EmpresaResumo> listarTabela(EmpresaFilter empresaFilter, Pageable pageable) {
        CriteriaBuilder Builder=Manager.getCriteriaBuilder();
        CriteriaQuery<EmpresaResumo> criteria=Builder.createQuery(EmpresaResumo.class);
        Root<Empresa> root=criteria.from(Empresa.class);

        criteria.select(Builder.construct(EmpresaResumo.class, root.get(Empresa_.id),root.get(Empresa_.nome),
                root.get(Empresa_.numeroCertificacao),root.get(Empresa_.nif),root.get(Empresa_.email),
                root.get(Empresa_.endereco),root.get(Empresa_.telefone),root.get(Empresa_.dataCertificacao),
                root.get(Empresa_.dataCriacao)));

        Predicate[] predicates=criarPredicates(empresaFilter,Builder,root);
        criteria.where(predicates);

        TypedQuery<EmpresaResumo> query=Manager.createQuery(criteria);
        adicionarRestricoesDePagina(query,pageable);
        return new PageImpl<>(query.getResultList(),pageable,total(empresaFilter));
    }


    private Long total(EmpresaFilter empresaFilter) {
        CriteriaBuilder Builder=Manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria=Builder.createQuery(Long.class);
        Root<Empresa> root=criteria.from(Empresa.class);

        Predicate[] predicates=criarPredicates(empresaFilter,Builder,root);
        criteria.where(predicates);
        criteria.select(Builder.count(root));
        return Manager.createQuery(criteria).getSingleResult();
    }

    private void adicionarRestricoesDePagina(TypedQuery<?> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicates(EmpresaFilter empresaFilter, CriteriaBuilder builder, Root<Empresa> root) {
        List<Predicate> predicates=new ArrayList<>();
        if(!StringUtils.isEmpty(empresaFilter.getNome())){
            predicates.add(builder.like(builder.lower(root.get(Empresa_.NOME)),"%"+empresaFilter.getNome().toLowerCase()+"%"));
        }
        if(!StringUtils.isEmpty(empresaFilter.getNif())){
            predicates.add(builder.like(root.get(Empresa_.NIF),"%"+empresaFilter.getNif()+"%"));
        }
        if(!StringUtils.isEmpty(empresaFilter.getTelefone())){
            predicates.add(builder.like(builder.lower(root.get(Empresa_.TELEFONE)),"%"+empresaFilter.getTelefone().toLowerCase()+"%"));
        }
        return predicates.toArray(new Predicate[0]);
    }
}
