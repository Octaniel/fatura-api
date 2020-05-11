package com.imposto.fatura.repository.produto;

import com.imposto.fatura.model.*;
import com.imposto.fatura.repository.filter.ProdutoFilter;
import com.imposto.fatura.repository.projection.ProdutoResumo;
import com.imposto.fatura.repository.projection.ServicoResumo;
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

public class ProdutoRepositoryImpl implements ProdutoRepositoryQuery {

    @PersistenceContext
    private EntityManager Manager;

    @Override
    public Page<?> resumo(ProdutoFilter produtoFilter, Pageable pageable) {
        CriteriaBuilder builder = Manager.getCriteriaBuilder();
        if (produtoFilter.getNatureza().equals("PRODUTO")) {
            CriteriaQuery<ProdutoResumo> query = builder.createQuery(ProdutoResumo.class);
            Root<Produto> rootpro = query.from(Produto.class);
            Root<Empresa> rootemp = query.from(Empresa.class);
            Root<Usuario> rootusu = query.from(Usuario.class);
            Join<Object, Object> modelojoin = rootpro.join(Produto_.MODELO, JoinType.LEFT);
            Join<Object, Object> tipprodutojoin = rootpro.join(Produto_.TIPO_PRODUTO, JoinType.LEFT);
            Join<Object, Object> marcajoin = modelojoin.join(Modelo_.MARCA, JoinType.LEFT);
            Join<Object, Object> taxajoin = rootpro.join(Produto_.TAXA, JoinType.LEFT);

            query.select(builder.construct(ProdutoResumo.class
                    , rootpro.get(Produto_.id), rootpro.get(Produto_.nome), modelojoin.get(Modelo_.NOME),
                    tipprodutojoin.get(TipoProduto_.DESCRICAO),
                    rootpro.get(Produto_.precoVenda), rootpro.get(Produto_.status), rootpro.get(Produto_.unidade),
                    rootpro.get(Produto_.stock), marcajoin.get(Marca_.NOME),
                    rootpro.get(Produto_.codigo), taxajoin.get(Taxa_.DESCRICAO),
                    taxajoin.get(Taxa_.VALOR),rootpro.get(Produto_.valorInalteravel),rootpro.get(Produto_.emPromocao)));


            Predicate[] predicates = criarPredicatesResumo(produtoFilter, builder, rootpro, rootusu, rootemp);
            query.where(predicates);

            TypedQuery<ProdutoResumo> typedQuery = Manager.createQuery(query);
            List<ProdutoResumo> list = new ArrayList<>();
            int size = typedQuery.getResultList().size();
            adicionarRestricoesDePaginaResumo(typedQuery, pageable);

            return new PageImpl<>(typedQuery.getResultList(), pageable, size);
        } else {
            CriteriaQuery<ServicoResumo> query = builder.createQuery(ServicoResumo.class);
            Root<Produto> rootpro = query.from(Produto.class);
            Root<Empresa> rootemp = query.from(Empresa.class);
            Root<Usuario> rootusu = query.from(Usuario.class);
            Join<Object, Object> taxajoin = rootpro.join(Produto_.TAXA, JoinType.LEFT);
            Join<Object, Object> tipprodutojoin = rootpro.join(Produto_.TIPO_PRODUTO, JoinType.LEFT);
            Join<Object, Object> motivoisenjoin = rootpro.join(Produto_.MOTIVO_ISENCAO, JoinType.LEFT);

            query.select(builder.construct(ServicoResumo.class
                    , rootpro.get(Produto_.id), rootpro.get(Produto_.nome),
                    tipprodutojoin.get(TipoProduto_.DESCRICAO),
                    rootpro.get(Produto_.precoVenda), rootpro.get(Produto_.status), taxajoin.get(Taxa_.DESCRICAO),
                    motivoisenjoin.get(MotivoIsencao_.DESCRICAO), motivoisenjoin.get(MotivoIsencao_.CODIGO)
                    , taxajoin.get(Taxa_.VALOR)));
            Predicate[] predicates = criarPredicatesResumo(produtoFilter, builder, rootpro, rootusu, rootemp);
            query.where(predicates);
            TypedQuery<ServicoResumo> typedQuery = Manager.createQuery(query);
            int size = typedQuery.getResultList().size();
            adicionarRestricoesDePaginaResumo(typedQuery, pageable);
            return new PageImpl<>(typedQuery.getResultList(), pageable, size);
        }
    }

