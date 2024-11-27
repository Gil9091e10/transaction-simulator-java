package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.Advice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Advice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdviceRepository extends JpaRepository<Advice, Long>, JpaSpecificationExecutor<Advice> {}
