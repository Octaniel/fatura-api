package com.imposto.fatura.repository.venda;

import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.filter.DocumentoFilter;
import com.imposto.fatura.repository.filter.VendaFilter;
import com.imposto.fatura.repository.projection.VendaResumo;
import com.imposto.fatura.repository.projection.VendaResumoPro;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class VendaRepositoryImpl implements VendaRepositoryQuery {

    @PersistenceContext
    private EntityManager Manager;


    @Override
    public Page<Venda> filtrar(String nomeCliente, Pageable pageable) {
        CriteriaBuilder Builder = Manager.getCriteriaBuilder();
        CriteriaQuery<Venda> criteria = Builder.createQuery(Venda.class);
        Root<Venda> root = criteria.from(Venda.class);

        Predicate[] predicates = criarPredicates(nomeCliente, Builder, root);
        criteria.where(predicates);

        TypedQuery<Venda> query = Manager.createQuery(criteria);
        adicionarRestricoesDePagina(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(nomeCliente));
    }

    @Override
    public List<Venda> paraDoc(DocumentoFilter documentoFilter) {
        CriteriaBuilder Builder = Manager.getCriteriaBuilder();
        CriteriaQuery<Venda> criteria = Builder.createQuery(Venda.class);
        Root<Venda> rootven = criteria.from(Venda.class);
        Root<Usuario> rootusu = criteria.from(Usuario.class);
        Root<Empresa> rootemp = criteria.from(Empresa.class);
        criteria.select(rootven);
        Predicate[] predicates = criarPredicatesResumo(Builder, rootven, documentoFilter, rootusu, rootemp);
        criteria.where(predicates);

        TypedQuery<Venda> query = Manager.createQuery(criteria);
        return query.getResultList();
    }


    private Predicate[] criarPredicatesResumo(CriteriaBuilder builder, Root<Venda> rootven, DocumentoFilter documentoFilter, Root<Usuario> rootusu, Root<Empresa> rootemp) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID), rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(rootven.get(Venda_.USUARIO_CRIOU_ID), rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(rootemp.get(Empresa_.ID), documentoFilter.getIdEmpresa()));
        System.out.println(documentoFilter.getDataInicio());
        System.out.println(documentoFilter.getDataFim());
        predicates.add(builder.greaterThanOrEqualTo(rootven.get(Venda_.DATA), documentoFilter.getDataInicio()));
        predicates.add(builder.lessThanOrEqualTo(rootven.get(Venda_.DATA), documentoFilter.getDataFim()));

        return predicates.toArray(new Predicate[0]);
    }

    @Override
    public Page<VendaResumo> resumo(VendaFilter vendaFilter, Pageable pageable) {
        CriteriaBuilder builder = Manager.getCriteriaBuilder();
        CriteriaQuery<VendaResumo> query = builder.createQuery(VendaResumo.class);
        Root<Venda> rootven = query.from(Venda.class);
        Root<Empresa> rootemp = query.from(Empresa.class);
        Root<Usuario> rootusu = query.from(Usuario.class);
        Root<Cliente> rootcl = query.from(Cliente.class);
        Root<Serie> rootser = query.from(Serie.class);
        Root<TipoDocumento> roottpdo = query.from(TipoDocumento.class);
        Join<Object, Object> book = rootven.join(Venda_.CLIENTE, JoinType.LEFT);

        query.select(builder.construct(VendaResumo.class
                , rootven.get(Venda_.id), book.get(Cliente_.NOME),
                rootven.get(Venda_.valorTotal), rootven.get(Venda_.nifClienteVenda),
                rootven.get(Venda_.valorImposto), rootven.get(Venda_.valor), rootven.get(Venda_.data),
                rootven.get(Venda_.dataCriacao), rootven.get(Venda_.status), rootven.get(Venda_.numero)
                , rootser.get(Serie_.numero), rootser.get(Serie_.ano), roottpdo.get(TipoDocumento_.sigla),
                roottpdo.get(TipoDocumento_.descricao)));

        query.groupBy(rootven.get(Venda_.id));

        Predicate[] predicates = criarPredicatesResumo(vendaFilter, builder, rootven, rootusu, rootemp, rootcl, rootser, roottpdo);
        query.where(predicates);

        TypedQuery<VendaResumo> typedQuery = Manager.createQuery(query);
        int size = typedQuery.getResultList().size();
        adicionarRestricoesDePaginaResumo(typedQuery, pageable);
        return new PageImpl<>(typedQuery.getResultList(), pageable, size);
    }

    @Override
    public VendaResumoPro resumoPro(Integer id) {
        CriteriaBuilder builder = Manager.getCriteriaBuilder();
        CriteriaQuery<VendaResumoPro> query = builder.createQuery(VendaResumoPro.class);
        Root<Venda> rootven = query.from(Venda.class);
        Root<Empresa> rootemp = query.from(Empresa.class);
        Root<Usuario> rootusu = query.from(Usuario.class);
        Root<Usuario> rootusu1 = query.from(Usuario.class);
        Join<Object, Object> joincli = rootven.join(Venda_.CLIENTE, JoinType.LEFT);

        query.select(builder.construct(VendaResumoPro.class
                , rootven.get(Venda_.id), joincli.get(Cliente_.NOME),
                rootven.get(Venda_.valorTotal), rootven.get(Venda_.nifClienteVenda),
                rootven.get(Venda_.valorImposto), rootven.get(Venda_.valor), rootven.get(Venda_.data),
                rootven.get(Venda_.dataCriacao), rootven.get(Venda_.status), rootusu.get(Usuario_.nome)
                , rootusu1.get(Usuario_.nome), rootven.get(Venda_.dataAlteracao), rootven.get(Venda_.numero)
                , rootven.get(Venda_.serie).get(Serie_.numero), rootven.get(Venda_.serie).get(Serie_.ano)
                , rootven.get(Venda_.serie).get(Serie_.numeroAutorizacao), rootven.get(Venda_.serie).get(Serie_.tipoDocumento).get(TipoDocumento_.sigla)
                , rootven.get(Venda_.serie).get(Serie_.tipoDocumento).get(TipoDocumento_.descricao)));

        query.groupBy(rootven.get(Venda_.id));

        Predicate[] predicates = criarPredicatesResumoPro(id, builder, rootven, rootusu, rootemp, rootusu1);
        query.where(predicates);
        List<VendaResumoPro> resultList = Manager.createQuery(query).getResultList();
        if (resultList.size() > 0)
            return resultList.get(0);
        else return new VendaResumoPro();
    }

    private void adicionarRestricoesDePaginaResumo(TypedQuery<VendaResumo> typedQuery, Pageable pageable) {
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicatesResumo(VendaFilter vendaFilter, CriteriaBuilder builder, Root<Venda> rootven, Root<Usuario> rootusu, Root<Empresa> rootemp, Root<Cliente> rootcl, Root<Serie> rootser, Root<TipoDocumento> roottpdo) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID), rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(rootven.get(Venda_.USUARIO_CRIOU_ID), rootusu.get(Usuario_.ID)));
        //predicates.add(builder.equal(rootven.get(Venda_.CLIENTE),rootcl));
        predicates.add(builder.equal(rootven.get(Venda_.SERIE), rootser));
        predicates.add(builder.equal(rootven.get(Venda_.SERIE).get(Serie_.TIPO_DOCUMENTO), roottpdo));
        predicates.add(builder.equal(rootemp.get(Empresa_.ID), vendaFilter.getIdEmpresa()));

        if (!StringUtils.isEmpty(vendaFilter.getNomeCliente())) {
            predicates.add(builder.like(builder.lower(rootven.get(Venda_.CLIENTE).get(Cliente_.NOME)), "%" + vendaFilter.getNomeCliente().toLowerCase() + "%"));
        }
        if (vendaFilter.getNifCliente() != null) {
            predicates.add(builder.like(rootven.get(Venda_.CLIENTE).get(Cliente_.NIF), "%" + vendaFilter.getNifCliente() + "%"));
        }
        if (vendaFilter.getDataInicio() != null) {
            predicates.add(builder.greaterThanOrEqualTo(rootven.get(Venda_.DATA), vendaFilter.getDataInicio()));
        }
        if (vendaFilter.getDataFim() != null) {
            predicates.add(builder.lessThanOrEqualTo(rootven.get(Venda_.DATA), vendaFilter.getDataFim()));
        }
        return predicates.toArray(new Predicate[0]);
    }

    private Predicate[] criarPredicatesResumoPro(Integer id, CriteriaBuilder builder, Root<Venda> rootven, Root<Usuario> rootusu, Root<Empresa> rootemp, Root<Usuario> rootusu1) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID), rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(rootusu1.get(Usuario_.EMPRESA).get(Empresa_.ID), rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(rootven.get(Venda_.USUARIO_CRIOU_ID), rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(rootven.get(Venda_.USUARIO_ALTEROU_ID), rootusu1.get(Usuario_.ID)));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:" + id);
        predicates.add(builder.equal(rootven.get(Venda_.ID), id));
        return predicates.toArray(new Predicate[0]);
    }

    private Long total(String nomeCliente) {
        CriteriaBuilder Builder = Manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = Builder.createQuery(Long.class);
        Root<Venda> root = criteria.from(Venda.class);

        Predicate[] predicates = criarPredicates(nomeCliente, Builder, root);
        criteria.where(predicates);
        criteria.select(Builder.count(root));
        return Manager.createQuery(criteria).getSingleResult();
    }

    private void adicionarRestricoesDePagina(TypedQuery<Venda> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicates(String nomeCliente, CriteriaBuilder builder, Root<Venda> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(nomeCliente)) {
            predicates.add(builder.like(builder.lower(root.get(Venda_.CLIENTE).get(Cliente_.NOME)), "%" + nomeCliente.toLowerCase() + "%"));
        }
        if (!StringUtils.isEmpty(nomeCliente)) {
            predicates.add(builder.like(builder.lower(root.get(Venda_.CLIENTE).get(Cliente_.NOME)), "%" + nomeCliente.toLowerCase() + "%"));
        }
        return predicates.toArray(new Predicate[0]);
    }
}
