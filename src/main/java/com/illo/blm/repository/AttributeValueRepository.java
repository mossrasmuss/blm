package com.illo.blm.repository;

import com.illo.blm.domain.AttributeValue;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AttributeValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
}
