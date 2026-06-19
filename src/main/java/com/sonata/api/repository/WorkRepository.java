package com.sonata.api.repository;

import com.sonata.api.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repositorio JPA para {@link Work} (obras). */
@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findByComposer_ComposerId(Long composerId);

    List<Work> findByTitleContainingIgnoreCase(String title);
}
