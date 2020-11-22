package com.illo.blm.repository;

import com.illo.blm.domain.SalesProperty;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SalesProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesPropertyRepository extends JpaRepository<SalesProperty, Long> {
}
