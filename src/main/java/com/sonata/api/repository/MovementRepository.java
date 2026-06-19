package com.sonata.api.repository;

import com.sonata.api.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repositorio JPA para {@link Movement}. */
@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    /** Lista movimentos de uma obra ja ordenados (suporta gapless). */
    List<Movement> findByWork_WorkIdOrderByMovementOrderAsc(Long workId);
}
