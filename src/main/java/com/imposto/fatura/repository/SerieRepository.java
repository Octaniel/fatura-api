package com.imposto.fatura.repository;

import com.imposto.fatura.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Integer> {
}
