package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.MessageFieldType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MessageFieldType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageFieldTypeRepository extends JpaRepository<MessageFieldType, Long>, JpaSpecificationExecutor<MessageFieldType> {}
