package com.sonata.api.repository;

import com.sonata.api.model.PlayHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repositorio JPA para {@link PlayHistory} . */
@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {

    /** Ultimas reproducoes do usuario, mais recentes primeiro. */
    List<PlayHistory> findByUser_UserIdOrderByPlayedAtDesc(Long userId, Pageable pageable);
}
