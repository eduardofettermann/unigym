package com.unigym.atleta.repository;

import com.unigym.atleta.domain.Atleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtletaRepository extends JpaRepository<Atleta, Long> {
    boolean existsByEmail(String email);
}
