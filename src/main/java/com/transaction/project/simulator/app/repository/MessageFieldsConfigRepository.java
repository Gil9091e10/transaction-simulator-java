package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.MessageFieldsConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MessageFieldsConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageFieldsConfigRepository
    extends JpaRepository<MessageFieldsConfig, Long>, JpaSpecificationExecutor<MessageFieldsConfig> {}
