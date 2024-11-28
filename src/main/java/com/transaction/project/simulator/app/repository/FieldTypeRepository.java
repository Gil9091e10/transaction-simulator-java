package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.FieldType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FieldType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldTypeRepository extends JpaRepository<FieldType, Long>, JpaSpecificationExecutor<FieldType> {}
