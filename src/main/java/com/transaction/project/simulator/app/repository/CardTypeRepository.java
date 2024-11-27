package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.CardType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CardType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Long>, JpaSpecificationExecutor<CardType> {}
