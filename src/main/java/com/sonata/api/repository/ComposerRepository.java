package com.sonata.api.repository;

import com.sonata.api.model.Composer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repositorio JPA para {@link Composer}. */
@Repository
public interface ComposerRepository extends JpaRepository<Composer, Long> {

    /** Busca por periodo historico (Barroco, Classico, Romantico...). */
    List<Composer> findByHistoricalPeriodIgnoreCase(String historicalPeriod);

    List<Composer> findByNameContainingIgnoreCase(String name);
}
