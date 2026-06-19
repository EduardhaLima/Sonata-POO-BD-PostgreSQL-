package com.sonata.api.repository;

import com.sonata.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para {@link User}.
 *
 * <p>Spring Data gera a implementacao em runtime - basta declarar
 * o metodo que ele monta a query a partir do nome.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** Usado no login - email eh unico. */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
