package com.illo.blm.repository;

import com.illo.blm.domain.PropertyPricing;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PropertyPricing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyPricingRepository extends JpaRepository<PropertyPricing, Long> {
}
