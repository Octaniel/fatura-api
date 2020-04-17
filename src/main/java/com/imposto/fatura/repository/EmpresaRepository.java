package com.imposto.fatura.repository;

import com.imposto.fatura.model.Empresa;
import com.imposto.fatura.repository.empresa.EmpresaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>, EmpresaRepositoryQuery {
}
