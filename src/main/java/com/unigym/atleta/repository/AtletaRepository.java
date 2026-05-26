package com.unigym.atleta.repository;

import com.unigym.atleta.domain.Atleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AtletaRepository extends JpaRepository<Atleta, Long> {
    boolean existsByEmail(String email);
    Optional<Atleta> findByEmail(String email);
}
