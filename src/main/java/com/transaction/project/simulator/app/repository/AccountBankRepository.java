package com.transaction.project.simulator.app.repository;

import com.transaction.project.simulator.app.domain.AccountBank;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountBank entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountBankRepository extends JpaRepository<AccountBank, Long>, JpaSpecificationExecutor<AccountBank> {}
