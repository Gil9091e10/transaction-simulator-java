package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.DebitCard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DebitCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DebitCardRepository extends JpaRepository<DebitCard, Long>, JpaSpecificationExecutor<DebitCard> {}
