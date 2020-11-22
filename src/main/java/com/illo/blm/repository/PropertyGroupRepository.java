package com.illo.blm.repository;

import com.illo.blm.domain.PropertyGroup;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PropertyGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyGroupRepository extends JpaRepository<PropertyGroup, Long> {
}
