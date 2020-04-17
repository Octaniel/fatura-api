package com.imposto.fatura.repository;

import com.imposto.fatura.model.Cliente;
import com.imposto.fatura.repository.cliente.ClienteRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>, ClienteRepositoryQuery {
}