    private List<Predicate> criarPredicatePadrao(ProdutoFilter produtoFilter, CriteriaBuilder builder, Root<Produto> rootpro, Root<Usuario> rootusu, Root<Empresa> rootemp) {
        TipoOferta tipoOferta = TipoOferta.valueOf(produtoFilter.getNatureza());
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(rootusu.get(Usuario_.EMPRESA).get(Empresa_.ID), rootemp.get(Empresa_.ID)));
        predicates.add(builder.equal(rootpro.get(Produto_.USUARIO_CRIOU_ID), rootusu.get(Usuario_.ID)));
        predicates.add(builder.equal(rootpro.get(Produto_.NATUREZA), tipoOferta));
        return predicates;
    }

    private void adicionarRestricoesDePaginaResumo(TypedQuery<?> typedQuery, Pageable pageable) {
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
    }

    private Predicate[] criarPredicatesResumo(ProdutoFilter produtoFilter, CriteriaBuilder builder, Root<Produto> rootpro, Root<Usuario> rootusu, Root<Empresa> rootemp) {
        List<Predicate> predicates = criarPredicatePadrao(produtoFilter, builder, rootpro, rootusu, rootemp);
        return getPredicates(produtoFilter, builder, rootpro, rootemp, predicates);
    }

    private Predicate[] getPredicates(ProdutoFilter produtoFilter, CriteriaBuilder builder, Root<Produto> rootpro, Root<Empresa> rootemp, List<Predicate> predicates) {
        TipoOferta tipoOferta = TipoOferta.valueOf(produtoFilter.getNatureza());
        predicates.add(builder.equal(rootemp.get(Empresa_.ID), produtoFilter.getIdEmpresa()));

        if (!StringUtils.isEmpty(produtoFilter.getNome())) {
            predicates.add(builder.like(builder.lower(rootpro.get(Produto_.NOME)), "%" + produtoFilter.getNome().toLowerCase() + "%"));
        }
        if (!StringUtils.isEmpty(produtoFilter.getModelo())) {
            predicates.add(builder.like(builder.lower(rootpro.get(Produto_.MODELO)), "%" + produtoFilter.getModelo().toLowerCase() + "%"));
        }
        if (produtoFilter.getTipoProduto() != null) {
            predicates.add(builder.equal(rootpro.get(Produto_.TIPO_PRODUTO).get(TipoProduto_.ID), produtoFilter.getTipoProduto()));
        }
        if (!StringUtils.isEmpty(produtoFilter.getUnidade())) {
            predicates.add(builder.like(builder.lower(rootpro.get(Produto_.UNIDADE)), "%" + produtoFilter.getUnidade().toLowerCase() + "%"));
        }
        if (!StringUtils.isEmpty(produtoFilter.getCodigo())) {
            predicates.add(builder.like(builder.lower(rootpro.get(Produto_.CODIGO)), "%" + produtoFilter.getCodigo().toLowerCase() + "%"));
        }
        if (produtoFilter.getStatus() != null && produtoFilter.getStatus().equals(true)) {
            predicates.add(builder.equal(rootpro.get(Produto_.STATUS), produtoFilter.getStatus()));
        }

        if(tipoOferta.equals(TipoOferta.PRODUTO))predicates.add(builder.greaterThan(rootpro.get(Produto_.STOCK), 0));

        return predicates.toArray(new Predicate[0]);
    }

}
