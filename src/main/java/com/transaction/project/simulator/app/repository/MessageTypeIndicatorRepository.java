package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.MessageTypeIndicator;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MessageTypeIndicator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageTypeIndicatorRepository
    extends JpaRepository<MessageTypeIndicator, Long>, JpaSpecificationExecutor<MessageTypeIndicator> {}
