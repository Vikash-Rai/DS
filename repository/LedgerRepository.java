package com.equabli.datascrubbing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.Ledger;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
	
}