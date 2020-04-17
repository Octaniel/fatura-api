package com.imposto.fatura.validation;

import com.imposto.fatura.model.Produto;
import com.imposto.fatura.model.TipoOferta;
import com.imposto.fatura.validation.group.ProdutoGroup;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class OfertaGroupSequenceProvider implements DefaultGroupSequenceProvider<Produto> {
    @Override
    public List<Class<?>> getValidationGroups(Produto produto) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Produto.class);
        if(produto !=null && produto.getNatureza()!=null)
        if(produto.getNatureza().equals(TipoOferta.PRODUTO))groups.add(ProdutoGroup.class);

        return groups;
    }
}
