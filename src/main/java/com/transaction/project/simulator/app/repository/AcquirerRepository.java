package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.Acquirer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Acquirer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcquirerRepository extends JpaRepository<Acquirer, Long>, JpaSpecificationExecutor<Acquirer> {}
