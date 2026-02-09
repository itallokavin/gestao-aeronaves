package com.sonda.gestao_aeronaves.persistence.repository;

import com.sonda.gestao_aeronaves.persistence.entity.Aircraft;
import com.sonda.gestao_aeronaves.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    @Query("SELECT a FROM Aircraft a WHERE " +
           "UPPER(a.name) LIKE UPPER(CONCAT('%', :term, '%')) OR " +
           "UPPER(a.description) LIKE UPPER(CONCAT('%', :term, '%')) OR " +
           "UPPER(CAST(a.brand AS string)) LIKE UPPER(CONCAT('%', :term, '%'))")
    List<Aircraft> findByTerm(@Param("term") String term);

    long countBySoldFalse();

    List<Aircraft> findByCreatedAfter(LocalDateTime data);

    List<Aircraft> findByBrand(Brand brand);
}