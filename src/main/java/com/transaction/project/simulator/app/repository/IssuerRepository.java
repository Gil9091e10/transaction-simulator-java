package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.Issuer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Issuer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssuerRepository extends JpaRepository<Issuer, Long>, JpaSpecificationExecutor<Issuer> {}
