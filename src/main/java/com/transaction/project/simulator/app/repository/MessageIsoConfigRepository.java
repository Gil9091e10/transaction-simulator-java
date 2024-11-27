package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.MessageIsoConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MessageIsoConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageIsoConfigRepository extends JpaRepository<MessageIsoConfig, Long>, JpaSpecificationExecutor<MessageIsoConfig> {}
