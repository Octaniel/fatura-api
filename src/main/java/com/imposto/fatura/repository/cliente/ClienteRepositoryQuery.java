package com.imposto.fatura.repository.cliente;

import com.imposto.fatura.repository.filter.ClienteFilter;
import com.imposto.fatura.repository.projection.ClienteResumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteRepositoryQuery {
    Page<ClienteResumo> resumo(ClienteFilter clienteFilter, Pageable pageable);
}
