package com.unigym.atleta.repository;

import java.util.Optional;

import com.unigym.atleta.domain.Atleta;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AtletaRepository extends JpaRepository<Atleta, Long> {

	Optional<Atleta> findByEmailIgnoreCase(String email);
}
